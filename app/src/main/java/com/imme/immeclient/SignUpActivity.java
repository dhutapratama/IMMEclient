package com.imme.immeclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.net.URLEncoder;

public class SignUpActivity extends AppCompatActivity {

    String fullname, email, password, confirmp, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#ff0f99da"));

        TextView sign_up_button_sign_up = (TextView) findViewById(R.id.sign_up_button_sign_in);
        sign_up_button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.imme.immeclient.SignInActivity");
                startActivity(intent);
            }
        });

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        final EditText sign_up_edittext_full_name = (EditText) findViewById(R.id.sign_up_edittext_full_name);
        sign_up_edittext_full_name.setTypeface(hnLight);

        final EditText sign_up_edittext_email = (EditText) findViewById(R.id.sign_up_edittext_email);
        sign_up_edittext_email.setTypeface(hnLight);

        final EditText sign_up_edittext_password = (EditText) findViewById(R.id.sign_up_edittext_password);
        sign_up_edittext_password.setTypeface(hnLight);

        final EditText sign_up_edittext_confirm_password = (EditText) findViewById(R.id.sign_up_edittext_confirm_password);
        sign_up_edittext_confirm_password.setTypeface(hnLight);

        final TextView sign_up_edittext_phone_number = (EditText) findViewById(R.id.sign_up_edittext_phone_number);
        sign_up_edittext_phone_number.setTypeface(hnLight);
        sign_up_button_sign_up.setTypeface(hbqLight);

        sign_up_button_sign_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Boolean signup_success = false;
                try {
                    fullname = sign_up_edittext_full_name.getText().toString();
                    email = sign_up_edittext_email.getText().toString();
                    password = sign_up_edittext_password.getText().toString();
                    confirmp = sign_up_edittext_confirm_password.getText().toString();
                    phone = sign_up_edittext_phone_number.getText().toString();
                    signup_success = doSignup();
                } catch (JSONException e) {
                    Log.e("JSONException", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("IOException", e.toString());
                    e.printStackTrace();
                }

                if (signup_success) {
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private Boolean doSignup() throws JSONException, IOException {
        String request_code = "1001";

        String postData = "request_code=" + request_code
                + "&csrf_token=" + URLEncoder.encode(GlobalVariable.CSRF_TOKEN, "UTF-8")
                + "&fullname=" + URLEncoder.encode(fullname, "UTF-8")
                + "&email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&confirmp=" + URLEncoder.encode(confirmp, "UTF-8")
                + "&phone=" + URLEncoder.encode(phone, "UTF-8");

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER, postData);

        String fileContent = readFile(GlobalVariable.MOBILESTATUS_FILE);
        JSONObject mobileStatus = new JSONObject(fileContent);
        GlobalVariable.CSRF_TOKEN = mobileStatus.getString("csrf_token");

        Boolean signupstatus = false;
        if (serviceResult.getBoolean("error")){
            signupstatus = false;
            Toast.makeText(this, serviceResult.getString("message"), Toast.LENGTH_LONG).show();
        } else {
            signupstatus = true;
            Toast.makeText(this, serviceResult.getString("registration_message"), Toast.LENGTH_LONG).show();
        }
        writeFile(GlobalVariable.MOBILESTATUS_FILE, mobileStatus.toString());

        return signupstatus;
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

}
