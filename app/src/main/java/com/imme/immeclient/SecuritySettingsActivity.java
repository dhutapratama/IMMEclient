package com.imme.immeclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecuritySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF249962"));

        initList();

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

    List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();

    private void initList() {
        int[] icon = new int[]{
                R.mipmap.profile_camera,
                R.mipmap.profile_camera,
                R.mipmap.profile_camera,
                R.mipmap.profile_camera
        };

        String[] menu_item = new String[]{
                "Change PIN 1",
                "Change PIN 2",
                "GPS Location",
                "Using Color PIN"
        };

        String[] menu_description = new String[]{
                "Last change 10 Des 2015",
                "Last change 3 Nov 2015",
                "Always track transaction",
                "Off"
        };

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<4;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("icon", Integer.toString(icon[i]) );
            hm.put("menu_item", menu_item[i]);
            hm.put("menu_description",menu_description[i]);
            aList.add(hm);
        }

        String[] from = { "icon","menu_item","menu_description" };
        int[] to = { R.id.icon ,R.id.menu_item ,R.id.menu_description};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_security_settings, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) findViewById(R.id.listView);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);
    }
}
