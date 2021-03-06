package com.lantern.lantern.dump;

import com.lantern.lantern.RYLA;
import com.lantern.lantern.RylaInstrumentation;
import com.lantern.lantern.eventpath.EventPathItem;
import com.lantern.lantern.eventpath.EventPathManager;
import com.lantern.lantern.util.Logger;

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
        JSONArray eventPathData = new JSONArray();

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

            //crashData.put("system_service", new SystemServiceData().getDumpData());

            List<EventPathItem> eventPath = EventPathManager.getInstance(RYLA.getInstance().getContext()).getEventPathList();

            Logger.d("crash dump data", "event path num : " + eventPath.size());
            if (eventPath != null && eventPath.size() != 0) {
                for (EventPathItem item : eventPath) {
                    JSONObject eventPathItem = new JSONObject();
                    eventPathItem.put("datetime", item.getDatetime());
                    eventPathItem.put("class_name", item.getClassName());
                    eventPathItem.put("method_name", item.getMethodName());
                    eventPathItem.put("line_num", item.getLineNum());
                    eventPathItem.put("event_label", item.getEventLabel());

                    eventPathData.put(eventPathItem);
                }
            }

            crashData.put("event_path", eventPathData);

            System.out.println(crashData);

            crashData.put("res_data", RylaInstrumentation.getInstance().getShallowDump(new ShallowDumpData(true)).getDumpData());

            /*
            ThreadTrace threadTrace = new ThreadTrace();

            crashData.put("thread_trace", threadTrace.toJsonArray());
            */



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return crashData;
    }

}
