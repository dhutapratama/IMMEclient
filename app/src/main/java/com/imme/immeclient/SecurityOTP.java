package com.imme.immeclient;

import android.text.TextUtils;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by sysadm@ilccourse.com on 12/11/2015.
 */

public class SecurityOTP {
    static char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    //5GwcTzO0ODM6eSV3s66PJjeedlEvxWc9
    private String iv = "fedcba9876543210";//Dummy iv (CHANGE IT!)
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    private String SecretKey = GlobalVariable.OTP_KEY + "qwerty";//Dummy secretKey (CHANGE IT!)

    public SecurityOTP()
    {
        ivspec = new IvParameterSpec(iv.getBytes());

        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception
    {
        if(text == null || text.length() == 0)
            throw new Exception("Empty string");

        byte[] encrypted = null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e)
        {
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
    }

    public static String bytesToHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }


    private static String padString(String source)
    {
        char paddingChar = 0;
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++)
        {
            source += paddingChar;
        }

        return source;
    }

    public static String tba_key(){
        long unixTime = System.currentTimeMillis() / 1000L;
        long answer = (int)(long) unixTime;
        for (int i = 0; i < 5; i++) {
            String operator_code = TextUtils.substring(GlobalVariable.TBA_ALGORITHM, i, i+1);
            String factor = TextUtils.substring(GlobalVariable.TBA_ALGORITHM, i + 5, i+6);

            if (operator_code.equals("1")) {
                Log.w(Integer.toString(i), "plus(" + answer + ", " + factor + ")");
                answer +=  Integer.parseInt(factor);
            } else if (operator_code.equals("2")) {
                Log.w(Integer.toString(i), "minus(" + answer + ", " + factor + ")");
                answer -=  Integer.parseInt(factor);
            } else if (operator_code.equals("3")) {
                Log.w(Integer.toString(i), "minus(" + answer + ", " + factor + ")");
                answer *=  Integer.parseInt(factor);
            }
        }

        Log.w("Answer", String.valueOf(answer));
        return String.valueOf(answer);
    }

    public static String cba_key(){
        Integer answer = Integer.parseInt(GlobalVariable.CBA_COUNTER);

        for (int i = 0; i < 5; i++) {
            String operator_code = TextUtils.substring(GlobalVariable.CBA_ALGORITHM, i, i + 1);
            String factor = TextUtils.substring(GlobalVariable.CBA_ALGORITHM, i + 5, i + 6);
            if (operator_code.equals("1")) {
                answer = plus(answer, factor);
            } else if (operator_code.equals("2")) {
                answer = times(answer, factor);
            }
        }
        return answer.toString();
    }

    private static int plus(Integer a, String b) {
        return a + Integer.parseInt(b);
    }

    private static int minus(Integer a, String b) {
        return a - Integer.parseInt(b);
    }

    private static int times(Integer a, String b) {
        return a * Integer.parseInt(b);
    }
}
