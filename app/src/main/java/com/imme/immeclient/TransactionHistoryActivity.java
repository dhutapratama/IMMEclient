package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {

    ProgressDialog loading;
    Boolean error_status = false;
    String error_message, message;
    Integer error_code = 0;
    JSONArray transactions;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF249962"));
        loading = ProgressDialog.show(TransactionHistoryActivity.this, "", "Loading your transaction", false);
        new get_transaction_history().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transaction_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initList() throws JSONException {
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        if(transactions == null) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("image_trans_type", Integer.toString(R.mipmap.imme_logo));
            hm.put("text_trans_type", "-");
            hm.put("date", "");
            hm.put("name", "No Transaction");
            hm.put("amount", "-");

            aList.add(hm);
        } else {
            for (int i = 0; i < transactions.length(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                JSONObject transaction = transactions.getJSONObject(i);

                if (transaction.getString("type").equals("1")) {
                    hm.put("image_trans_type", Integer.toString(R.mipmap.transaction_history_button_receive_icon));
                    hm.put("text_trans_type", "Receive");
                } else if (transaction.getString("type").equals("2")) {
                    hm.put("image_trans_type", Integer.toString(R.mipmap.transaction_history_button_send_icon));
                    hm.put("text_trans_type", "Transfer");
                } else if (transaction.getString("type").equals("5")) {
                    hm.put("image_trans_type", Integer.toString(R.mipmap.transaction_history_button_topup_icon));
                    hm.put("text_trans_type", "Deposit");
                }
                hm.put("date", transaction.getString("date"));
                hm.put("name", transaction.getString("name"));
                hm.put("amount", transaction.getString("amount"));

                aList.add(hm);
            }

        }

        String[] from = {"image_trans_type", "text_trans_type", "date", "name", "amount"};
        int[] to = {R.id.lth_image_trans_type, R.id.lth_text_trans_type, R.id.lth_date, R.id.lth_name, R.id.lth_amount};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_transaction_history, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = (ListView) findViewById(R.id.list_transaction);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);
    }

    private class get_transaction_history extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                getTransactionHistory();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (TransactionHistoryActivity.this.loading != null) {
                TransactionHistoryActivity.this.loading.dismiss();
            }

            if (error_status) {
                Intent intent = new Intent(TransactionHistoryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(TransactionHistoryActivity.this, error_message, Toast.LENGTH_LONG).show();
            } else {
                try {
                    initList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getTransactionHistory() throws JSONException, IOException {
        String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8");

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "history/transaction", postData);

        if (serviceResult.getBoolean("error")) {
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
            transactions = serviceResult.getJSONArray("transactions");
        }
    }
}
