package com.lantern.lantern;

import android.os.Build;
import android.util.Log;

/**
 * Created by YS on 2017-02-01.
 */

public class DeviceInfo {
    private static final String TAG = "DeviceInfo";
    public DeviceInfo() {
    }

    public static void sendDeviceInfo() {
        // TODO server connect
    }

    public static void printDeviceInfo() {
        Log.i(TAG, "BOARD = " + Build.BOARD);
        Log.i(TAG, "BRAND = " + Build.BRAND);
        Log.i(TAG, "CPU_ABI = " + Build.CPU_ABI);
        Log.i(TAG, "DEVICE = " + Build.DEVICE);
        Log.i(TAG, "DISPLAY = " + Build.DISPLAY);
        Log.i(TAG, "FINGERPRINT = " + Build.FINGERPRINT);
        Log.i(TAG, "HOST = " + Build.HOST);
        Log.i(TAG, "ID = " + Build.ID);
        Log.i(TAG, "MANUFACTURER = " + Build.MANUFACTURER);
        Log.i(TAG, "MODEL = " + Build.MODEL);
        Log.i(TAG, "PRODUCT = " + Build.PRODUCT);
        Log.i(TAG, "TAGS = " + Build.TAGS);
        Log.i(TAG, "TYPE = " + Build.TYPE);
        Log.i(TAG, "USER = " + Build.USER);
        Log.i(TAG, "VERSION.RELEASE = " + Build.VERSION.RELEASE);
    }
}
