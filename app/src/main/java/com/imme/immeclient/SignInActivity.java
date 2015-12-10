package com.imme.immeclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        TextView sign_in_textview_sign_up = (TextView) findViewById(R.id.sign_in_textview_sign_up);
        sign_in_textview_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.imme.immeclient.SignUpActivity");
                startActivity(intent);
            }
        });

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
        // Sign in button action
        sign_in_button_sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    email = sign_in_edittext_email.getText().toString();
                    password = sign_in_edittext_password.getText().toString();
                    doLogin();
                } catch (JSONException e) {
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
        } else {
            Toast.makeText(this, serviceResult.getString("login_message"), Toast.LENGTH_LONG).show();
            mobileStatus.put("first_time_app", "false");
            mobileStatus.put("login_status", "true");
            mobileStatus.put("csrf_token", serviceResult.getString("csrf_token"));
            mobileStatus.put("session_key", serviceResult.getString("session_key"));
            mobileStatus.put("imme_algorithm", serviceResult.getString("imme_algorithm"));
            mobileStatus.put("tba_algorithm", serviceResult.getString("tba_algorithm"));
            mobileStatus.put("cba_algorithm", serviceResult.getString("cba_algorithm"));
            mobileStatus.put("cba_counter", serviceResult.getString("cba_counter"));
            GlobalVariable.CSRF_TOKEN = serviceResult.getString("csrf_token");
            GlobalVariable.CSRF_TOKEN = serviceResult.getString("csrf_token");
           GlobalVariable.LOGIN_STATUS = "true";
        }
        writeFile(GlobalVariable.MOBILESTATUS_FILE, mobileStatus.toString());
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
