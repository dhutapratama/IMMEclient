package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private String email = new String();
    private String password = new String();
    private ProgressDialog loading = null;
    Boolean login_error = false;
    private String message = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (GlobalVariable.APP_FIRST_TIME_APP.equals("true")) {
            // Tutorial 3 Halaman
            startActivity(new Intent(SignInActivity.this, WelcomeScreen.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#ff0f99da"));

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

        //final TextView dont_have_account = (TextView) findViewById(R.id.dont_have_account);
        //dont_have_account.setTypeface(hbqLight);

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
            email = sign_in_edittext_email.getText().toString();
            password = sign_in_edittext_password.getText().toString();
            loading = ProgressDialog.show(SignInActivity.this, "", "Sign in...", true, true);
            new loginTask().execute();

            if (GlobalVariable.APP_LOGIN_STATUS.equals("true")) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                //Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
            }

            }
        });
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
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
        } catch (IOException e) {
            Log.e("SplashScreen", "Can not read file: " + e.toString());
        }
        return ret;
    }

    public void doLogin() throws JSONException, IOException {
        String postData ="email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");
        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "login", postData);

        if (!serviceResult.getBoolean("error")){
            // SECURITY GlobalVariable SET
            // Security
            GlobalVariable.SECURITY_IMME_ALGORITHM = serviceResult.getString("imme_algorithm");
            GlobalVariable.SECURITY_TBA_ALGORITHM = serviceResult.getString("tba_algorithm");
            GlobalVariable.SECURITY_CBA_ALGORITHM = serviceResult.getString("cba_algorithm");
            GlobalVariable.SECURITY_CBA_COUNTER = serviceResult.getString("cba_counter");
            GlobalVariable.SECURITY_SESSION_KEY = serviceResult.getString("session_key");

            // Customer
            JSONObject customerData = serviceResult.getJSONObject("account");
            GlobalVariable.CUSTOMER_ACCOUNT_NUMBER  = customerData.getString("account_number");
            GlobalVariable.CUSTOMER_FULL_NAME       = customerData.getString("full_name");
            GlobalVariable.CUSTOMER_PICTURE_URL     = customerData.getString("picture_url");
            GlobalVariable.CUSTOMER_EMAIL           = customerData.getString("email");
            GlobalVariable.CUSTOMER_PHONE_NUMBER    = customerData.getString("phone_number");
            GlobalVariable.CUSTOMER_IDCARD_NUMBER   = customerData.getString("idcard_number");
            GlobalVariable.CUSTOMER_IDCARD_TYPE     = customerData.getString("idcard_type");
            if (customerData.getString("is_verified_email").equals("1")){
                GlobalVariable.CUSTOMER_IS_VERIFIED_EMAIL = "true";
            } else {
                GlobalVariable.CUSTOMER_IS_VERIFIED_EMAIL = "false";
            }
            if (customerData.getString("is_verified_phone").equals("1")){
                GlobalVariable.CUSTOMER_IS_VERIFIED_PHONE = "true";
            } else {
                GlobalVariable.CUSTOMER_IS_VERIFIED_PHONE = "false";
            }

            // App
            GlobalVariable.APP_FIRST_TIME_APP = "false";
            GlobalVariable.APP_LOGIN_STATUS = "true";

            // Money
            GlobalVariable.MONEY_MAIN_BALANCE = Integer.parseInt(customerData.getString("balance"));

            commit();
        } else {
            login_error = true;
            message = serviceResult.getString("message");
        }
    }

    private void commit() throws JSONException {
        String securityContent = readFile(GlobalVariable.FILE_SECURITY);
        String customerContent = readFile(GlobalVariable.FILE_CUSTOMER);
        String moneyContent = readFile(GlobalVariable.FILE_MONEY);
        String appContent = readFile(GlobalVariable.FILE_APP);

        JSONObject securityData = new JSONObject(securityContent);
        JSONObject moneyData = new JSONObject(moneyContent);
        JSONObject customerData = new JSONObject(customerContent);
        JSONObject appData = new JSONObject(appContent);

        // Security Data
        securityData.put("imme_algorithm", GlobalVariable.SECURITY_IMME_ALGORITHM);
        securityData.put("tba_algorithm", GlobalVariable.SECURITY_TBA_ALGORITHM);
        securityData.put("cba_algorithm", GlobalVariable.SECURITY_CBA_ALGORITHM);
        securityData.put("cba_counter", GlobalVariable.SECURITY_CBA_COUNTER);
        securityData.put("session_key", GlobalVariable.SECURITY_SESSION_KEY);

        // Customer Data
        customerData.put("account_number", GlobalVariable.CUSTOMER_ACCOUNT_NUMBER);
        customerData.put("full_name", GlobalVariable.CUSTOMER_FULL_NAME);
        customerData.put("picture_url", GlobalVariable.CUSTOMER_PICTURE_URL);
        customerData.put("email", GlobalVariable.CUSTOMER_EMAIL);
        customerData.put("phone_number", GlobalVariable.CUSTOMER_PHONE_NUMBER);
        customerData.put("idcard_number", GlobalVariable.CUSTOMER_IDCARD_NUMBER);
        customerData.put("idcard_type", GlobalVariable.CUSTOMER_IDCARD_TYPE);
        customerData.put("is_verified_email", GlobalVariable.CUSTOMER_IS_VERIFIED_EMAIL);
        customerData.put("is_verified_phone", GlobalVariable.CUSTOMER_IS_VERIFIED_PHONE);

        // Money Data
        moneyData.put("main_balance", Integer.toString(GlobalVariable.MONEY_MAIN_BALANCE));

        // App Data
        appData.put("first_time_app", GlobalVariable.APP_FIRST_TIME_APP);
        appData.put("login_status", GlobalVariable.APP_LOGIN_STATUS);
        writeFile(GlobalVariable.FILE_SECURITY, securityData.toString());
        writeFile(GlobalVariable.FILE_CUSTOMER, customerData.toString());
        writeFile(GlobalVariable.FILE_MONEY, moneyData.toString());
        writeFile(GlobalVariable.FILE_APP, appData.toString());
    }

    private class loginTask extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            Log.i("MyApp", "Background thread starting");
            try {
                doLogin();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (SignInActivity.this.loading != null) {
                SignInActivity.this.loading.dismiss();
            }
            if (login_error) {
                Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
