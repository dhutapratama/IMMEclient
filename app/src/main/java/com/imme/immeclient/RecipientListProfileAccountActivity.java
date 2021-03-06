package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class RecipientListProfileAccountActivity extends AppCompatActivity {

    ProgressDialog loading;
    Boolean error_status = false;
    String error_message, message, feedback_name, search_id, feedback_picture;
    Integer error_code = 0;
    TextView button_remove, recipient_name;
    ImageView recipient_picture;
    ProgressBar loading_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_list_profile_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        search_id = getIntent().getStringExtra("search_id");

        loading_bar = (ProgressBar) findViewById(R.id.Loading);
        recipient_picture = (ImageView) findViewById(R.id.RecipientPicture);
        recipient_name = (TextView) findViewById(R.id.RecipientName);
        button_remove = (TextView) findViewById(R.id.ButtonRemove);

        new get_recipient().execute();

        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_remove.setVisibility(View.GONE);
                new remove_recipient().execute();
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

    private class remove_recipient extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                removeRecipient();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (error_status) {
                Toast.makeText(RecipientListProfileAccountActivity.this, error_message, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(RecipientListProfileAccountActivity.this, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RecipientListProfileAccountActivity.this, RecipientListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    public void removeRecipient() throws JSONException, IOException {
        String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&search_id=" + URLEncoder.encode(search_id, "UTF-8");

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "recipient/remove_recipient", postData);

        if (serviceResult.getBoolean("error")) {
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
            message = serviceResult.getString("message");
        }
    }

    private class get_recipient extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                getRecipient();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (error_status) {
                Toast.makeText(RecipientListProfileAccountActivity.this, error_message, Toast.LENGTH_LONG).show();
                finish();
            } else {
                loading_bar.setVisibility(View.GONE);
                recipient_picture.setVisibility(View.VISIBLE);
                recipient_name.setVisibility(View.VISIBLE);
                button_remove.setVisibility(View.VISIBLE);
                recipient_name.setText(feedback_name);
                ImageLoadPlease(getApplicationContext(), feedback_picture, recipient_picture);
            }
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
    }

    public void getRecipient() throws JSONException, IOException {
        String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&search_id=" + URLEncoder.encode(search_id, "UTF-8");

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "recipient/get_recipient", postData);

        if (serviceResult.getBoolean("error")) {
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
            feedback_name = serviceResult.getString("name");
            feedback_picture = serviceResult.getString("picture_url");
        }
    }
}
