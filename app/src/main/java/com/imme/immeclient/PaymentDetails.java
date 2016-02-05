package com.imme.immeclient;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentDetails extends AppCompatActivity {

    MLRoundedImageView DetailImage;
    TextView DetailName, DetailDate, total_price;
    String[] products_name, quantity, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF0093d9"));

        DetailImage = (MLRoundedImageView) findViewById(R.id.DetailImage);
        DetailName = (TextView) findViewById(R.id.DetailName);
        DetailDate = (TextView) findViewById(R.id.DetailDate);
        total_price = (TextView) findViewById(R.id.pd_total_price);

        if (getIntent().getStringExtra("reference") == null) {
            finish();
        } else {
            new get_main_data().execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent = new Intent(PaymentDetails.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PaymentDetails.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initList() {
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<1;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("products_name", products_name[i] );
            hm.put("quantity", quantity[i]);
            hm.put("price",price[i]);
            aList.add(hm);
        }

        String[] from = { "products_name","quantity","price" };
        int[] to = { R.id.lpp_product_name ,R.id.lpp_qty ,R.id.lpp_price};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_payment_products, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) findViewById(R.id.product_list);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);
    }

    private class get_main_data extends AsyncTask<String, String, JSONObject> {
        LinearLayout LLDetail;
        ProgressBar LoadingAnimation;

        protected JSONObject doInBackground(String... args) {
            JSONObject serviceResult = null;
            try {
                String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                        + "&reference=" + URLEncoder.encode(getIntent().getStringExtra("reference"), "UTF-8");
                serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "history/transaction_detail", postData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return serviceResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingAnimation = (ProgressBar) findViewById(R.id.LoadingAnimation);
            LLDetail = (LinearLayout) findViewById(R.id.LLDetail);
            LoadingAnimation.setVisibility(View.VISIBLE);
            LLDetail.setVisibility(View.GONE);
        }

        protected void onPostExecute(JSONObject feedback_data) {
            LLDetail.setVisibility(View.VISIBLE);
            LoadingAnimation.setVisibility(View.GONE);

            if (feedback_data.length() == 0) {
                Toast.makeText(PaymentDetails.this, "Server issue, please contact 081235404833", Toast.LENGTH_LONG).show();
                finish();
            } else {
                try {
                    if (feedback_data.getBoolean("error")) {
                        Toast.makeText(PaymentDetails.this, feedback_data.getString("message"), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        JSONObject data = feedback_data.getJSONObject("data");

                        ImageLoadPlease(PaymentDetails.this, data.getString("picture_url"), DetailImage);
                        DetailName.setText(data.getString("name"));
                        DetailDate.setText(data.getString("date"));
                        total_price.setText(data.getString("amount"));

                        products_name = new String[]{
                                data.getString("type")
                        };
                        quantity = new String[]{
                                "1"
                        };
                        price = new String[]{
                                data.getString("amount")
                        };

                        initList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
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
