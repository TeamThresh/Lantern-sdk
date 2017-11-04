package com.lantern.lantern;

import android.os.Build;

import com.lantern.lantern.util.Logger;

/**
 * Created by YS on 2017-02-01.
 */

public class DeviceInfo {
    private static final String TAG = "DeviceInfo";

    public static void sendDeviceInfo() {
        // TODO server connect
    }

    public static void printDeviceInfo() {
        Logger.i(TAG, "BOARD = " + Build.BOARD);
        Logger.i(TAG, "BRAND = " + Build.BRAND);
        Logger.i(TAG, "CPU_ABI = " + Build.CPU_ABI);
        Logger.i(TAG, "DEVICE = " + Build.DEVICE);
        Logger.i(TAG, "DISPLAY = " + Build.DISPLAY);
        Logger.i(TAG, "FINGERPRINT = " + Build.FINGERPRINT);
        Logger.i(TAG, "HOST = " + Build.HOST);
        Logger.i(TAG, "ID = " + Build.ID);
        Logger.i(TAG, "MANUFACTURER = " + Build.MANUFACTURER);
        Logger.i(TAG, "MODEL = " + Build.MODEL);
        Logger.i(TAG, "PRODUCT = " + Build.PRODUCT);
        Logger.i(TAG, "TAGS = " + Build.TAGS);
        Logger.i(TAG, "TYPE = " + Build.TYPE);
        Logger.i(TAG, "USER = " + Build.USER);
        Logger.i(TAG, "VERSION.RELEASE = " + Build.VERSION.RELEASE);
    }
}
