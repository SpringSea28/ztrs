package com.ztrs.zgj;

import android.util.Log;

public class LogUtils {

    private static final boolean DEBUG = true;

    public static void LogE(String TAG, String msg) {
        Log.e(TAG, msg);
    }

    public static void LogI(String TAG, String msg) {
        Log.i(TAG, msg);
    }

    public static void LogD(String TAG, String msg) {
        Log.d(TAG, msg);
    }


    public static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        sb.append("[");
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.substring(0, sb.length() - 1) + "]";
    }

    public static String toHexString(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex;
    }
}
