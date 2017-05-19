package com.lantern.lantern.dump;

import com.lantern.lantern.Resource.ActivityStack;
import com.lantern.lantern.Resource.CPUAppResource;
import com.lantern.lantern.Resource.CPUResource;
import com.lantern.lantern.Resource.MemoryResource;
import com.lantern.lantern.Resource.NetworkResource;
import com.lantern.lantern.Resource.StatResource;
import com.lantern.lantern.Resource.ThreadTrace;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by yky on 2017. 2. 13..
 */

public class ShallowDumpData implements DumpData {
    private Long startTime;
    private Long endTime;
    private CPUResource cpuInfo;
    private CPUAppResource cpuAppInfo;
    private StatResource vmstatInfo;
    private MemoryResource memoryInfo;
    private ActivityStack activityStackInfo;
    private NetworkResource networkUsageInfo;
    private ThreadTrace stackTraceinfo;
    private HashMap<String, Long> taskTime;
    private float batteryPercent;
    private SystemServiceData systemServiceData;

    public ShallowDumpData() {

    }

    public void setDumpData(Long startTime, Long endTime, CPUResource cpuInfo,
                            CPUAppResource cpuAppInfo, StatResource vmstatInfo,
                            MemoryResource memoryInfo, ActivityStack activityStackInfo,
                            NetworkResource networkUsageInfo, ThreadTrace stackTraceinfo,
                            HashMap<String, Long> taskTime, float batteryPercent, SystemServiceData systemServiceData) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.cpuInfo = cpuInfo;
        this.cpuAppInfo = cpuAppInfo;
        this.vmstatInfo = vmstatInfo;
        this.memoryInfo = memoryInfo;
        this.activityStackInfo = activityStackInfo;
        this.networkUsageInfo = networkUsageInfo;
        this.stackTraceinfo = stackTraceinfo;
        this.taskTime = taskTime;
        this.batteryPercent = batteryPercent;
        this.systemServiceData = systemServiceData;
    }



    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public CPUResource getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(CPUResource cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public CPUAppResource getCpuAppInfo() {
        return cpuAppInfo;
    }

    public void setCpuAppInfo(CPUAppResource cpuAppInfo) {
        this.cpuAppInfo = cpuAppInfo;
    }

    public StatResource getVmstatInfo() {
        return vmstatInfo;
    }

    public void setVmstatInfo(StatResource vmstatInfo) {
        this.vmstatInfo = vmstatInfo;
    }

    public MemoryResource getMemoryInfo() {
        return memoryInfo;
    }

    public void setMemoryInfo(MemoryResource memoryInfo) {
        this.memoryInfo = memoryInfo;
    }

    public ActivityStack getActivityStackInfo() {
        return activityStackInfo;
    }

    public void setActivityStackInfo(ActivityStack activityStackInfo) {
        this.activityStackInfo = activityStackInfo;
    }

    public NetworkResource getNetworkUsageInfo() {
        return networkUsageInfo;
    }

    public void setNetworkUsageInfo(NetworkResource networkUsageInfo) {
        this.networkUsageInfo = networkUsageInfo;
    }

    public ThreadTrace getStackTraceinfo() {
        return stackTraceinfo;
    }

    public void setStackTraceinfo(ThreadTrace stackTraceinfo) {
        this.stackTraceinfo = stackTraceinfo;
    }

    public float getBatteryPercent() { return batteryPercent; }

    public void setBatteryPercent(float batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    public SystemServiceData getSystemServiceData() {
        return systemServiceData;
    }

    public void setSystemServiceData(SystemServiceData systemServiceData) {
        this.systemServiceData = systemServiceData;
    }

    @Override
    //type이 res인 dump JSON object 생성하기
    //crash dump data에서도 사용됨
    public JSONObject getDumpData() {
        return getDumpData(false);
    }

    public JSONObject getDumpData(boolean forCrash) {
        JSONObject resData = new JSONObject();
        JSONObject durationData = new JSONObject();
        JSONObject timeData = new JSONObject();

        try {
            //type
            if(!forCrash)
                resData.put("type", "res");

            //duration_time
            durationData.put("start", getStartTime());
            durationData.put("end", getEndTime());
            resData.put("duration_time", durationData);

            // task time
            for (String key : taskTime.keySet()) {
                timeData.put(key, taskTime.get(key));
            }
            resData.put("task_time", timeData);

            // parse os data
            resData.put("os", getParsedOsData());

            // parse app data
            resData.put("app", getParsedAppData());

            resData.put("system_service", getSystemServiceData().getDumpData());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resData;
    }

    private JSONObject getParsedOsData() {
        JSONObject osData = new JSONObject();

        try {
            //cpu
            osData.put("cpu", getCpuInfo().toJson());

            // vmstat
            osData.put("vmstat", getVmstatInfo().toJson());

            //battery
            // TODO 권한 필요
            //osData.put("battery", 10);
            osData.put("battery", getBatteryPercent());

            //network_usage
            osData.put("network_usage", getNetworkUsageInfo().toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return osData;
    }

    private JSONObject getParsedAppData() {
        JSONObject appData = new JSONObject();

        // TODO toJson()이나 toRes() 으로 다 받을 수 있으면 좋을거 같은데...
        try {
            // cpu app
            appData.put("cpu_app", getCpuAppInfo().toJson());

            //memory
            appData.put("memory", getMemoryInfo().toJson());

            //activity_stack
            appData.put("activity_stack", getActivityStackInfo().toJsonArray());

            //thread_trace
            appData.put("thread_trace", getStackTraceinfo().toJsonArray());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return appData;

    }


}
