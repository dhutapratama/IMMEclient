package com.imme.immeclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#ff0f99da"));

        TextView sign_up_button_sign_up = (TextView) findViewById(R.id.sign_up_button_sign_in);
        sign_up_button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.imme.immeclient.SignInActivity");
                startActivity(intent);
            }
        });

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        EditText sign_up_edittext_full_name = (EditText) findViewById(R.id.sign_up_edittext_full_name);
        sign_up_edittext_full_name.setTypeface(hnLight);

        EditText sign_up_edittext_email = (EditText) findViewById(R.id.sign_up_edittext_email);
        sign_up_edittext_email.setTypeface(hnLight);

        EditText sign_up_edittext_password = (EditText) findViewById(R.id.sign_up_edittext_password);
        sign_up_edittext_password.setTypeface(hnLight);

        EditText sign_up_edittext_confirm_password = (EditText) findViewById(R.id.sign_up_edittext_confirm_password);
        sign_up_edittext_confirm_password.setTypeface(hnLight);

        TextView sign_up_edittext_phone_number = (EditText) findViewById(R.id.sign_up_edittext_phone_number);
        sign_up_edittext_phone_number.setTypeface(hnLight);

        //TextView sign_up_button_sign_up = (TextView) findViewById(R.id.sign_up_button_sign_up);
        //sign_up_button_sign_up.setTypeface(hbqLight);
    }

}
