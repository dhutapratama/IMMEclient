package com.imme.immeclient;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.w3c.dom.Text;


public class ReceiveActivity extends AppCompatActivity {
    String money = "0";
    Button button_0, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9;
    ImageButton button_backspace;
    TextView balance;

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
 
        TextView text_receive_enter_amount = (TextView) findViewById(R.id.receive_enter_amount);
        text_receive_enter_amount.setTypeface(hnLight);

        TextView text_receive_rp = (TextView) findViewById(R.id.receive_rp);
        text_receive_rp.setTypeface(hnLight);

        TextView text_receive_balance_value = (TextView) findViewById(R.id.receive_balance_value);
        text_receive_balance_value.setTypeface(hbqLight);

        TextView text_receiver_list_text = (TextView) findViewById(R.id.receiver_list_text);
        text_receiver_list_text.setTypeface(hnLight);

        TextView text_receiver_continue = (TextView) findViewById(R.id.receiver_continue);
        text_receiver_continue.setTypeface(hnLight);
        // Close Font

        final Button button = (Button) findViewById(R.id.receiver_continue);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("com.imme.immeclient.ReceiveQRCodeActivity");
                startActivity(intent);
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

        balance = (TextView) findViewById(R.id.receive_balance_value);
        button_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "0";
                if (money.equals("00")) {
                    money = "0";
                } else {
                    balance.setText(money);
                }
            }
        });

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "1";
                if (money.equals("01")) {
                    money = "1";
                }
                balance.setText(money);
            }
        });

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "2";
                if (money.equals("02")) {
                    money = "2";
                }
                balance.setText(money);
            }
        });

        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "3";
                if (money.equals("03")) {
                    money = "3";
                }
                balance.setText(money);
            }
        });

        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "4";
                if (money.equals("04")) {
                    money = "4";
                }
                balance.setText(money);
            }
        });

        button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "5";
                if (money.equals("05")) {
                    money = "5";
                }
                balance.setText(money);
            }
        });

        button_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "6";
                if (money.equals("06")) {
                    money = "6";
                }
                balance.setText(money);
            }
        });

        button_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "7";
                if (money.equals("07")) {
                    money = "7";
                }
                balance.setText(money);
            }
        });

        button_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "8";
                if (money.equals("08")) {
                    money = "8";
                }
                balance.setText(money);
            }
        });

        button_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "9";
                if (money.equals("09")) {
                    money = "9";
                }
                balance.setText(money);
            }
        });

        button_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = money + "0";
                if (money.length() <= 2) {
                    money = "0";
                }else {
                    money = money.substring(0, money.length()-2);
                }
                balance.setText(money);
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
}
