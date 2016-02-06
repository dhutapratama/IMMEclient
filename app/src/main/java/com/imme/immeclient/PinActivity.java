package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
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
import java.text.NumberFormat;
import java.util.Locale;

public class PinActivity extends AppCompatActivity {

    String pin = "";
    Button button_0, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9;
    ImageButton button_backspace;
    View pin_1, pin_2, pin_3, pin_4;
    ProgressDialog loading;
    Boolean error_status = false;
    String error_message = null;
    Integer error_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        // Button Action
        button_0 = (Button) findViewById(R.id.button_0);
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);
        button_5 = (Button) findViewById(R.id.button_5);
        button_6 = (Button) findViewById(R.id.button_6);
        button_7 = (Button) findViewById(R.id.button_7);
        button_8 = (Button) findViewById(R.id.button_8);
        button_9 = (Button) findViewById(R.id.button_9);
        button_backspace = (ImageButton) findViewById(R.id.button_backspace);
        pin_1 = (View) findViewById(R.id.pin_1);
        pin_2 = (View) findViewById(R.id.pin_2);
        pin_3 = (View) findViewById(R.id.pin_3);
        pin_4 = (View) findViewById(R.id.pin_4);

        button_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("0");
            }
        });

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("1");
            }
        });

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("2");
            }
        });

        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("3");
            }
        });

        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("4");
            }
        });

        button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("5");
            }
        });

        button_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("6");
            }
        });

        button_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("7");
            }
        });

        button_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("8");
            }
        });

        button_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text("9");
            }
        });

        button_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin.length() < 1) {
                    pin = "";
                }else {
                    pin = pin.substring(0, pin.length()-1);
                }

                switch (pin.length()) {
                    case 0:
                        pin_1.setBackgroundColor(Color.WHITE);
                        break;
                    case 1:
                        pin_2.setBackgroundColor(Color.WHITE);
                        break;
                    case 2:
                        pin_3.setBackgroundColor(Color.WHITE);
                        break;
                    case 3:
                        pin_4.setBackgroundColor(Color.WHITE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void text(String string) {
        pin = pin + string;
        if (pin.length() == 4) {
            if (getIntent().getStringExtra("search_id") != null){
                new transfer().execute();
            } else {
                loading = ProgressDialog.show(PinActivity.this, "", "Validating transfer", false, true);
                new pin_check().execute();
            }
        }

        switch (pin.length()) {
            case 1:
                pin_1.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                pin_2.setBackgroundColor(Color.GREEN);
                break;
            case 3:
                pin_3.setBackgroundColor(Color.GREEN);
                break;
            case 4:
                pin_4.setBackgroundColor(Color.GREEN);
                break;
            default:
                break;
        }
    }

    private class pin_check extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                paySend();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (PinActivity.this.loading != null) {
                PinActivity.this.loading.dismiss();
            }
            if (error_status) {
                if (error_code == 113) {
                    finish();
                    Toast.makeText(PinActivity.this, error_message + " Please try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PinActivity.this, error_message, Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(PinActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            } else {
                finish();
                if (GlobalVariable.TRANSACTION_TYPE.equals("1"))
                {
                    Intent intentView = new Intent(getApplicationContext(), PersonalSend.class);
                    startActivity(intentView);
                } else {
                    Intent intentView = new Intent(getApplicationContext(), PaymentDetails.class);
                    startActivity(intentView);
                }
            }
        }
    }

    JSONObject serviceResult;
    public void paySend() throws JSONException, IOException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&apply_code=" + URLEncoder.encode(GlobalVariable.PAY_APPLY_CODE, "UTF-8")
                + "&pin_1=" + URLEncoder.encode(pin, "UTF-8");

        if (GlobalVariable.TRANSACTION_TYPE.equals("1")){
            serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "pay/send", postData);
        } else if (GlobalVariable.TRANSACTION_TYPE.equals("8")) {
            serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "pay/payment", postData);
        }

        if (serviceResult.getBoolean("error")){
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
        }
    }

    private class transfer extends AsyncTask<String, String, JSONObject> {
        protected JSONObject doInBackground(String... args) {
            JSONObject serviceResult = null;
            try {
                String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                        + "search_id=" + URLEncoder.encode(getIntent().getStringExtra("search_id"), "UTF-8")
                        + "amount=" + URLEncoder.encode(getIntent().getStringExtra("amount"), "UTF-8")
                        + "message=" + URLEncoder.encode(getIntent().getStringExtra("message"), "UTF-8")
                        + "pin_1=" + URLEncoder.encode(pin, "UTF-8");
                serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "pay/transfer", postData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return serviceResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(PinActivity.this, "", "Validating transfer", false, true);
        }

        protected void onPostExecute(JSONObject feedback_data) {
            loading.dismiss();
            if (feedback_data.length() == 0) {
                Toast.makeText(PinActivity.this, "Server issue, please contact 081235404833", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            try {
                if (feedback_data.getBoolean("error")) {
                    //feedback_data.getInt("code")

                    Toast.makeText(PinActivity.this, feedback_data.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    JSONObject data = feedback_data.getJSONObject("data");

                    GlobalVariable.PAY_RECIPIENT_NAME = getIntent().getStringExtra("name");
                    GlobalVariable.PAY_AMOUNT = getIntent().getStringExtra("amount");
                    GlobalVariable.MONEY_MAIN_BALANCE = data.getInt("balance");

                    Intent intentView = new Intent(getApplicationContext(), PersonalSend.class);
                    intentView.putExtra("picture_url", getIntent().getStringExtra("picture_url"));
                    startActivity(intentView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
