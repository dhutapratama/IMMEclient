package com.imme.immeclient;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class InviteGetMoneyActivity extends AppCompatActivity {

    Button ReferralCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_get_money);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF249962"));

        ReferralCode = (Button) findViewById(R.id.ReferralCode);
        new get_referral_code().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class get_referral_code extends AsyncTask<String, String, JSONObject> {
        protected JSONObject doInBackground(String... args) {
            JSONObject serviceResult = null;
            try {
                String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8");
                serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "info/referral_code", postData);
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
                Toast.makeText(InviteGetMoneyActivity.this, "Server issue, please contact 081235404833", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            try {
                if (feedback_data.getBoolean("error")) {
                    Toast.makeText(InviteGetMoneyActivity.this, feedback_data.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    JSONObject data = feedback_data.getJSONObject("data");
                    ReferralCode.setText(data.getString("referral_code"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
