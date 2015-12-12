package com.imme.immeclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Enumeration;

public class SignInActivity extends AppCompatActivity {
    public String email = new String();
    public String password = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean first_time = true;
        try {
            first_time = checkFirstTimeApp();
        } catch (JSONException e) {
            Toast.makeText(this, "Error JSON Format", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Apps Error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if (first_time) {
            // Tutorial 3 Halaman
            startActivity(new Intent(SignInActivity.this, WelcomeScreen.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#ff0f99da"));

        //TextView sign_in_textview_sign_up = (TextView) findViewById(R.id.sign_in_textview_sign_up);
        //sign_in_textview_sign_up.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View view) {
        //   Intent intent = new Intent("com.imme.immeclient.SignUpActivity");
        //    startActivity(intent);
        // }
        //});

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        final EditText sign_in_edittext_email = (EditText) findViewById(R.id.sign_in_edittext_email);
        sign_in_edittext_email.setTypeface(hnLight);

        final EditText sign_in_edittext_password = (EditText) findViewById(R.id.sign_in_edittext_password);
        sign_in_edittext_password.setTypeface(hnLight);

        final TextView sign_in_button_sign_in = (TextView) findViewById(R.id.sign_in_button_sign_in);
        sign_in_button_sign_in.setTypeface(hbqLight);

        final TextView sign_in_button_sign_up = (TextView) findViewById(R.id.sign_in_button_sign_up);
        sign_in_button_sign_up.setTypeface(hbqLight);

        final TextView dont_have_account = (TextView) findViewById(R.id.dont_have_account);
        dont_have_account.setTypeface(hbqLight);

        // sign up destination
        sign_in_button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.imme.immeclient.SignUpActivity");
                startActivity(intent);
            }
        });

        // Sign in button action
        sign_in_button_sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    email = sign_in_edittext_email.getText().toString();
                    password = sign_in_edittext_password.getText().toString();
                    doLogin();
                } catch (JSONException e) {
                    Toast.makeText(SignInActivity.this, "Error JSON Format", Toast.LENGTH_LONG).show();
                    Toast.makeText(SignInActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                    Log.e("JSONException", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("IOException", e.toString());
                    e.printStackTrace();
                }

                if (GlobalVariable.LOGIN_STATUS.equals("true")) {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void writeFile(String varname, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(varname, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFile(String varname) {
        String ret = "";
        try {
            InputStream inputStream = openFileInput(varname);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("SplashScreen", "File not found: " + e.toString());
            writeFile(varname, "created");
            ret = "created";
        } catch (IOException e) {
            Log.e("SplashScreen", "Can not read file: " + e.toString());
        }
        return ret;
    }

    public void doLogin() throws JSONException, IOException {
        String fileContent = readFile(GlobalVariable.MOBILESTATUS_FILE);
        JSONObject mobileStatus = new JSONObject(fileContent);
        JSONObject mobileSecurity = new JSONObject();
        JSONObject balanceData = new JSONObject();
        JSONObject userData = new JSONObject();

        GlobalVariable.CSRF_TOKEN = mobileStatus.getString("csrf_token");
        String request_code = "1002";

        String postData = "request_code=" + request_code
                + "&csrf_token=" + URLEncoder.encode(GlobalVariable.CSRF_TOKEN, "UTF-8")
                + "&email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&device_ip=" + URLEncoder.encode(GlobalVariable.CLIENT_IP(), "UTF-8");


        //Toast.makeText(this, postData, Toast.LENGTH_LONG).show();

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER, postData);

        if (serviceResult.getBoolean("error")){
            Toast.makeText(this, serviceResult.getString("message"), Toast.LENGTH_LONG).show();
            GlobalVariable.CSRF_TOKEN = serviceResult.getString("csrf_token");
            mobileStatus.put("csrf_token", serviceResult.getString("csrf_token"));
        } else {
            Toast.makeText(this, serviceResult.getString("login_message"), Toast.LENGTH_LONG).show();

            mobileStatus.put("first_time_app", "false");
            mobileStatus.put("login_status", "true");
            mobileStatus.put("csrf_token", serviceResult.getString("csrf_token"));
            mobileStatus.put("imme_algorithm", serviceResult.getString("imme_algorithm"));
            GlobalVariable.CSRF_TOKEN = serviceResult.getString("csrf_token");
            GlobalVariable.LOGIN_STATUS = "true";

            mobileSecurity.put("tba_algorithm", serviceResult.getString("tba_algorithm"));
            mobileSecurity.put("cba_algorithm", serviceResult.getString("cba_algorithm"));
            mobileSecurity.put("cba_counter", serviceResult.getString("cba_counter"));
            mobileSecurity.put("session_key", serviceResult.getString("session_key"));
            GlobalVariable.TBA_ALGORITHM = serviceResult.getString("tba_algorithm");
            GlobalVariable.CBA_ALGORITHM = serviceResult.getString("cba_algorithm");
            GlobalVariable.CBA_COUNTER = serviceResult.getString("cba_counter");
            GlobalVariable.SESSION_KEY = serviceResult.getString("session_key");

            JSONObject account = serviceResult.getJSONObject("account");

            balanceData.put("main_balance", account.getString("balance"));
            balanceData.put("gift_balance", account.getString("gift_balance"));
            GlobalVariable.MAIN_BALANCE = Integer.parseInt(account.getString("balance"));
            GlobalVariable.GIFT_BALANCE = Integer.parseInt(account.getString("gift_balance"));

            userData.put("account_number", account.getString("account_number"));
            userData.put("full_name", account.getString("full_name"));
            userData.put("picture_url", account.getString("picture_url"));
            userData.put("email", account.getString("email"));
            userData.put("phone_number", account.getString("phone_number"));
            userData.put("idcard_number", account.getString("idcard_number"));
            userData.put("idcard_type", account.getString("idcard_type"));

            GlobalVariable.ACCOUNT_NUMBER = account.getString("account_number");
            GlobalVariable.FULL_NAME = account.getString("full_name");
            GlobalVariable.PICTURE_URL = account.getString("picture_url");
            GlobalVariable.EMAIL = account.getString("email");
            GlobalVariable.PHONE_NUMBER = account.getString("phone_number");
            GlobalVariable.IDCARD_NUMBER = account.getString("idcard_number");
            GlobalVariable.IDCARD_TYPE = account.getString("idcard_type");
            if (account.getString("is_verified_email").equals("1")){
                GlobalVariable.IS_VERIFIED_EMAIL = "true";
                userData.put("is_verified_email", "true");
            } else {
                GlobalVariable.IS_VERIFIED_EMAIL = "false";
                userData.put("is_verified_email", "false");
            }
            if (account.getString("is_verified_phone").equals("1")){
                GlobalVariable.IS_VERIFIED_PHONE = "true";
                userData.put("is_verified_phone", "true");
            } else {
                GlobalVariable.IS_VERIFIED_PHONE = "false";
                userData.put("is_verified_phone", "false");
            }
        }
        writeFile(GlobalVariable.MOBILESTATUS_FILE, mobileStatus.toString());
        writeFile(GlobalVariable.SECURITY_FILE, mobileSecurity.toString());
        writeFile(GlobalVariable.BALANCE_FILE, balanceData.toString());
        writeFile(GlobalVariable.USERDATA_FILE, userData.toString());

    }

    private Boolean checkFirstTimeApp() throws JSONException {
        String fileContent = readFile(GlobalVariable.MOBILESTATUS_FILE);
        JSONObject mobileStatus = new JSONObject(fileContent);
        Boolean first_time = true;

        GlobalVariable.FIRST_TIME_APP = mobileStatus.getString("first_time_app");

        if (GlobalVariable.FIRST_TIME_APP.equals("false")) {
            first_time = false;
        }
        return first_time;
    }
}
