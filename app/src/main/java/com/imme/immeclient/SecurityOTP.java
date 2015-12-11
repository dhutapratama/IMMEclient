package com.imme.immeclient;

import android.text.TextUtils;

/**
 * Created by sysadm@ilccourse.com on 12/11/2015.
 */
public class SecurityOTP {
    public static String tba_key(){
        long unixTime = System.currentTimeMillis() / 1000L;
        Integer answer = (int)(long) unixTime;
        for (int i = 0; i < 5; i++) {
            String operator_code = TextUtils.substring(GlobalVariable.TBA_ALGORITHM, i, i+1);
            String factor = TextUtils.substring(GlobalVariable.TBA_ALGORITHM, i + 5, i+6);

            if (operator_code.equals("1")) {
                answer = plus(answer, factor);
            } else if (operator_code.equals("2")) {
                answer = minus(answer, factor);
            } else if (operator_code.equals("3")) {
                answer = times(answer, factor);
            }
        }
        return answer.toString();
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
