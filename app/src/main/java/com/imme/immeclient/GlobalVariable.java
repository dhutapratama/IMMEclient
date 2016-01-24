package com.imme.immeclient;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by santo on 24-Oct-15.
 */
public class GlobalVariable {
    // SERVER
    //public static final String DISTRIBUTOR_SERVER = "http://api3.imme.asia/";
    public static final String DISTRIBUTOR_SERVER = "http://api1.imme.asia/";
    //public static final String DISTRIBUTOR_SERVER = "http://api.imme.app/";
    public static final String ACK_CODE = "5GwcTzO0ODM6eSV3s66PJjeedlEvxWc9";
    public static String otp_key_generator () {
        long unixTime = System.currentTimeMillis() / 1000L;
        Integer date = (int)(long) unixTime;
        // Key Rebuilding
        int keylength = Integer.toString(date).length();
        String temp = Integer.toString(date);
        if (keylength == 16) {
            int diff = Integer.toString(date).length() - 16;
            for (int i = 0; i < diff; i ++) {
                temp = temp + "i";
            }
        }
        return temp;
    }
    public static String OTP_KEY = otp_key_generator();

    // FILE VARIABLE
    public static final String FILE_SERVER = "Server.json";
    public static final String FILE_SECURITY = "Security.json";
    public static final String FILE_CUSTOMER = "Customer.json";
    public static final String FILE_MONEY = "Money.json";
    public static final String FILE_APP = "App.json";

    // SECURITY VARIABLE
    public static String SECURITY_IMME_ALGORITHM = "IMME_KEY";
    public static String SECURITY_TBA_ALGORITHM = "TBA_KEY";
    public static String SECURITY_CBA_ALGORITHM = "CBA_KEY";
    public static String SECURITY_CBA_COUNTER = "CBA_COUNTER";
    public static String SECURITY_SESSION_KEY = "SESSION_KEY";
    public static String SECURITY_CSRF_TOKEN = "CSRF_TOKEN";
    public static String SECURITY_USER_AGENT = "USER_AGENT";

    // CUSTOMER VARIABLE
    public static String CUSTOMER_ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static String CUSTOMER_FULL_NAME = "FULL_NAME";
    public static String CUSTOMER_PICTURE_URL = "PICTURE_URL";
    public static String CUSTOMER_EMAIL = "EMAIL";
    public static String CUSTOMER_PHONE_NUMBER = "PHONE_NUMBER";
    public static String CUSTOMER_IDCARD_NUMBER = "IDCARD_NUMBER";
    public static String CUSTOMER_IDCARD_TYPE = "IDCARD_TYPE";
    public static String CUSTOMER_IS_VERIFIED_EMAIL = "false";
    public static String CUSTOMER_IS_VERIFIED_PHONE = "false";

    // APP VARIABLE
    public static String APP_FIRST_TIME_APP = "true";
    public static String APP_LOGIN_STATUS = "false";
    public static String APP_CLIENT_VERSION = "1.0.0";

    // MONEY
    public static int MONEY_MAIN_BALANCE = 0;
    public static int MONEY_SEND_AMOUNT = 0;
    public static int MONEY_REQUEST_AMOUNT = 0;
    public static String MONEY_TRANSACTION_CODE = "0";

    // IN APP CACHE VARIABLE
    public static String PAY_RECIPIENT_NAME = "";
    public static String PAY_AMOUNT = "";
    public static String PAY_APPLY_CODE = "";
    public static String RECEIVE_SENDER_NAME = "";
    public static String RECEIVE_SENDER_PICTURE = "";
    public static int DEPOSIT_AMOUNT = 0;
    public static String TRANSACTION_TYPE = "";
}
