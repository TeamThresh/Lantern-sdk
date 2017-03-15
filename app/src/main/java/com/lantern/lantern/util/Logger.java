package com.lantern.lantern.util;

/**
 * Created by yky on 2017. 3. 15..
 */

public class Logger {
    private static final boolean PRINT_LOG = false;

    public Logger() {
    }

    public static void d(String tag, String msg) {
        if (PRINT_LOG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (PRINT_LOG) {
            android.util.Log.d(tag, msg, tr);
        }
    }
}
