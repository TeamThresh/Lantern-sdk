package com.lantern.lantern.Resource;

import android.os.Looper;

import com.lantern.lantern.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by YS on 2017-03-15.
 */

public class ThreadTrace {

    HashMap<String, List<StackTraceElement>> allThreads = new HashMap<>();

    public ThreadTrace() {
        this.allThreads = getAllThreadTracing();
    }

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
        HashMap<String, List<StackTraceElement>> allThreads = new HashMap<>();

        Map<Thread, StackTraceElement[]> temp = Thread.getAllStackTraces();
        Set<Thread> tempKey = temp.keySet();
        for (Thread th : tempKey) {
            Logger.d("KEY", th.getName());
            List<StackTraceElement> threadStack = new ArrayList<>();
            for (StackTraceElement element : temp.get(th)) {
                Logger.d("ELEMENTS", element.toString());
                threadStack.add(element);
            }
            allThreads.put(th.getName(), threadStack);
        }

        return allThreads;
    }

    public JSONArray toJsonArray() {
        JSONArray threadList = new JSONArray();

        try {
            Set<String> keySet = allThreads.keySet();
            for(String key : keySet) {
                JSONObject tempObj = new JSONObject();
                JSONArray stackTraceData = new JSONArray();
                tempObj.put("thread_name", key);

                int traceDepth =0;
                for(StackTraceElement element : allThreads.get(key)) {
                    JSONObject stackTraceLine = new JSONObject();
                    stackTraceLine.put("trace_depth", traceDepth++);
                    stackTraceLine.put("trace_content", element);
                    stackTraceData.put(stackTraceLine);
                }
                tempObj.put("trace_list", stackTraceData);
                threadList.put(tempObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return threadList;
    }
}
