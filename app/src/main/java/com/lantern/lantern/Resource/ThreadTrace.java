package com.lantern.lantern.Resource;

import android.os.Looper;
import android.util.Log;

import com.lantern.lantern.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by YS on 2017-03-15.
 */

public class ThreadTrace {

    public void getMainThreadTracing() {
        List<String> stackTraceLines = new ArrayList<>();

        Thread t = Looper.getMainLooper().getThread();
        StackTraceElement[] stackTraceList = t.getStackTrace();
        for(StackTraceElement stackTrace : stackTraceList) {
            Logger.d("STACK TRACE", stackTrace.toString());
            stackTraceLines.add(stackTrace.toString());
        }

        //return stackTraceLines;
    }

    public HashMap<String, List<StackTraceElement>> getAllThreadTracing() {
        HashMap<String, List<StackTraceElement>> tempThread = new HashMap<>();

        Map<Thread, StackTraceElement[]> temp = Thread.getAllStackTraces();
        Set<Thread> tempKey = temp.keySet();
        for (Thread th : tempKey) {
            Logger.d("KEY", th.getName());
            List<StackTraceElement> threadStack = new ArrayList<>();
            for (StackTraceElement element : temp.get(th)) {
                Logger.d("ELEMENTS", element.toString());
                threadStack.add(element);
            }
            tempThread.put(th.getName(), threadStack);
        }


        return tempThread;
    }
}
