package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Locale;

public class SendPayPersonalDetail extends AppCompatActivity {
    Boolean error_status = false;
    String error_message = null;
    private ProgressDialog loading = null;
    CountDownTimer count_down_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pay_personal_detail);
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

        final TextView time_out = (TextView) findViewById(R.id.personal_time_out);
        count_down_timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                time_out.setText("Cancel in " + millisUntilFinished / 1000L);
            }

            public void onFinish() {
                Intent intent = new Intent(SendPayPersonalDetail.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }.start();

        Button btn_personal_send = (Button) findViewById(R.id.btn_personal_send);
        btn_personal_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                count_down_timer.cancel();
                Intent intentView = new Intent("com.imme.immeclient.PinActivity");
                startActivity(intentView);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new cancel_pay().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new cancel_pay().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class cancel_pay extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                cancelPay();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (SendPayPersonalDetail.this.loading != null) {
                SendPayPersonalDetail.this.loading.dismiss();
            }
            count_down_timer.cancel();
            Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void cancelPay() throws IOException, JSONException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&apply_code=" + URLEncoder.encode(GlobalVariable.PAY_APPLY_CODE, "UTF-8");
        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "pay/cancel", postData);

        error_message = serviceResult.getString("message");
        if (serviceResult.getBoolean("error")){
            error_status = true;
        } else {
            error_status = false;
        }
    }
}
