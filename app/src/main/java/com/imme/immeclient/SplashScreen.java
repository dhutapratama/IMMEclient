package com.imme.immeclient;

/**
 * Created by lasedev on 11/11/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Give permission to use internet
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Better quiting apps
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        if (getIntent().getStringExtra("apps_name") != null) {
            Intent intent = new Intent(this, FastPaymentActivity.class);
            intent.putExtra("apps_name", getIntent().getStringExtra("apps_name"));
            startActivity(intent);
        } else {
            SecurityData mDbHelper = new SecurityData(this);
            SQLiteDatabase db =  mDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    SecurityData.SESSION_KEY,
                    SecurityData.FIRST_TIME
            };

            Cursor collected_data = db.query(
                    SecurityData.TABLE_LOGIN_DATA, // The table to query
                    projection, // The columns to return
                    null, // The columns for the WHERE clause
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            collected_data.moveToFirst();
            String session_key = collected_data.getString(collected_data.getColumnIndexOrThrow(SecurityData.SESSION_KEY));

            db.close();
            if (!session_key.equals("")) {
                GlobalVariable.SECURITY_SESSION_KEY = collected_data.getString(collected_data.getColumnIndexOrThrow("session_key"));
                new session_check().execute();
            } else {
                int first_time = collected_data.getInt(collected_data.getColumnIndexOrThrow(SecurityData.FIRST_TIME));
                if (first_time == 1) {
                    Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                }
            }
        }

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    private class session_check extends AsyncTask<String, String, JSONObject> {
        protected JSONObject doInBackground(String... args) {
            JSONObject serviceResult = null;
            try {
                String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8");
                serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "login/session_check", postData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return serviceResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(JSONObject feedback_data) {
            if (feedback_data.length() == 0) {
                Toast.makeText(SplashScreen.this, "Server issue, please contact 081235404833", Toast.LENGTH_LONG).show();
            } else {
                try {
                    if (feedback_data.getBoolean("error")) {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
