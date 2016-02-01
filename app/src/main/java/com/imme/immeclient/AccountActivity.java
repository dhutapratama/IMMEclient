package com.imme.immeclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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

        //TextView account_textview_balance = (TextView) findViewById(R.id.account_textview_balance);
        //account_textview_balance.setTypeface(hnLight);

        //TextView account_textview_balance_rp = (TextView) findViewById(R.id.account_textview_balance_rp);
        //account_textview_balance_rp.setTypeface(hnLight);

        //TextView account_textview_balance_value = (TextView) findViewById(R.id.account_textview_balance_value);
        //account_textview_balance_value.setTypeface(hbqLight);

        //TextView account_textview_balance_gift = (TextView) findViewById(R.id.account_textview_balance_gift);
        //account_textview_balance_gift.setTypeface(hnLight);

        //TextView account_textview_balance_gift_rp = (TextView) findViewById(R.id.account_textview_balance_gift_rp);
        //account_textview_balance_gift_rp.setTypeface(hnLight);

        //TextView account_textview_balance_gift_value = (TextView) findViewById(R.id.account_textview_balance_gift_value);
        //account_textview_balance_gift_value.setTypeface(hbqLight);

        TextView account_textview_account_number = (TextView) findViewById(R.id.account_textview_account_number);
        account_textview_account_number.setTypeface(hnLight);

        TextView account_textview_account_number_value = (TextView) findViewById(R.id.account_textview_account_number_value);
        account_textview_account_number_value.setTypeface(hnLight);

        TextView account_textview_account_setting = (TextView) findViewById(R.id.account_textview_account_setting);
        account_textview_account_setting.setTypeface(hbqLight);

        TextView account_textview_full_name = (TextView) findViewById(R.id.account_textview_full_name);
        account_textview_full_name.setTypeface(hnLight);

        EditText account_edittext_full_name_value = (EditText) findViewById(R.id.account_edittext_full_name_value);
        account_edittext_full_name_value.setTypeface(hnLight);

        TextView account_textview_profil_image = (TextView) findViewById(R.id.account_textview_profil_image);
        account_textview_profil_image.setTypeface(hnLight);

        TextView account_textview_upload_image = (TextView) findViewById(R.id.account_textview_upload_image);
        account_textview_upload_image.setTypeface(hnLight);

        TextView account_textview_max_size = (TextView) findViewById(R.id.account_textview_max_size);
        account_textview_max_size.setTypeface(hnLight);

        TextView account_textview_email = (TextView) findViewById(R.id.account_textview_email);
        account_textview_email.setTypeface(hnLight);

        EditText account_edittext_email_value = (EditText) findViewById(R.id.account_edittext_email_value);
        account_edittext_email_value.setTypeface(hnLight);

        TextView account_textview_phone_number = (TextView) findViewById(R.id.account_textview_phone_number);
        account_textview_phone_number.setTypeface(hnLight);

        EditText account_edittext_phone_number_value = (EditText) findViewById(R.id.account_edittext_phone_number_value);
        account_edittext_phone_number_value.setTypeface(hnLight);

        TextView account_button_save = (TextView) findViewById(R.id.account_button_save);
        account_button_save.setTypeface(hbqLight);

        TextView account_textview_account_details = (TextView) findViewById(R.id.account_textview_account_details);
        account_textview_account_details.setTypeface(hbqLight);

        TextView account_textview_id_number = (TextView) findViewById(R.id.account_textview_id_number);
        account_textview_id_number.setTypeface(hnLight);

        TextView account_textview_id_number_value = (TextView) findViewById(R.id.account_textview_id_number_value);
        account_textview_id_number_value.setTypeface(hnLight);

        TextView account_textview_id_type = (TextView) findViewById(R.id.account_textview_id_type);
        account_textview_id_type.setTypeface(hnLight);

        TextView account_textview_id_type_value = (TextView) findViewById(R.id.account_textview_id_type_value);
        account_textview_id_type_value.setTypeface(hnLight);

        TextView account_textview_resend_verification_email = (TextView) findViewById(R.id.account_textview_resend_verification_email);
        account_textview_resend_verification_email.setTypeface(hnLight);

        TextView account_textview_verification_email = (TextView) findViewById(R.id.account_textview_verification_email);
        account_textview_verification_email.setTypeface(hbqLight);

        TextView account_textview_verification_phone_number = (TextView) findViewById(R.id.account_textview_verification_phone_number);
        account_textview_verification_phone_number.setTypeface(hbqLight);

        TextView account_textview_verify_phone_number = (TextView) findViewById(R.id.account_textview_verify_phone_number);
        account_textview_verify_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.imme.immeclient.AccountVerifyPhoneNumberStep1Activity");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
