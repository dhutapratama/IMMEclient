package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView main_textview_balance_value;
    NavigationView navigationView;
    String intent_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        } else {
            try {
                initVariable();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            toolbar.getLayoutParams().height = toolbar.getLayoutParams().height + getStatusBarHeight();
        }
        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        TextView main_textview_balance = (TextView) findViewById(R.id.main_textview_balance);
        main_textview_balance.setTypeface(hnLight);

        TextView main_textview_rp = (TextView) findViewById(R.id.main_textview_rp);
        main_textview_rp.setTypeface(hnLight);

        main_textview_balance_value = (TextView) findViewById(R.id.main_textview_balance_value);
        main_textview_balance_value.setTypeface(hbqLight);

        TextView main_textview_last_transaction = (TextView) findViewById(R.id.main_textview_last_transaction);
        main_textview_last_transaction.setTypeface(hnLight);

        TextView main_textview_history_name1 = (TextView) findViewById(R.id.main_textview_history_name1);
        main_textview_history_name1.setTypeface(hnLight);

        TextView main_textview_history_name2 = (TextView) findViewById(R.id.main_textview_history_name2);
        main_textview_history_name2.setTypeface(hnLight);

        TextView main_textview_history_name3 = (TextView) findViewById(R.id.main_textview_history_name3);
        main_textview_history_name3.setTypeface(hnLight);

        TextView main_textview_history_total1 = (TextView) findViewById(R.id.main_textview_history_total1);
        main_textview_history_total1.setTypeface(hnLight);

        TextView main_textview_history_total2 = (TextView) findViewById(R.id.main_textview_history_total2);
        main_textview_history_total2.setTypeface(hnLight);

        TextView main_textview_history_total3 = (TextView) findViewById(R.id.main_textview_history_total3);
        main_textview_history_total3.setTypeface(hnLight);

        TextView main_textview_history_date1 = (TextView) findViewById(R.id.main_textview_history_date1);
        main_textview_history_date1.setTypeface(hnLight);

        TextView main_textview_history_date2 = (TextView) findViewById(R.id.main_textview_history_date2);
        main_textview_history_date2.setTypeface(hnLight);

        TextView main_textview_history_date3 = (TextView) findViewById(R.id.main_textview_history_date3);
        main_textview_history_date3.setTypeface(hnLight);

        TextView main_textview_hello = (TextView) findViewById(R.id.main_textview_hello);
        main_textview_hello.setTypeface(hnLight);

        TextView main_textview_what_do_you_need = (TextView) findViewById(R.id.main_textview_what_do_you_need);
        main_textview_what_do_you_need.setTypeface(hnLight);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.mipmap.imme_logo);

        // Status Bar Coloring
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.MONEY_MAIN_BALANCE);
        main_textview_balance_value.setText(formated_money);

        // Set menu header so can edit text
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        TextView full_name = (TextView) header.findViewById(R.id.full_name);
        TextView verified_status = (TextView) header.findViewById(R.id.verified_status);
        ImageView verified_icon = (ImageView) header.findViewById(R.id.verified_icon);

        full_name.setText(GlobalVariable.CUSTOMER_FULL_NAME);
        if (GlobalVariable.CUSTOMER_IS_VERIFIED_EMAIL.equals("true") && GlobalVariable.CUSTOMER_IS_VERIFIED_PHONE.equals("true")) {
            verified_status.setText("Verified");
            verified_icon.setVisibility(View.VISIBLE);
        } else {
            verified_status.setText("Not Verified");
            verified_icon.setVisibility(View.GONE);
        }

        // Button Action
        ImageButton main_button_send_pay = (ImageButton) findViewById(R.id.main_button_send_pay);
        main_button_send_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_status = "send";
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("PRESS BACK TO OPEN RECIPIENT");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.setCaptureActivity(CustomLayout.class);
                integrator.initiateScan();
            }
        });

        ImageButton main_button_receive = (ImageButton) findViewById(R.id.main_button_receive);
        main_button_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReceiveActivity.class);
                startActivity(intent);
            }
        });

        ImageButton main_button_topup = (ImageButton) findViewById(R.id.main_button_topup);
        main_button_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_status = "deposit";
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("SCAN VOUCHER BARCODE");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.setCaptureActivity(CustomLayout.class);
                integrator.initiateScan();
            }
        });


        RelativeLayout last_transaction_1 = (RelativeLayout) findViewById(R.id.last_transaction_1);
        last_transaction_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "PT. Lazada E-Commerce (-Rp300.000)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                       */
                Intent intent = new Intent("com.imme.immeclient.PaymentDetails");
                startActivity(intent);
            }
        });

        RelativeLayout last_transaction_2 = (RelativeLayout) findViewById(R.id.last_transaction_2);
        last_transaction_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.imme.immeclient.PaymentDetails");
                startActivity(intent);
            }
        });

        RelativeLayout last_transaction_3 = (RelativeLayout) findViewById(R.id.last_transaction_3);
        last_transaction_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.imme.immeclient.PaymentDetails");
                startActivity(intent);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*try {
            GetImage.productLookup();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Intent intent = new Intent(this, ExampleService.class);
        startService(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.nav_user_agreement) {
            Intent intent = new Intent("com.imme.immeclient.UserAgreementActivity");
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_privacy_policy) {
            Intent intent = new Intent("com.imme.immeclient.PrivacyPolicyActivity");
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_help_support) {
           Intent intent = new Intent("com.imme.immeclient.HelpAndSupportActivity");
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_help_support) {
            Intent intent = new Intent("com.imme.immeclient.HelpAndSupportActivity");
            startActivity(intent);
            return true;
        }
        */
        if (id == R.id.nav_feedback) {
            Intent intent = new Intent("com.imme.immeclient.FeedbackActivity");
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_about) {
            Intent intent = new Intent("com.imme.immeclient.AboutActivity");
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_sign_out) {
            try {
                signOut();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finish();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        // Gift page hide
        //else if (id == R.id.nav_gift) {
          //  Intent intent = new Intent("com.imme.immeclient.GiftActivity");
            //startActivity(intent);
            //return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_account) {
            drawer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intent);
                }
            }, 200);
        }
        else if (id == R.id.nav_transaction_history) {
            drawer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), TransactionHistoryActivity.class);
                    startActivity(intent);
                }
            }, 200);
        }
        else if (id == R.id.nav_security_settings) {
            drawer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), SecuritySettingsActivity.class);
                    startActivity(intent);
                }
            }, 200);
        }
        else if (id == R.id.nav_recipient_list) {
            drawer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), RecipientListActivity.class);
                    startActivity(intent);
                }
            }, 200);
        }
        else if (id == R.id.nav_invite_get_money) {
            drawer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), InviteGetMoneyActivity.class);
                    startActivity(intent);
                }
            }, 200);
        }
        else if (id == R.id.nav_exit) {
            drawer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
            }, 200);
        }
        return true;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    String voucher_code = new String();
    String transaction_code = new String();
    Boolean error_status = false;
    String error_message = null;
    private ProgressDialog loading = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(resultCode != 0) {
                if (intent_status.equals("deposit")) {
                    voucher_code = result.getContents();
                    loading = ProgressDialog.show(MainActivity.this, "", "Validating voucher", true, true);
                    new voucher_check().execute();
                } else if (intent_status.equals("send")){
                    transaction_code = result.getContents();
                    loading = ProgressDialog.show(MainActivity.this, "", "Checking recipient", true, true);
                    new send_check().execute();
                }
            } else {
                if (intent_status.equals("send")) {
                    Intent intent = new Intent(getApplicationContext(), SendPayActivity.class);
                    startActivity(intent);
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class send_check extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                sendCheck();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (MainActivity.this.loading != null) {
                MainActivity.this.loading.dismiss();
            }
            if (error_status) {
                Toast.makeText(MainActivity.this, error_message, Toast.LENGTH_LONG).show();
            } else {
                Intent intentView = new Intent(getApplicationContext(), SendPayPersonalDetail.class);
                startActivity(intentView);
            }
        }
    }

    private boolean sendCheck() throws IOException, JSONException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&transaction_code=" + URLEncoder.encode(transaction_code, "UTF-8");
        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "pay/check", postData);

        if (serviceResult.getBoolean("error")){
            error_status = true;
            error_message = serviceResult.getString("message");
        } else {
            error_status = false;
            GlobalVariable.PAY_RECIPIENT_NAME = serviceResult.getString("recipient_name");
            GlobalVariable.PAY_AMOUNT = serviceResult.getString("amount");
            GlobalVariable.PAY_APPLY_CODE = serviceResult.getString("apply_code");
        }
        return serviceResult.getBoolean("error");
    }

    private class voucher_check extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                voucherCheck();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (MainActivity.this.loading != null) {
                MainActivity.this.loading.dismiss();
            }
            if (error_status) {
                Toast.makeText(MainActivity.this, error_message, Toast.LENGTH_LONG).show();
            } else {
                String formated_money = NumberFormat.getNumberInstance(Locale.GERMANY).format(GlobalVariable.MONEY_MAIN_BALANCE);
                main_textview_balance_value.setText(formated_money);

                Intent intent = new Intent("com.imme.immeclient.Deposit");
                startActivity(intent);
            }
        }
    }

    private boolean voucherCheck() throws IOException, JSONException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&voucher_code=" + URLEncoder.encode(voucher_code, "UTF-8");
        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "deposit", postData);

        if (serviceResult.getBoolean("error")){
            error_status = true;
            error_message = serviceResult.getString("message");
        } else {
            // VARIABLE SET
            error_status = false;
            GlobalVariable.DEPOSIT_AMOUNT = Integer.parseInt(serviceResult.getString("deposit_amount"));
            GlobalVariable.MONEY_MAIN_BALANCE = Integer.parseInt(serviceResult.getString("balance"));
            commit();
        }
        return serviceResult.getBoolean("error");
    }

    public void writeFile(String varname, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(varname, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFile(String varname) {
        String ret = "";
        try {
            InputStream inputStream = openFileInput(varname);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("SplashScreen", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("SplashScreen", "Can not read file: " + e.toString());
        }
        return ret;
    }

    private void signOut() throws JSONException {
        JSONObject serverData = new JSONObject();
        JSONObject securityData = new JSONObject();
        JSONObject moneyData = new JSONObject();
        JSONObject customerData = new JSONObject();
        JSONObject appData = new JSONObject();

        // Server Data
        serverData.put("distributor_server", "http://api.studiwidie.com/");
        serverData.put("ack_code", "5GwcTzO0ODM6eSV3s66PJjeedlEvxWc9");
        serverData.put("otp_key", "OTP_KEY");

        // Security Data
        securityData.put("imme_algorithm", "IMME_ALGORITHM");
        securityData.put("tba_algorithm", "TBA_ALGORITHM");
        securityData.put("cba_algorithm", "CBA_ALGORITHM");
        securityData.put("cba_counter", "CBA_COUNTER");
        securityData.put("session_key", "SESSION_KEY");
        securityData.put("csrf_token", "CSRF_TOKEN");
        securityData.put("user_agent","USER_AGENT");

        // Customer Data
        customerData.put("account_number", "ACCOUNT_NUMBER");
        customerData.put("full_name", "FULL_NAME");
        customerData.put("picture_url", "PICTURE_URL");
        customerData.put("email", "EMAIL");
        customerData.put("phone_number", "PHONE_NUMBER");
        customerData.put("idcard_number", "IDCARD_NUMBER");
        customerData.put("idcard_type", "IDCARD_TYPE");
        customerData.put("is_verified_email", "false");
        customerData.put("is_verified_phone", "false");

        // Money Data
        moneyData.put("main_balance", "0");
        moneyData.put("send_ammount", "0");
        moneyData.put("request_amount", "0");
        moneyData.put("transaction_code", "0");

        // App Data

        appData.put("first_time_app", "false");
        appData.put("login_status", "false");
        appData.put("client_version","1.0.0");

        writeFile(GlobalVariable.FILE_SERVER, serverData.toString());
        writeFile(GlobalVariable.FILE_SECURITY, securityData.toString());
        writeFile(GlobalVariable.FILE_CUSTOMER, customerData.toString());
        writeFile(GlobalVariable.FILE_MONEY, moneyData.toString());
        writeFile(GlobalVariable.FILE_APP, appData.toString());
        initVariable();
    }

    private void initVariable() throws JSONException {
        String securityContent = readFile(GlobalVariable.FILE_SECURITY);
        JSONObject securityData = new JSONObject(securityContent);

        String customerContent = readFile(GlobalVariable.FILE_CUSTOMER);
        JSONObject customerData = new JSONObject(customerContent);

        String moneyContent = readFile(GlobalVariable.FILE_MONEY);
        JSONObject moneyData = new JSONObject(moneyContent);

        String appContent = readFile(GlobalVariable.FILE_APP);
        JSONObject appData = new JSONObject(appContent);

        // Security Data
        GlobalVariable.SECURITY_IMME_ALGORITHM = securityData.getString("imme_algorithm");
        GlobalVariable.SECURITY_TBA_ALGORITHM = securityData.getString("tba_algorithm");
        GlobalVariable.SECURITY_CBA_ALGORITHM = securityData.getString("cba_algorithm");
        GlobalVariable.SECURITY_CBA_COUNTER = securityData.getString("cba_counter");
        GlobalVariable.SECURITY_SESSION_KEY = securityData.getString("session_key");
        GlobalVariable.SECURITY_CSRF_TOKEN = securityData.getString("csrf_token");
        GlobalVariable.SECURITY_USER_AGENT = securityData.getString("user_agent");

        // Customer Data
        GlobalVariable.CUSTOMER_ACCOUNT_NUMBER = customerData.getString("account_number");
        GlobalVariable.CUSTOMER_FULL_NAME = customerData.getString("full_name");
        GlobalVariable.CUSTOMER_PICTURE_URL = customerData.getString("picture_url");
        GlobalVariable.CUSTOMER_EMAIL = customerData.getString("email");
        GlobalVariable.CUSTOMER_PHONE_NUMBER = customerData.getString("phone_number");
        GlobalVariable.CUSTOMER_IDCARD_NUMBER = customerData.getString("idcard_number");
        GlobalVariable.CUSTOMER_IDCARD_TYPE = customerData.getString("idcard_type");
        GlobalVariable.CUSTOMER_IS_VERIFIED_EMAIL = customerData.getString("is_verified_email");
        GlobalVariable.CUSTOMER_IS_VERIFIED_PHONE = customerData.getString("is_verified_phone");

        // Money Data
        GlobalVariable.MONEY_MAIN_BALANCE = Integer.parseInt(moneyData.getString("main_balance"));
        GlobalVariable.MONEY_SEND_AMOUNT = Integer.parseInt(moneyData.getString("send_ammount"));
        GlobalVariable.MONEY_REQUEST_AMOUNT = Integer.parseInt(moneyData.getString("request_amount"));
        GlobalVariable.MONEY_TRANSACTION_CODE = moneyData.getString("transaction_code");

        // Money Data
        GlobalVariable.APP_FIRST_TIME_APP = appData.getString("first_time_app");
        GlobalVariable.APP_LOGIN_STATUS = appData.getString("login_status");
        GlobalVariable.APP_CLIENT_VERSION = appData.getString("client_version");
    }

    private void commit() throws JSONException {
        String moneyContent = readFile(GlobalVariable.FILE_MONEY);
        JSONObject moneyData = new JSONObject(moneyContent);

        // Money Data
        moneyData.put("main_balance", Integer.toString(GlobalVariable.MONEY_MAIN_BALANCE));

        writeFile(GlobalVariable.FILE_MONEY, moneyData.toString());
    }
}
