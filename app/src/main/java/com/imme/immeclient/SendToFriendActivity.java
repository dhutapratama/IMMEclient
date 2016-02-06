package com.imme.immeclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SendToFriendActivity extends AppCompatActivity {

    String transfer_amount, transfer_message;
    EditText TransferMoney, TransferDescription;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#FF03B0FF"));

        TextView RecipientName = (TextView) findViewById(R.id.RecipientName);
        ImageView UserPicture = (ImageView) findViewById(R.id.UserPicture);

        TransferMoney = (EditText) findViewById(R.id.TransferMoney);
        TransferDescription = (EditText) findViewById(R.id.TransferDescription);

        String picture_url = getIntent().getStringExtra("picture_url");
        ImageLoadPlease(getBaseContext(), picture_url, UserPicture);
        RecipientName.setText(getIntent().getStringExtra("name"));

        TextView Btn_Transfer = (TextView) findViewById(R.id.Btn_Transfer);
        Btn_Transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer_amount = TransferMoney.getText().toString();
                transfer_message = TransferDescription.getText().toString();

                Intent intent = new Intent(SendToFriendActivity.this, PinActivity.class);
                intent.putExtra("search_id", getIntent().getStringExtra("search_id"));
                intent.putExtra("picture_url", getIntent().getStringExtra("picture_url"));
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("amount", transfer_amount);
                intent.putExtra("message", transfer_message);
                startActivity(intent);
            }
        });
    }

    public ImageLoader ImageLoadPlease(Context context, String imageURI, ImageView target) {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
            config.threadPriority(Thread.NORM_PRIORITY - 2);
            config.denyCacheImageMultipleSizesInMemory();
            config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
            config.diskCacheSize(500 * 1024 * 1024);

            ImageLoader.getInstance().init(config.build());
        }

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
        imageLoader.destroy();
        return imageLoader;
    }
}
