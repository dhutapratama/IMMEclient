package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class ChangePin2Process3Activity extends AppCompatActivity {

    String pin = "";
    Button button_0, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9;
    ImageButton button_backspace;
    View pin_1, pin_2, pin_3, pin_4, pin_5, pin_6;
    ProgressDialog loading;
    Boolean error_status = false;
    String error_message, message;
    Integer error_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin2_process3);
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
        pin_5 = (View) findViewById(R.id.pin_5);
        pin_6 = (View) findViewById(R.id.pin_6);


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
                    case 4:
                        pin_5.setBackgroundColor(Color.WHITE);
                        break;
                    case 5:
                        pin_6.setBackgroundColor(Color.WHITE);
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
                Intent intent = new Intent(ChangePin2Process3Activity.this, SecuritySettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void text(String string) {
        pin = pin + string;
        if (pin.length() == 6) {
            GlobalVariable.CHANGE_PIN_2_PIN_2 = pin;
            loading = ProgressDialog.show(ChangePin2Process3Activity.this, "", "Changing PIN 2", false);
            new change_pin().execute();
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
            case 5:
                pin_5.setBackgroundColor(Color.GREEN);
                break;
            case 6:
                pin_6.setBackgroundColor(Color.GREEN);
                break;
            default:
                break;
        }
    }

    private class change_pin extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                changePin();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (ChangePin2Process3Activity.this.loading != null) {
                ChangePin2Process3Activity.this.loading.dismiss();
            }
            if (error_status) {
                Toast.makeText(ChangePin2Process3Activity.this, error_message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChangePin2Process3Activity.this, SecuritySettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(ChangePin2Process3Activity.this, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChangePin2Process3Activity.this, SecuritySettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    public void changePin() throws JSONException, IOException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&pin_2_new=" + URLEncoder.encode(GlobalVariable.CHANGE_PIN_2_NEW, "UTF-8")
                + "&pin_2=" + URLEncoder.encode(GlobalVariable.CHANGE_PIN_2_PIN_2, "UTF-8");

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "setting/pin_2", postData);
        if (serviceResult.getBoolean("error")){
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
            message = serviceResult.getString("message");
        }
    }
}