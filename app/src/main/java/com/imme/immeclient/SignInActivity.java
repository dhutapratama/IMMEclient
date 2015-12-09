package com.imme.immeclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // Read Log Status
        String fileContent = readFile(GlobalVariable.MOBILESTATUS_FILE);

        if (fileContent.isEmpty() || fileContent.equals("created")) {
            Toast.makeText(this, "Fresh Installing IMME" + fileContent, Toast.LENGTH_LONG).show();
        } else {
            try {
                JSONObject mobileStatus = new JSONObject(fileContent);
                Toast.makeText(this, mobileStatus.getString("csrf_token"),Toast.LENGTH_LONG).show();
                Log.e("SignInActivity", fileContent);
            } catch (JSONException e) {
                Log.e("SignInActivity", "Error Extraction : " + e.toString());
                //e.printStackTrace();
            }
        }

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        EditText sign_in_edittext_email = (EditText) findViewById(R.id.sign_in_edittext_email);
        sign_in_edittext_email.setTypeface(hnLight);

        EditText sign_in_edittext_password = (EditText) findViewById(R.id.sign_in_edittext_password);
        sign_in_edittext_password.setTypeface(hnLight);

        TextView sign_in_button_sign_in = (TextView) findViewById(R.id.sign_in_button_sign_in);
        sign_in_button_sign_in.setTypeface(hbqLight);
    }


    public void writeFile(String varname, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(varname + ".json", Context.MODE_PRIVATE));
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
            InputStream inputStream = openFileInput(varname + ".json");

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
