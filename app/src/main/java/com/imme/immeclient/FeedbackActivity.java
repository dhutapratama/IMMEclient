package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class FeedbackActivity extends AppCompatActivity {

    ProgressDialog loading;
    Boolean error_status = false;
    String error_message, message;
    Integer error_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#FF249962"));

        Button btn_send_feedback = (Button) findViewById(R.id.btn_send_feedback);
        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text_feedback = (EditText) findViewById(R.id.EditTextFeedbackBody);
                message = text_feedback.getText().toString();
                loading = ProgressDialog.show(FeedbackActivity.this, "", "Sending Feedback", false, true);
                new send_feedback().execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class send_feedback extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                sendFeedback();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (FeedbackActivity.this.loading != null) {
                FeedbackActivity.this.loading.dismiss();
            }
            if (error_status) {
                finish();
                Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(FeedbackActivity.this, error_message, Toast.LENGTH_LONG).show();
            } else {
                finish();
                Intent intentView = new Intent(getApplicationContext(), MainActivity.class);
                intentView.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentView);
                Toast.makeText(FeedbackActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void sendFeedback() throws JSONException, IOException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&message=" + URLEncoder.encode(message, "UTF-8");

         JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "feedback/send", postData);

        if (serviceResult.getBoolean("error")){
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
            message = serviceResult.getString("message");
        }
    }
}
