package com.imme.immeclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Locale;


public class ReceiveActivity extends AppCompatActivity {
    ImageButton button_backspace;
    String requestAmount = "0";
    TextView text_receive_balance_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");
        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        Button button_0 = (Button) findViewById(R.id.button_0);
        button_0.setTypeface(hnLight);

        Button button_1 = (Button) findViewById(R.id.button_1);
        button_1.setTypeface(hnLight);

        Button button_2 = (Button) findViewById(R.id.button_2);
        button_2.setTypeface(hnLight);

        Button button_3 = (Button) findViewById(R.id.button_3);
        button_3.setTypeface(hnLight);

        Button button_4 = (Button) findViewById(R.id.button_4);
        button_4.setTypeface(hnLight);

        Button button_5 = (Button) findViewById(R.id.button_5);
        button_5.setTypeface(hnLight);

        Button button_6 = (Button) findViewById(R.id.button_6);
        button_6.setTypeface(hnLight);

        Button button_7 = (Button) findViewById(R.id.button_7);
        button_7.setTypeface(hnLight);

        Button button_8 = (Button) findViewById(R.id.button_8);
        button_8.setTypeface(hnLight);

        Button button_9 = (Button) findViewById(R.id.button_9);
        button_9.setTypeface(hnLight);

        TextView text_receive_enter_amount = (TextView) findViewById(R.id.receive_enter_amount);
        text_receive_enter_amount.setTypeface(hnLight);

        TextView text_receive_rp = (TextView) findViewById(R.id.receive_rp);
        text_receive_rp.setTypeface(hnLight);

        text_receive_balance_value = (TextView) findViewById(R.id.receive_balance_value);
        text_receive_balance_value.setTypeface(hbqLight);

        TextView text_receiver_list_text = (TextView) findViewById(R.id.receiver_list_text);
        text_receiver_list_text.setTypeface(hnLight);

        Button text_receiver_continue = (Button) findViewById(R.id.receiver_continue);
        text_receiver_continue.setTypeface(hnLight);
        // Close Font

        final Button button = (Button) findViewById(R.id.receiver_continue);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String postData = null;

                try {
                    long unixTime = System.currentTimeMillis() / 1000L;
                    Integer answer = (int)(long) unixTime;
                    postData = "&session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                            + "&amount=" + URLEncoder.encode(String.valueOf(GlobalVariable.MONEY_REQUEST_AMOUNT), "UTF-8");

                    JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "receive", postData);

                    if (serviceResult.getString("error").equals("true")) {
                        Toast.makeText(ReceiveActivity.this, serviceResult.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        GlobalVariable.MONEY_TRANSACTION_CODE = serviceResult.getString("transaction_code");
                        Intent intent = new Intent("com.imme.immeclient.ReceiveQRCodeActivity");
                        startActivity(intent);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
            if (requestAmount.length() <= 1) {
                requestAmount = "0";
            }else {
                requestAmount = requestAmount.substring(0, requestAmount.length()-1);
            }

            GlobalVariable.MONEY_REQUEST_AMOUNT = Integer.parseInt(requestAmount);
            String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.MONEY_REQUEST_AMOUNT);
            text_receive_balance_value.setText(formated_money);
            text_receive_balance_value.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
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
        requestAmount = requestAmount + string;
        if (requestAmount.equals("0" + string)) {
            requestAmount = string;
        } else if (requestAmount.length() > 8) {
            requestAmount = requestAmount.substring(0, requestAmount.length()-1);
        }

        GlobalVariable.MONEY_REQUEST_AMOUNT = Integer.parseInt(requestAmount);
        String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.MONEY_REQUEST_AMOUNT);
        text_receive_balance_value.setText(formated_money);
    }
}
