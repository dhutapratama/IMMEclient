package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SendPayActivity extends AppCompatActivity {
    private Button mButton;
    String transaction_code = new String();
    Boolean error_status = false;
    String error_message = null;
    private ProgressDialog loading = null;

    ProgressBar LoadingAnimation;

    ListView RecipientList;
    JSONArray recipient_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        mButton = (Button) findViewById(R.id.scan_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(SendPayActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("PRESS BACK TO OPEN RECIPIENT");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.setCaptureActivity(CustomLayout.class);
                integrator.initiateScan();
            }
        });

        RecipientList = (ListView) findViewById(R.id.RecipientList);
        RecipientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String search_id = null,
                        picture_url = null,
                        name = null;
                if (recipient_list.length() > 0) {
                    try {
                        JSONObject recipient = recipient_list.getJSONObject(position);
                        search_id = recipient.getString("search_id");
                        picture_url = recipient.getString("picture_url");
                        name = recipient.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(getApplicationContext(), SendToFriendActivity.class);
                    intent.putExtra("search_id", search_id);
                    intent.putExtra("picture_url", picture_url);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            }
        });

        LoadingAnimation = (ProgressBar) findViewById(R.id.LoadingAnimation);

        new get_account().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(resultCode != 0) {
                transaction_code = result.getContents();
                loading = ProgressDialog.show(SendPayActivity.this , "", "Checking recipient", true, true);
                new send_check().execute();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    private class send_check extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                sendCheck();
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
            if (SendPayActivity.this.loading != null) {
                SendPayActivity.this.loading.dismiss();
            }
            if (error_status) {
                Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_LONG).show();
            } else {
                Intent intentView = new Intent(getApplicationContext(), SendPayPersonalDetail.class);
                startActivity(intentView);
            }
        }
    }

    private boolean sendCheck() throws IOException, JSONException {
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&transaction_code=" + URLEncoder.encode(transaction_code, "UTF-8");
        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "pay/check", postData);

        if (serviceResult.getBoolean("error")){
            error_status = true;
            error_message = serviceResult.getString("message");
        } else {
            error_status = false;
            GlobalVariable.PAY_RECIPIENT_NAME = serviceResult.getString("recipient_name");
            GlobalVariable.PAY_AMOUNT = serviceResult.getString("amount");
            GlobalVariable.PAY_APPLY_CODE = serviceResult.getString("apply_code");
        }
        return serviceResult.getBoolean("error");
    }

    private class get_account extends AsyncTask<String, String, JSONObject> {
        protected JSONObject doInBackground(String... args) {
            JSONObject serviceResult = null;
            try {
                String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8");
                serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "recipient/get_list", postData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return serviceResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(JSONObject feedback_data) {
            LoadingAnimation.setVisibility(View.GONE);
            if (feedback_data.length() == 0) {
                Toast.makeText(SendPayActivity.this, "Server issue, please contact 081235404833", Toast.LENGTH_LONG).show();
                finish();
            }
            try {
                if (feedback_data.getBoolean("error")) {
                    Toast.makeText(SendPayActivity.this, feedback_data.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    recipient_list = feedback_data.getJSONArray("recipient_list");

                    List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
                    SimpleAdapter adapter = null;
                    String[] from = {"recipientName"};
                    int[] to = {R.id.recipientName};

                    if(!feedback_data.getBoolean("data_available")) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("recipientName", "empty recipient list");
                        aList.add(hm);
                        adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_recipient_empty, from, to);
                    } else {
                        for (int i = 0; i < recipient_list.length(); i++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            JSONObject recipient = recipient_list.getJSONObject(i);
                            hm.put("recipientName", recipient.getString("name"));
                            aList.add(hm);
                        }

                        final Context context = getBaseContext();
                        adapter = new SimpleAdapter(context, aList, R.layout.list_recipient, from, to){
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
                    }
                    RecipientList.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
