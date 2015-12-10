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

public class SignInActivity extends AppCompatActivity {
    public String email = new String();
    public String password = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // Read Log Status
        String fileContent = readFile(GlobalVariable.MOBILESTATUS_FILE);
        try {
            JSONObject mobileStatus = new JSONObject(fileContent);
            GlobalVariable.CSRF_TOKEN = mobileStatus.getString("csrf_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Sign in button action
        sign_in_button_sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email = sign_in_edittext_email.getText().toString();
                password = sign_in_edittext_password.getText().toString();
                doLogin();
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

    public void doLogin() {
        String request_code = "1002";
        String csrf_token = "";
        String device_ip = "";
        JSONObject mobileStatus = new JSONObject();

        String file_data = readFile(GlobalVariable.MOBILESTATUS_FILE);
        Log.v("Read File Login", file_data);
        csrf_token = GlobalVariable.CSRF_TOKEN;
        //Toast.makeText(this, file_data, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "CSRF : " + csrf_token, Toast.LENGTH_LONG).show();

        String query = "request_code=" + request_code
                + "&csrf_token=" + csrf_token
                + "&email=" + email
                + "&password=" + password
                + "&device_ip=" + device_ip;
        Log.v("QUERY : ", query);
        try {
            JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER, query);
            try {
                mobileStatus = new JSONObject(file_data);
                mobileStatus.put("csrf_token", serviceResult.getString("csrf_token"));
                //GlobalVariable.CSRF_TOKEN = serviceResult.getString("csrf_token");

                if (serviceResult.getBoolean("error"))
                {
                    Log.v("Error" + serviceResult.getString("code"), serviceResult.getString("message"));
                } else {
                    Log.v("Login Success", serviceResult.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            writeFile(GlobalVariable.MOBILESTATUS_FILE, mobileStatus.toString());
            Log.v("Write File", mobileStatus.toString());
            //Toast.makeText(this, "query : " + query /*serviceResult.getString("csrf_token")*/, Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Result : " + serviceResult.toString(), Toast.LENGTH_LONG).show();
            Log.v("Post Result", serviceResult.toString());
        } catch (IOException ioex) {
            Toast.makeText(this, ioex.getStackTrace().toString() + "\r\n" + ioex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
