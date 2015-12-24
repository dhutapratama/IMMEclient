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
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Locale;

public class PersonalSend extends AppCompatActivity {
    ProgressDialog loading;
    Boolean error_status = false;
    String error_message = null, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        TextView personal_name = (TextView) findViewById(R.id.personal_name);
        TextView personal_balance = (TextView) findViewById(R.id.personal_balance);
        TextView main_balance = (TextView) findViewById(R.id.send_main_balance);

        personal_name.setText(GlobalVariable.PAY_RECIPIENT_NAME);
        String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(Integer.parseInt(GlobalVariable.PAY_AMOUNT));
        personal_balance.setText("Rp " + formated_money);
        formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.MONEY_MAIN_BALANCE);
        main_balance.setText(formated_money);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent = new Intent(PersonalSend.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // API Running
        Intent intent = new Intent(PersonalSend.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
