package com.imme.immeclient;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class TransactionHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#FF249962"));

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        TextView transaction_history_send = (TextView) findViewById(R.id.transaction_history_send);
        transaction_history_send.setTypeface(hnLight);

        TextView transaction_history_date = (TextView) findViewById(R.id.transaction_history_send_date);
        transaction_history_date.setTypeface(hnLight);

        TextView transaction_history_action = (TextView) findViewById(R.id.transaction_history_send_action);
        transaction_history_action.setTypeface(hnLight);

        TextView transaction_history_rp = (TextView) findViewById(R.id.transaction_history_send_rp);
        transaction_history_rp.setTypeface(hnLight);

        TextView transaction_history_value = (TextView) findViewById(R.id.transaction_history_send_value);
        transaction_history_value.setTypeface(hnLight);

        TextView transaction_history_receive = (TextView) findViewById(R.id.transaction_history_receive);
        transaction_history_receive.setTypeface(hnLight);

        TextView transaction_history_receive_date = (TextView) findViewById(R.id.transaction_history_receive_date);
        transaction_history_receive_date.setTypeface(hnLight);

        TextView transaction_history_receive_action = (TextView) findViewById(R.id.transaction_history_receive_action);
        transaction_history_receive_action.setTypeface(hnLight);

        TextView transaction_history_receive_rp = (TextView) findViewById(R.id.transaction_history_receive_rp);
        transaction_history_receive_rp.setTypeface(hnLight);

        TextView transaction_history_receive_value = (TextView) findViewById(R.id.transaction_history_receive_value);
        transaction_history_receive_value.setTypeface(hnLight);

        TextView transaction_history_topup = (TextView) findViewById(R.id.transaction_history_topup);
        transaction_history_topup.setTypeface(hnLight);

        TextView transaction_history_topup_date = (TextView) findViewById(R.id.transaction_history_topup_date);
        transaction_history_topup_date.setTypeface(hnLight);

        TextView transaction_history_topup_action = (TextView) findViewById(R.id.transaction_history_topup_action);
        transaction_history_topup_action.setTypeface(hnLight);

        TextView transaction_history_topup_rp = (TextView) findViewById(R.id.transaction_history_topup_rp);
        transaction_history_topup_rp.setTypeface(hnLight);

        TextView transaction_history_topup_value = (TextView) findViewById(R.id.transaction_history_topup_value);
        transaction_history_topup_value.setTypeface(hnLight);

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
