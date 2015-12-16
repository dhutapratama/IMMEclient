package com.imme.immeclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class PersonalSend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView personal_name = (TextView) findViewById(R.id.personal_name);
        TextView personal_balance = (TextView) findViewById(R.id.personal_balance);

        personal_name.setText(GlobalVariable.PAY_RECIPIENT_NAME);
        personal_balance.setText(GlobalVariable.PAY_AMOUNT);
    }

}
