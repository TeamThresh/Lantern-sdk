package com.lantern.lantern;

import com.lantern.lantern.dump.CrashDumpData;
import com.lantern.lantern.dump.DumpFileManager;
import com.lantern.lantern.util.Logger;

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

        Logger.d("CRASH", stacktrace.toString());

        CrashDumpData crashDumpData = new CrashDumpData(stacktrace.toString());
        DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                crashDumpData
        );


        this.defaultExceptionHandler.uncaughtException(t, e);
    }
}
