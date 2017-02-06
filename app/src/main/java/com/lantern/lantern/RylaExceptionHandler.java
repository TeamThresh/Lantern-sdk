package com.lantern.lantern;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by YS on 2017-01-31.
 */

public class RylaExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;

    public RylaExceptionHandler(Thread.UncaughtExceptionHandler pDefaultExceptionHandler) {
        this.defaultExceptionHandler = pDefaultExceptionHandler;
    }

    public void uncaughtException(Thread t, Throwable e) {
        StringWriter stacktrace = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stacktrace);
        e.printStackTrace(printWriter);

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        Log.d("CRASH", stacktrace.toString());
        this.defaultExceptionHandler.uncaughtException(t, e);
    }
}
