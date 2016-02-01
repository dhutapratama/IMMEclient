package com.imme.immeclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Send_pay_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pay_details);
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

        final Button button = (Button) findViewById(R.id.pay_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentView = new Intent("com.imme.immeclient.PinActivity");
                startActivity(intentView);
            }
        });

        initList();

        TextView cspd_balance = (TextView) findViewById(R.id.cspd_balance);
        String formated_balance = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.MONEY_MAIN_BALANCE);
        cspd_balance.setText(formated_balance);

        TextView cspd_merchant_name = (TextView) findViewById(R.id.cspd_merchant_name);
        cspd_merchant_name.setText(GlobalVariable.PAY_RECIPIENT_NAME);

        TextView total_price = (TextView) findViewById(R.id.ltp_total_price);
        total_price.setText("Rp " + GlobalVariable.PAY_AMOUNT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent = new Intent(Send_pay_details.this, MainActivity.class);
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
        Intent intent = new Intent(Send_pay_details.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initList() {
        String[] products_name = new String[]{
                "Payment"
        };

        String[] quantity = new String[]{
                "1"
        };

        String[] price = new String[]{
                "Rp " + GlobalVariable.PAY_AMOUNT
        };

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<1;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("products_name", products_name[i] );
            hm.put("quantity", quantity[i]);
            hm.put("price",price[i]);
            aList.add(hm);
        }

        String[] from = { "products_name","quantity","price" };
        int[] to = { R.id.lpp_product_name ,R.id.lpp_qty ,R.id.lpp_price};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_payment_products, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) findViewById(R.id.product_list);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);
    }
}