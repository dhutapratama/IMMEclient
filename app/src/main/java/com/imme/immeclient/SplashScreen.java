package com.imme.immeclient;

/**
 * Created by lasedev on 11/11/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Call Initial procedure
        try {
            initialProcedure();
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IOException", e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(this, "Error JSON Format", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show();
            Log.e("JSONException", e.toString());
            e.printStackTrace();

            //moveTaskToBack(true);
            //android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(1);
        }

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if (GlobalVariable.LOGIN_STATUS.equals("true")) {
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

    public boolean initialProcedure() throws IOException, JSONException {
        //Initial Variable
        JSONObject serviceResult;

        // Check File
        String fileContent = readFile(GlobalVariable.MOBILESTATUS_FILE);
        //Log.v("Read File",fileContent);
        GlobalVariable.ANDROID_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        JSONObject loginStatus = new JSONObject();
        //Log.v("Status","No file / Fresh installing");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        String date = formatter.format(today);


        if (fileContent.equals("created")) {
            GlobalVariable.ACK = "?request_code=1000"
                    + "&device_id=" + URLEncoder.encode(GlobalVariable.ANDROID_ID, "UTF-8")
                    + "&device_type=android"
                    + "&device_ip=" + URLEncoder.encode(GlobalVariable.CLIENT_IP(), "UTF-8")
                    + "&date=" + "2015-12-07+00%3A00%3A00" //URLEncoder.encode(date, "UTF-8")
                    + "&client_version=" + URLEncoder.encode( GlobalVariable.CLIENT_VERSION, "UTF-8")
                    + "&authentication_code=0sgsOhUwJ9dSLDZ78ztEG4LclvIdIOMjlLwbw9QjD4g%3D";

            serviceResult = WebServiceClient.getRequest(GlobalVariable.DISTRIBUTOR_SERVER + GlobalVariable.ACK);

            //Toast.makeText(this, serviceResult.toString(), Toast.LENGTH_LONG).show();
            loginStatus.put("first_time_app", "true");
            loginStatus.put("login_status", "false");
            loginStatus.put("user_agent", serviceResult.getString("user_agent"));
            loginStatus.put("csrf_token", serviceResult.getString("csrf_token"));
            loginStatus.put("android_id", GlobalVariable.ANDROID_ID);
            GlobalVariable.USER_AGENT = serviceResult.getString("user_agent");
            GlobalVariable.CSRF_TOKEN = serviceResult.getString("csrf_token");

            writeFile(GlobalVariable.MOBILESTATUS_FILE, loginStatus.toString());

            if (serviceResult.getBoolean("error")){
                Toast.makeText(this, serviceResult.getString("message"), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, serviceResult.getString("hello_message"), Toast.LENGTH_LONG).show();
            }
            //Log.v("Write File", loginStatus.toString());
        } else {
            loginStatus = new JSONObject(fileContent);
            GlobalVariable.LOGIN_STATUS = loginStatus.getString("login_status");
            if (GlobalVariable.LOGIN_STATUS.equals("true")) {
                initVariable();
            }
        }
        return true;
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
            //Log.e("SplashScreen", "File not found: " + e.toString());
                writeFile(varname, "created");
                ret = "created";
            //Log.v("SplashScreen", "File Created");
        } catch (IOException e) {
            Log.e("SplashScreen", "Can not read file: " + e.toString());
        }
        //Log.v("SplashScreen", ret);
        return ret;
    }

    private void initVariable() throws JSONException {
        String securityContent = readFile(GlobalVariable.SECURITY_FILE);

        JSONObject securityData = new JSONObject(securityContent);

        String accountContent = readFile(GlobalVariable.USERDATA_FILE);
        JSONObject accountData = new JSONObject(accountContent);

        String balanceContent = readFile(GlobalVariable.BALANCE_FILE);
        JSONObject balanceData = new JSONObject(balanceContent);

        GlobalVariable.TBA_ALGORITHM = securityData.getString("tba_algorithm");
        GlobalVariable.CBA_ALGORITHM = securityData.getString("cba_algorithm");
        GlobalVariable.CBA_COUNTER = securityData.getString("cba_counter");
        GlobalVariable.SESSION_KEY = securityData.getString("session_key");

        Toast.makeText(this, GlobalVariable.CBA_COUNTER, Toast.LENGTH_LONG).show();

        GlobalVariable.MAIN_BALANCE = Integer.parseInt(balanceData.getString("main_balance"));
        GlobalVariable.GIFT_BALANCE = Integer.parseInt(balanceData.getString("gift_balance"));

        GlobalVariable.ACCOUNT_NUMBER = accountData.getString("account_number");
        GlobalVariable.FULL_NAME = accountData.getString("full_name");
        GlobalVariable.PICTURE_URL = accountData.getString("picture_url");
        GlobalVariable.EMAIL = accountData.getString("email");
        GlobalVariable.PHONE_NUMBER = accountData.getString("phone_number");
        GlobalVariable.IDCARD_NUMBER = accountData.getString("idcard_number");
        GlobalVariable.IDCARD_TYPE = accountData.getString("idcard_type");
        GlobalVariable.IS_VERIFIED_EMAIL = accountData.getString("is_verified_email");
        GlobalVariable.IS_VERIFIED_PHONE = accountData.getString("is_verified_phone");
    }
}
