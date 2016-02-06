package com.imme.immeclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.NumberFormat;
import java.util.Locale;

public class Deposit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        TextView deposit_amount = (TextView) findViewById(R.id.voucher_amount);
        TextView deposit_balance = (TextView) findViewById(R.id.deposit_main_balance);

        String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.DEPOSIT_AMOUNT);
        deposit_amount.setText("Rp " + formated_money);

        formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.MONEY_MAIN_BALANCE);
        deposit_balance.setText(formated_money);
    }

}
