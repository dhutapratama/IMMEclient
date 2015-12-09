package com.imme.immeclient;

/**
 * Created by lasedev on 11/11/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

import com.imme.immeclient.GlobalVariable;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Call Initial procedure
        initialProcedure();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if (GlobalVariable.IS_LOGIN) {
                        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreen.this,SignInActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    public void initialProcedure() {
        // Read Log Status
        String fileContent = readFile(GlobalVariable.MOBILESTATUS_FILE);

        if (fileContent.isEmpty() || fileContent.equals("created")) {
            try {
                JSONObject serviceResult = WebServiceClient.requestWebService(GlobalVariable.DISTRIBUTOR_SERVER
                        + GlobalVariable.ACK);
                try {
                    JSONObject loginStatus = new JSONObject();
                    loginStatus.put("login_status", "false");
                    loginStatus.put("user_agent", serviceResult.getString("user_agent"));
                    loginStatus.put("csrf_token", serviceResult.getString("csrf_token"));

                    writeFile(GlobalVariable.MOBILESTATUS_FILE, loginStatus.toString());
                    Log.e("SplashScreen", "Write file: " + loginStatus.toString());

                    Toast.makeText(this, "Fresh Installing IMME", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException ioex) {
                Toast.makeText(this, ioex.getStackTrace().toString() + "\r\n" + ioex.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                JSONObject mobileStatus = new JSONObject(fileContent);

                Toast.makeText(this, mobileStatus.getString("csrf_token"),Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
