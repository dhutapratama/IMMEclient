package com.imme.immeclient;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * This Activity is exactly the same as CaptureActivity, but has a different orientation
 * setting in AndroidManifest.xml.
 */
public class CustomLayout extends CaptureActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        TextView overriding_text = (TextView) findViewById(R.id.text_over_scan);
        overriding_text.setText("Scan Here!");
    }

}