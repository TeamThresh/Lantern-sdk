package com.lantern.lantern.dump;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by YS on 2017-03-15.
 */

public class CrashDumpData implements DumpData {
    private List<StackTraceElement> stackTraceinfo;
    private String stackTraceRaw = "";

    public CrashDumpData(List<StackTraceElement> stackTraceinfo) {
        this.stackTraceinfo = stackTraceinfo;
    }

    public CrashDumpData(String stackTraceRaw) {
        this.stackTraceRaw = stackTraceRaw;
    }

    public List<StackTraceElement> getStackTraceinfo() {
        return stackTraceinfo;
    }

    public void setStackTraceinfo(List<StackTraceElement> stackTraceinfo) {
        this.stackTraceinfo = stackTraceinfo;
    }

    @Override
    public JSONObject getDumpData() {
        JSONObject crashData = new JSONObject();
        JSONArray stacktraceData = new JSONArray();

        try {
            //type
            crashData.put("type", "crash");

            //crash time
            crashData.put("crash_time", System.currentTimeMillis()+"");

            if (stackTraceinfo != null) {
                // stacktrace dump
                for (StackTraceElement element : getStackTraceinfo()) {
                    stacktraceData.put(element);
                }
                crashData.put("stacktrace", stacktraceData);
            } else {
                crashData.put("stacktrace", stackTraceRaw);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return crashData;
    }
}
