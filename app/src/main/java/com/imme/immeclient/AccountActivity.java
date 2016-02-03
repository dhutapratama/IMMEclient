package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class AccountActivity extends AppCompatActivity {

    ScrollView AccountSettings;
    ProgressBar LoadingAnimation;
    ProgressDialog LoadingDialog;
    TextView AccountNumber, IDNumber, IDType, BTN_Save;
    EditText FullName, Email, PhoneNumber;
    ImageView AccountPicture;
    LinearLayout EmailVerification, PhoneVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF249962"));

        LoadingAnimation = (ProgressBar) findViewById(R.id.LoadingAnimation);
        AccountSettings = (ScrollView) findViewById(R.id.AccountSettings);
        AccountPicture = (ImageView) findViewById(R.id.AccountPicture);
        AccountNumber = (TextView) findViewById(R.id.account_textview_account_number_value);
        FullName = (EditText) findViewById(R.id.account_edittext_full_name_value);
        Email = (EditText) findViewById(R.id.account_edittext_email_value);
        PhoneNumber = (EditText) findViewById(R.id.account_edittext_phone_number_value);;
        IDNumber = (TextView) findViewById(R.id.account_textview_id_number_value);
        IDType = (TextView) findViewById(R.id.account_textview_id_type_value);
        EmailVerification = (LinearLayout) findViewById(R.id.EmailVerification);
        PhoneVerification = (LinearLayout) findViewById(R.id.PhoneVerification);
        BTN_Save = (TextView) findViewById(R.id.account_button_save);

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        //TextView account_textview_balance = (TextView) findViewById(R.id.account_textview_balance);
        //account_textview_balance.setTypeface(hnLight);

        //TextView account_textview_balance_rp = (TextView) findViewById(R.id.account_textview_balance_rp);
        //account_textview_balance_rp.setTypeface(hnLight);

        //TextView account_textview_balance_value = (TextView) findViewById(R.id.account_textview_balance_value);
        //account_textview_balance_value.setTypeface(hbqLight);

        //TextView account_textview_balance_gift = (TextView) findViewById(R.id.account_textview_balance_gift);
        //account_textview_balance_gift.setTypeface(hnLight);

        //TextView account_textview_balance_gift_rp = (TextView) findViewById(R.id.account_textview_balance_gift_rp);
        //account_textview_balance_gift_rp.setTypeface(hnLight);

        //TextView account_textview_balance_gift_value = (TextView) findViewById(R.id.account_textview_balance_gift_value);
        //account_textview_balance_gift_value.setTypeface(hbqLight);

        TextView account_textview_account_number = (TextView) findViewById(R.id.account_textview_account_number);
        account_textview_account_number.setTypeface(hnLight);

        //account_textview_account_number_value.setTypeface(hnLight);

        TextView account_textview_account_setting = (TextView) findViewById(R.id.account_textview_account_setting);
        account_textview_account_setting.setTypeface(hbqLight);

        TextView account_textview_full_name = (TextView) findViewById(R.id.account_textview_full_name);
        account_textview_full_name.setTypeface(hnLight);

        //account_edittext_full_name_value.setTypeface(hnLight);

        TextView account_textview_profil_image = (TextView) findViewById(R.id.account_textview_profil_image);
        account_textview_profil_image.setTypeface(hnLight);

        TextView account_textview_upload_image = (TextView) findViewById(R.id.account_textview_upload_image);
        account_textview_upload_image.setTypeface(hnLight);

        TextView account_textview_max_size = (TextView) findViewById(R.id.account_textview_max_size);
        account_textview_max_size.setTypeface(hnLight);

        TextView account_textview_email = (TextView) findViewById(R.id.account_textview_email);
        account_textview_email.setTypeface(hnLight);

        //account_edittext_email_value.setTypeface(hnLight);

        TextView account_textview_phone_number = (TextView) findViewById(R.id.account_textview_phone_number);
        account_textview_phone_number.setTypeface(hnLight);

        //account_edittext_phone_number_value.setTypeface(hnLight);

        //account_button_save.setTypeface(hbqLight);

        TextView account_textview_account_details = (TextView) findViewById(R.id.account_textview_account_details);
        account_textview_account_details.setTypeface(hbqLight);

        TextView account_textview_id_number = (TextView) findViewById(R.id.account_textview_id_number);
        account_textview_id_number.setTypeface(hnLight);

        //account_textview_id_number_value.setTypeface(hnLight);

        TextView account_textview_id_type = (TextView) findViewById(R.id.account_textview_id_type);
        account_textview_id_type.setTypeface(hnLight);

        //account_textview_id_type_value.setTypeface(hnLight);

        TextView account_textview_resend_verification_email = (TextView) findViewById(R.id.account_textview_resend_verification_email);
        account_textview_resend_verification_email.setTypeface(hnLight);

        TextView account_textview_verification_email = (TextView) findViewById(R.id.account_textview_verification_email);
        account_textview_verification_email.setTypeface(hbqLight);

        TextView account_textview_verification_phone_number = (TextView) findViewById(R.id.account_textview_verification_phone_number);
        account_textview_verification_phone_number.setTypeface(hbqLight);

        TextView account_textview_verify_phone_number = (TextView) findViewById(R.id.account_textview_verify_phone_number);
        account_textview_verify_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, AccountVerifyPhoneNumberStep1Activity.class);
                startActivity(intent);
            }
        });

        new get_account().execute();

        BTN_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new save_account().execute();
            }
        });

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

    private class get_account extends AsyncTask<String, String, JSONObject> {
        protected JSONObject doInBackground(String... args) {
            JSONObject serviceResult = null;
            try {
                String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8");
                serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "setting/get_account_setting", postData);
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
            //if (AccountActivity.this.loading != null) {
            //    AccountActivity.this.loading.dismiss();
            //}
            if (feedback_data.length() == 0) {
                Toast.makeText(AccountActivity.this, "Server issue, please contact 081235404833", Toast.LENGTH_LONG).show();
                //return;
            }

            // Error handling
            try {
                if (feedback_data.getBoolean("error")) {
                    //feedback_data.getInt("code")

                    Toast.makeText(AccountActivity.this, feedback_data.getString("message"), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    JSONObject data = feedback_data.getJSONObject("data");
                    LoadingAnimation.setVisibility(View.GONE);

                    AccountSettings.setVisibility(View.VISIBLE);
                    AccountNumber.setText(data.getString("account_number"));

                    FullName.setText(data.getString("full_name"));
                    Email.setText(data.getString("email"));
                    PhoneNumber.setText(data.getString("phone_number"));
                    IDType.setText(data.getString("idcard_type"));
                    IDNumber.setText(data.getString("idcard_number"));

                    if (data.getBoolean("verified_email")) {
                        EmailVerification.setVisibility(View.GONE);
                    }

                    if (data.getBoolean("verified_phone")) {
                        PhoneVerification.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class save_account extends AsyncTask<String, String, JSONObject> {
        // Initialization global variable for private AsyncTask class
        String full_name, email, phone_number;

        protected JSONObject doInBackground(String... args) {
            JSONObject serviceResult = null;
            try {
                String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                        + "&full_name=" + URLEncoder.encode(full_name, "UTF-8")
                        + "&email=" + URLEncoder.encode(email, "UTF-8")
                        + "&phone_number=" + URLEncoder.encode(phone_number, "UTF-8");
                serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "setting/save_account_setting", postData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return serviceResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingDialog = ProgressDialog.show(AccountActivity.this, "", "Saving your account", false);
            full_name = FullName.getText().toString();
            email = Email.getText().toString();
            phone_number = PhoneNumber.getText().toString();
        }

        protected void onPostExecute(JSONObject feedback_data) {
            if (LoadingDialog != null) {
                LoadingDialog.dismiss();
            }
            //if (AccountActivity.this.loading != null) {
            //    AccountActivity.this.loading.dismiss();
            //}
            if (feedback_data.length() == 0) {
                Toast.makeText(AccountActivity.this, "Server issue, please contact 081235404833", Toast.LENGTH_LONG).show();
                //return;
            }

            // Error handling
            try {
                if (feedback_data.getBoolean("error")) {
                    //feedback_data.getInt("code")
                    Toast.makeText(AccountActivity.this, feedback_data.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    JSONObject data = feedback_data.getJSONObject("data");
                    Toast.makeText(AccountActivity.this, data.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
