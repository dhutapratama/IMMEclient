package com.imme.immeclient;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by santo on 24-Oct-15.
 */
public class GlobalVariable {
    public static final String DISTRIBUTOR_SERVER = "http://api.studiwidie.com/";
    public static String ACK = "?request_code=1000&device_id=823498723948723984729&device_type=android&device_ip=192.168.1.1&date=2015-12-07+00%3A00%3A00&client_version=1.0.0&authentication_code=0sgsOhUwJ9dSLDZ78ztEG4LclvIdIOMjlLwbw9QjD4g%3D";

    public static final String MOBILESTATUS_FILE = "MobileStatus.json";
    public static String FIRST_TIME_APP = "true";
    public static String LOGIN_STATUS = "false";
    public static String USER_AGENT = "USER_AGENT";
    public static String CSRF_TOKEN = "CSRF_TOKEN";
    public static String ANDROID_ID = "ANROID_ID";
    public static String CLIENT_VERSION = "1.0.0";
    public static String CLIENT_IP () {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return "0.0.0.0";
    }

    //
}