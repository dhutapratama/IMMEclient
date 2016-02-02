package com.imme.immeclient;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class RecipientListAddAccountActivity extends AppCompatActivity {

    ProgressDialog loading;
    Boolean error_status = false, already;
    String error_message, message, feedback_name, search_id;
    Integer error_code = 0;
    EditText add_account_text;
    TextView button_add, recipient_name;
    ImageView recipient_picture;
    ProgressBar loading_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_list_add_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));



        add_account_text = (EditText) findViewById(R.id.add_account_text);
        final Button add_account_clear = (Button) findViewById(R.id.add_account_clear);
        Button add_account_search = (Button) findViewById(R.id.add_account_search);
        loading_bar = (ProgressBar) findViewById(R.id.Loading);
        recipient_picture = (ImageView) findViewById(R.id.RecipientPicture);
        recipient_name = (TextView) findViewById(R.id.RecipientName);
        button_add = (TextView) findViewById(R.id.ButtonAdd);

        add_account_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                add_account_text.setText("");
            }
        });

        add_account_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (add_account_text.length() > 0) {
                    add_account_clear.setVisibility(View.VISIBLE);
                } else {
                    add_account_clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        add_account_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_account_text.length() > 6) {
                    loading_bar.setVisibility(View.VISIBLE);
                    recipient_picture.setVisibility(View.GONE);
                    recipient_name.setVisibility(View.GONE);
                    button_add.setVisibility(View.GONE);

                    new search_recipient().execute();
                } else {
                    loading_bar.setVisibility(View.GONE);
                    recipient_picture.setVisibility(View.GONE);
                    recipient_name.setVisibility(View.VISIBLE);
                    button_add.setVisibility(View.GONE);

                    recipient_name.setText("Try using another email");
                }

            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new add_recipient().execute();
                button_add.setVisibility(View.GONE);
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

    private class search_recipient extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                searchRecipient();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            loading_bar.setVisibility(View.GONE);

            if (error_status) {
                recipient_name.setVisibility(View.VISIBLE);

                recipient_name.setText(error_message);
            } else {
                recipient_picture.setVisibility(View.VISIBLE);
                recipient_name.setVisibility(View.VISIBLE);
                if (!already) {
                    button_add.setVisibility(View.VISIBLE);
                }
                recipient_name.setText(feedback_name);
            }
        }
    }

    public void searchRecipient() throws JSONException, IOException {
        String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
            + "&recipient_email=" + URLEncoder.encode(String.valueOf(add_account_text.getText()), "UTF-8");

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "recipient/search_recipient", postData);

        if (serviceResult.getBoolean("error")) {
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
            feedback_name = serviceResult.getString("name");
            search_id = serviceResult.getString("search_id");
            already = serviceResult.getBoolean("already");
        }
    }


    private class add_recipient extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            try {
                addRecipient();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            if (error_status) {
                Toast.makeText(RecipientListAddAccountActivity.this, error_message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RecipientListAddAccountActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void addRecipient() throws JSONException, IOException {
        String postData = "session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8")
                + "&search_id=" + URLEncoder.encode(search_id, "UTF-8");

        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "recipient/add_recipient", postData);

        if (serviceResult.getBoolean("error")) {
            error_status = true;
            error_message = serviceResult.getString("message");
            error_code = serviceResult.getInt("code");
        } else {
            error_status = false;
            message = serviceResult.getString("message");
        }
    }
}
