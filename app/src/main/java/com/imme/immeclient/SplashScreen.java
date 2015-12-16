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
import java.net.URLEncoder;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    try {
                        initialProcedure();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (GlobalVariable.APP_LOGIN_STATUS.equals("true")) {
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

    public boolean initialProcedure() throws Exception {
        // Init to all variable
        initVariable();
        /*
        GlobalVariable.ANDROID_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        JSONObject loginStatus = new JSONObject();

        long unixTime = System.currentTimeMillis() / 1000L;
        Integer date = (int)(long) unixTime;

        SecurityOTP MCrypt = new SecurityOTP();
        String authentication_code = MCrypt.bytesToHex(MCrypt.encrypt(GlobalVariable.ACK_CODE));

        if (fileContent.equals("created")) {
            String requestData = "?request_code=1000"
                    + "&device_id=" + URLEncoder.encode(GlobalVariable.ANDROID_ID, "UTF-8")
                    + "&device_type=android"
                    + "&device_ip=" + URLEncoder.encode(GlobalVariable.CLIENT_IP(), "UTF-8")
                    + "&date=" + URLEncoder.encode(Integer.toString(date), "UTF-8")
                    + "&client_version=" + URLEncoder.encode( GlobalVariable.CLIENT_VERSION, "UTF-8")
                    + "&authentication_code=" + URLEncoder.encode( authentication_code, "UTF-8");

            serviceResult = WebServiceClient.getRequest(GlobalVariable.DISTRIBUTOR_SERVER + requestData);

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
         */
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

    private String readFile(String varname) throws JSONException {
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
            create_initial_file();
            ret = readFile(varname);
        } catch (IOException e) {
            Log.e("SplashScreen", "Can not read file: " + e.toString());
        }
        return ret;
    }

    private void create_initial_file() throws JSONException {
        JSONObject serverData = new JSONObject();
        JSONObject securityData = new JSONObject();
        JSONObject moneyData = new JSONObject();
        JSONObject customerData = new JSONObject();
        JSONObject appData = new JSONObject();

        // Server Data
        serverData.put("distributor_server", "http://api.studiwidie.com/");
        serverData.put("ack_code", "5GwcTzO0ODM6eSV3s66PJjeedlEvxWc9");
        serverData.put("otp_key", "OTP_KEY");

        // Security Data
        securityData.put("tba_algorithm", "TBA_ALGORITHM");
        securityData.put("cba_algorithm", "CBA_ALGORITHM");
        securityData.put("cba_counter", "CBA_COUNTER");
        securityData.put("session_key", "SESSION_KEY");
        securityData.put("csrf_token", "CSRF_TOKEN");
        securityData.put("user_agent","USER_AGENT");

        // Customer Data
        customerData.put("account_number", "ACCOUNT_NUMBER");
        customerData.put("full_name", "FULL_NAME");
        customerData.put("picture_url", "PICTURE_URL");
        customerData.put("email", "EMAIL");
        customerData.put("phone_number", "PHONE_NUMBER");
        customerData.put("idcard_number", "IDCARD_NUMBER");
        customerData.put("idcard_type", "IDCARD_TYPE");
        customerData.put("is_verified_email", "false");
        customerData.put("is_verified_phone", "false");

        // Money Data
        moneyData.put("main_balance", "0");
        moneyData.put("send_ammount", "0");
        moneyData.put("request_amount", "0");
        moneyData.put("transaction_code", "0");

        // App Data
        appData.put("first_time_app", "true");
        appData.put("login_status", "false");
        appData.put("client_version","1.0.0");

        writeFile(GlobalVariable.FILE_SERVER, serverData.toString());
        writeFile(GlobalVariable.FILE_SECURITY, securityData.toString());
        writeFile(GlobalVariable.FILE_CUSTOMER, customerData.toString());
        writeFile(GlobalVariable.FILE_MONEY, moneyData.toString());
        writeFile(GlobalVariable.FILE_APP, appData.toString());
    }

    private void initVariable() throws JSONException {
        String securityContent = readFile(GlobalVariable.FILE_SECURITY);
        JSONObject securityData = new JSONObject(securityContent);

        String customerContent = readFile(GlobalVariable.FILE_CUSTOMER);
        JSONObject customerData = new JSONObject(customerContent);

        String moneyContent = readFile(GlobalVariable.FILE_MONEY);
        JSONObject moneyData = new JSONObject(moneyContent);

        String appContent = readFile(GlobalVariable.FILE_APP);
        JSONObject appData = new JSONObject(appContent);

        // Security Data
        GlobalVariable.SECURITY_IMME_ALGORITHM = securityData.getString("imme_algorithm");
        GlobalVariable.SECURITY_TBA_ALGORITHM = securityData.getString("tba_algorithm");
        GlobalVariable.SECURITY_CBA_ALGORITHM = securityData.getString("cba_algorithm");
        GlobalVariable.SECURITY_CBA_COUNTER = securityData.getString("cba_counter");
        GlobalVariable.SECURITY_SESSION_KEY = securityData.getString("session_key");
        GlobalVariable.SECURITY_CSRF_TOKEN = securityData.getString("csrf_token");
        GlobalVariable.SECURITY_USER_AGENT = securityData.getString("user_agent");

        // Customer Data
        GlobalVariable.CUSTOMER_ACCOUNT_NUMBER = customerData.getString("account_number");
        GlobalVariable.CUSTOMER_FULL_NAME = customerData.getString("full_name");
        GlobalVariable.CUSTOMER_PICTURE_URL = customerData.getString("picture_url");
        GlobalVariable.CUSTOMER_EMAIL = customerData.getString("email");
        GlobalVariable.CUSTOMER_PHONE_NUMBER = customerData.getString("phone_number");
        GlobalVariable.CUSTOMER_IDCARD_NUMBER = customerData.getString("idcard_number");
        GlobalVariable.CUSTOMER_IDCARD_TYPE = customerData.getString("idcard_type");
        GlobalVariable.CUSTOMER_IS_VERIFIED_EMAIL = customerData.getString("is_verified_email");
        GlobalVariable.CUSTOMER_IS_VERIFIED_PHONE = customerData.getString("is_verified_phone");

        // Money Data
        GlobalVariable.MONEY_MAIN_BALANCE = Integer.parseInt(moneyData.getString("main_balance"));
        GlobalVariable.MONEY_SEND_AMOUNT = Integer.parseInt(moneyData.getString("send_ammount"));
        GlobalVariable.MONEY_REQUEST_AMOUNT = Integer.parseInt(moneyData.getString("request_amount"));
        GlobalVariable.MONEY_TRANSACTION_CODE = "TRANSACTION_CODE";

        // Money Data
        GlobalVariable.APP_FIRST_TIME_APP = appData.getString("first_time_app");
        GlobalVariable.APP_LOGIN_STATUS = appData.getString("login_status");
        GlobalVariable.APP_CLIENT_VERSION = appData.getString("client_version");
    }
}
