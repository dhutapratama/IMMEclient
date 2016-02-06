package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class SignInActivity extends AppCompatActivity {
    private String email = null;
    private String password = null;
    private ProgressDialog loading = null;
    Boolean login_error = false;
    private String message = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#ff0f99da"));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        final EditText sign_in_edittext_email = (EditText) findViewById(R.id.sign_in_edittext_email);
        final EditText sign_in_edittext_password = (EditText) findViewById(R.id.sign_in_edittext_password);
        final Button sign_in_button_sign_in = (Button) findViewById(R.id.sign_in_button_sign_in);
        final TextView sign_in_button_sign_up = (TextView) findViewById(R.id.sign_in_button_sign_up);
        final TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword);

        sign_in_edittext_email.setTypeface(hnLight);
        sign_in_edittext_password.setTypeface(hnLight);
        sign_in_button_sign_in.setTypeface(hbqLight);
        sign_in_button_sign_up.setTypeface(hbqLight);
        forgotPassword.setTypeface(hbqLight);

        sign_in_button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.imme.immeclient.SignUpActivity");
                startActivity(intent);
            }
        });

        // Sign in button action
        sign_in_button_sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            email = sign_in_edittext_email.getText().toString();
            password = sign_in_edittext_password.getText().toString();
            new login_check().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    private class login_check extends AsyncTask<String, String, JSONObject> {
        protected JSONObject doInBackground(String... args) {
            JSONObject serviceResult = null;
            try {
                String postData ="email=" + URLEncoder.encode(email, "UTF-8")
                        + "&password=" + URLEncoder.encode(password, "UTF-8");
                serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "login", postData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return serviceResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(SignInActivity.this, "", "Sign in...", true);
        }

        protected void onPostExecute(JSONObject feedback_data) {
            loading.dismiss();

            if (feedback_data.length() == 0) {
                Toast.makeText(SignInActivity.this, "Server issue, please contact 081235404833", Toast.LENGTH_LONG).show();
            } else {
                try {
                    if (feedback_data.getBoolean("error")) {
                        Toast.makeText(SignInActivity.this, feedback_data.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject data = feedback_data.getJSONObject("data");

                        // SQL Logging data
                        SecurityData mDbHelper = new SecurityData(SignInActivity.this);
                        SQLiteDatabase db = mDbHelper.getReadableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(SecurityData.SESSION_KEY, data.getString("session_key"));
                        values.put(SecurityData.FIRST_TIME, 0);
                        db.update(SecurityData.TABLE_LOGIN_DATA, values, null, null);
                        db.close();

                        // Setting GlobalVariable
                        GlobalVariable.SECURITY_SESSION_KEY = data.getString("session_key");

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        SignInActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
