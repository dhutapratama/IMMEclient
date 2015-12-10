package com.imme.immeclient;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //TextView sign_in_textview_sign_up = (TextView) findViewById(R.id.sign_in_textview_sign_up);
        //sign_in_textview_sign_up.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View view) {
        //   Intent intent = new Intent("com.imme.immeclient.SignUpActivity");
        //    startActivity(intent);
        // }
        //});

        // Start Font
        Typeface hnLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeue-Light.otf");

        Typeface hbqLight = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaBQ-Light.otf");

        EditText sign_in_edittext_email = (EditText) findViewById(R.id.sign_in_edittext_email);
        sign_in_edittext_email.setTypeface(hnLight);

        EditText sign_in_edittext_password = (EditText) findViewById(R.id.sign_in_edittext_password);
        sign_in_edittext_password.setTypeface(hnLight);

        TextView sign_in_button_sign_in = (TextView) findViewById(R.id.sign_in_button_sign_in);
        sign_in_button_sign_in.setTypeface(hbqLight);

    }

}
