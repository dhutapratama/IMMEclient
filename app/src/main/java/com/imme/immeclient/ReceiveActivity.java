package com.imme.immeclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.NumberFormat;
import java.util.Locale;


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
                if (money.length() <= 1) {
                    money = "0";
                }else {
                    money = money.substring(0, money.length()-1);
                }

                String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(Integer.parseInt(money));
                balance.setText(formated_money);
                balance.setLayoutParams(new TableRow.LayoutParams(
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
        money = money + string;
        if (money.equals("0" + string)) {
            money = string;
        } else if (money.length() > 8) {
            money = money.substring(0, money.length()-1);
        }

        String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(Integer.parseInt(money));
        balance.setText(formated_money);
    }
}
