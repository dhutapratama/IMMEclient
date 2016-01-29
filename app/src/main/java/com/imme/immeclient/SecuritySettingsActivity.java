package com.imme.immeclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

        ListView myList = (ListView) findViewById(R.id.listView);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(SecuritySettingsActivity.this, ChangePin1Process1Activity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Toast.makeText(SecuritySettingsActivity.this, "1", Toast.LENGTH_LONG).show();
                } else if (position == 2) {
                    Toast.makeText(SecuritySettingsActivity.this, "2", Toast.LENGTH_LONG).show();
                } else if (position == 3) {
                    Toast.makeText(SecuritySettingsActivity.this, "3", Toast.LENGTH_LONG).show();
                }
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

    String a = "something";

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

        if (a.equals("something")) {
            a = "New String Bro!";
        }

        String[] menu_description = new String[]{
                "Last change 10 Des 2015",
                "Last change 3 Nov 2015",
                a,
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
