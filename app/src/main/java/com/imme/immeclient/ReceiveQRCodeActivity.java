package com.imme.immeclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
import java.text.NumberFormat;
import java.util.Locale;

public class ReceiveQRCodeActivity extends AppCompatActivity {
    String message = new String();
    Boolean checkSender_error = false;
    Boolean checkTransfer_error = false;
    ImageView qrcode = null;
    TextView time_out= null;
    TextView transfer_notification = null;
    TextView sender_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_qrcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        // Create Barcode
        Bitmap encoded_qr = encodeToQrCode(GlobalVariable.MONEY_TRANSACTION_CODE, 500, 500);
        qrcode = (ImageView) findViewById(R.id.qr_code);
        qrcode.setImageBitmap(encoded_qr);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.content_main, null); //log.xml is your file.
        TextView main_balance_value = (TextView)vi.findViewById(R.id.main_textview_balance_value); //get a reference to the textview on the log.xml file.
        String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.MONEY_REQUEST_AMOUNT);
        main_balance_value.setText(formated_money);

        time_out = (TextView) findViewById(R.id.time_out);
        transfer_notification = (TextView) findViewById(R.id.transfer_notificetion);
        sender_name = (TextView) findViewById(R.id.sender_name);

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                time_out.setText("Scan it! " + millisUntilFinished / 1000L + " Seconds");
            }

            public void onFinish() {
                time_out.setText("Your receive session has expired");
                qrcode.setVisibility(View.GONE);
            }
        }.start();

        new check_sender().execute();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReceiveQRCodeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public static Bitmap encodeToQrCode(String text, int width, int height){
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private void writeToFile(String varname, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(varname + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String varname) {

        String ret = "";

        try {
            InputStream inputStream = openFileInput(varname + ".txt");

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
            Log.e("MainActivity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("MainActivity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private class check_sender extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                checkSender();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (checkSender_error) {
                Toast.makeText(ReceiveQRCodeActivity.this, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReceiveQRCodeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                qrcode.setVisibility(View.GONE);
                time_out.setVisibility(View.GONE);
                transfer_notification.setVisibility(View.VISIBLE);
                sender_name.setVisibility(View.VISIBLE);
                sender_name.setText(GlobalVariable.RECEIVE_SENDER_NAME);
                new check_transfered().execute();
            }
        }
    }

    private class check_transfered extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                checkTransfered();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (checkTransfer_error) {
                Toast.makeText(ReceiveQRCodeActivity.this, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReceiveQRCodeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                transfer_notification.setText("Transaction Success");
                sender_name.setText("Your balance :" + GlobalVariable.MONEY_MAIN_BALANCE);
            }
        }
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

    public void checkTransfered() throws JSONException, IOException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&transaction_code=" + URLEncoder.encode(GlobalVariable.MONEY_TRANSACTION_CODE, "UTF-8");
        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "receive/check_transfered", postData);

        if (!serviceResult.getBoolean("error")){
            // Variable
            GlobalVariable.MONEY_MAIN_BALANCE = Integer.parseInt(serviceResult.getString("balance"));
            commit();
        } else {
            checkTransfer_error = true;
            message = serviceResult.getString("message");
        }
    }

    public void checkSender() throws JSONException, IOException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&transaction_code=" + URLEncoder.encode(GlobalVariable.MONEY_TRANSACTION_CODE, "UTF-8");
        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "receive/check_sender", postData);

        if (!serviceResult.getBoolean("error")){
            // Variable
            GlobalVariable.RECEIVE_SENDER_NAME = serviceResult.getString("sender_name");
            GlobalVariable.RECEIVE_SENDER_PICTURE = serviceResult.getString("sender_picture");
        } else {
            checkSender_error = true;
            message = serviceResult.getString("message");
        }
    }

    private void commit() throws JSONException {
        String moneyContent = readFile(GlobalVariable.FILE_MONEY);
        JSONObject moneyData = new JSONObject(moneyContent);

        // Money Data
        moneyData.put("main_balance", Integer.toString(GlobalVariable.MONEY_MAIN_BALANCE));

        writeFile(GlobalVariable.FILE_MONEY, moneyData.toString());
    }
}
