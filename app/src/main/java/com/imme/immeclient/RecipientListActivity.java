package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipientListActivity extends AppCompatActivity {
    Boolean error_status = false, data_available;
    String error_message, message;
    Integer error_code = 0;
    JSONArray recipient_list;
    String search_id;
    ProgressBar LoadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF249962"));

        new get_recipient_list().execute();

        final ListView RecipientList = (ListView) findViewById(R.id.RecipientList);
        RecipientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject recipient_data = null;
                try {
                    recipient_data = recipient_list.getJSONObject(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    search_id = recipient_data.getString("search_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), RecipientListProfileAccountActivity.class);
                intent.putExtra("search_id", search_id);
                startActivity(intent);
            }
        });

        TextView recipient_list_add_account = (TextView) findViewById(R.id.recipient_list_add_account);
        recipient_list_add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipientListActivity.this, RecipientListAddAccountActivity.class);
                startActivity(intent);
            }
        });

        LoadingAnimation = (ProgressBar) findViewById(R.id.LoadingAnimation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class get_recipient_list extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                getRecipientList();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Object result) {
            LoadingAnimation.setVisibility(View.GONE);
            if (error_status) {
                finish();
                Intent intent = new Intent(RecipientListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(RecipientListActivity.this, error_message, Toast.LENGTH_LONG).show();
            } else {
                try {
                    initList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getRecipientList() throws JSONException, IOException {
        String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8");

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "recipient/get_list", postData);

        if (serviceResult.getBoolean("error")) {
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
            data_available = serviceResult.getBoolean("data_available");
            if (serviceResult.getJSONArray("recipient_list")!= null) {
                recipient_list = serviceResult.getJSONArray("recipient_list");
            }
        }
    }

    private void initList() throws JSONException {
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        if(!data_available) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("recipientName", "empty recipient list");
            aList.add(hm);
        } else {
            for (int i = 0; i < recipient_list.length(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                JSONObject recipient = recipient_list.getJSONObject(i);
                hm.put("recipientName", recipient.getString("name"));
                aList.add(hm);
            }

        }

        String[] from = {"recipientName"};
        int[] to = {R.id.recipientName};
        SimpleAdapter adapter;
        final Context context = getBaseContext();
        if (data_available) {
            adapter = new SimpleAdapter(context, aList, R.layout.list_recipient, from, to) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    ImageView recipientPicture = (ImageView) v.findViewById(R.id.recipientPicture);
                    String PictureURL = "";
                    try {
                        PictureURL = recipient_list.getJSONObject(position).getString("picture_url");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ImageLoadPlease(context, PictureURL, recipientPicture);
                    return v;
                }

                public ImageLoader ImageLoadPlease(Context context, String imageURI, ImageView target) {
                    ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
                    config.threadPriority(Thread.NORM_PRIORITY - 2);
                    config.denyCacheImageMultipleSizesInMemory();
                    config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
                    config.diskCacheSize(500 * 1024 * 1024);

                    ImageLoader.getInstance().init(config.build());

                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.about_logo_imme)
                            .showImageForEmptyUri(R.mipmap.about_logo_imme)
                            .showImageOnFail(R.mipmap.about_logo_imme)
                            .resetViewBeforeLoading(false)
                            .delayBeforeLoading(100)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .build();

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(imageURI, target, options);
                    return imageLoader;
                }
            };
        } else {
            adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_recipient_empty, from, to);
        }

        // Getting a reference to listview of main.xml layout file
        ListView listView = (ListView) findViewById(R.id.RecipientList);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);
    }
}
