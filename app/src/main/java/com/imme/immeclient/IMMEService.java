package com.imme.immeclient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLEncoder;

/**
 * Created by sysadm@ilccourse.com on 1/7/2016.
 */
public class IMMEService extends IntentService {

    public IMMEService() {
        super("IMMEService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.v("Service", "Started");
        while(true) {
            try {
                if (!notification()) {
                    if (available_notification) {
                        for (int i = 0; i < notification.length(); i++) {
                            JSONObject notif = notification.getJSONObject(i);
                            create(notif.getInt("id"), notif.getString("type"), notif.getString("text"));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    Boolean error_status, available_notification;
    String error_message;
    JSONArray notification;

    private boolean notification() throws IOException, JSONException {
        Log.v("NOTIF", "Notif start");
        String postData ="session_key=" + URLEncoder.encode(GlobalVariable.SECURITY_SESSION_KEY, "UTF-8");
        JSONObject serviceResult = WebServiceClient.postRequest(GlobalVariable.DISTRIBUTOR_SERVER + "notification", postData);

        if (serviceResult.getBoolean("error")){
            error_status = true;
            error_message = serviceResult.getString("message");
        } else {
            // VARIABLE SET
            error_status = false;
            available_notification = serviceResult.getBoolean("available_notification");
            if (available_notification) {
                notification = serviceResult.getJSONArray("notification");
            }
        }
        Log.v("NOTIF", "Notif finish");
        return serviceResult.getBoolean("error");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        while(true)
        {
            create(10, "IMME", "Service started");
            try {
                create(10, "IMME", "Open notification");
                notification();
                create(10, "IMME", "Close Notification");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return START_STICKY;
        }
    }

    public void create(Integer id, String type, String text) {
        if (type.equals("notification")) {
            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("imme")
                            .setContentText(text);
            Intent resultIntent = new Intent(this, RecipientListActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);
            mBuilder.setAutoCancel(true);

            int mNotificationId = id;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        } else {
            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("imme")
                            .setContentText(text);

            Intent resultIntent = new Intent(this, RecipientListActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);
            mBuilder.setAutoCancel(true);

            int mNotificationId = id;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }
}
