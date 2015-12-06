package com.imme.immeclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SecuritySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_settings);
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

        //Get widgets reference from XML layout
        //final TextView tView = (TextView) findViewById(R.id.tv);
        //Switch sButton = (Switch) findViewById(R.id.switch_btn);

        //Set a Click Listener for Switch Button
        //sButton.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View v) {
                //Is the switch is on?
        //  boolean on = ((Switch) v).isChecked();
        //      if (on) {
                    //Do something when switch is on/checked
        //          tView.setText("Switch is on....");
        //      } else {
                    //Do something when switch is off/unchecked
        //          tView.setText("Switch is off....");
        //      }
        //  }
        //});
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
