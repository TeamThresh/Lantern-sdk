package com.lantern.lantern.dump;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yky on 2017. 2. 13..
 */

public class ShallowDumpData implements DumpData {
    private Long startTime;
    private Long endTime;
    private List<Long> cpuInfo;
    private List<Long> memoryInfo;
    private List<String> activityStackInfo;
    private List<String> networkUsageInfo;
    private List<String> stackTraceinfo;


    public ShallowDumpData(Long startTime, Long endTime, List<Long> cpuInfo,
                           List<Long> memoryInfo, List<String> activityStackInfo,
                           List<String> networkUsageInfo, List<String> stackTraceinfo) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.cpuInfo = cpuInfo;
        this.memoryInfo = memoryInfo;
        this.activityStackInfo = activityStackInfo;
        this.networkUsageInfo = networkUsageInfo;
        this.stackTraceinfo = stackTraceinfo;
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

    public List<Long> getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(List<Long> cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public List<Long> getMemoryInfo() {
        return memoryInfo;
    }

    public void setMemoryInfo(List<Long> memoryInfo) {
        this.memoryInfo = memoryInfo;
    }

    public List<String> getActivityStackInfo() {
        return activityStackInfo;
    }

    public void setActivityStackInfo(List<String> activityStackInfo) {
        this.activityStackInfo = activityStackInfo;
    }

    public List<String> getNetworkUsageInfo() {
        return networkUsageInfo;
    }

    public void setNetworkUsageInfo(List<String> networkUsageInfo) {
        this.networkUsageInfo = networkUsageInfo;
    }

    public List<String> getStackTraceinfo() {
        return stackTraceinfo;
    }

    public void setStackTraceinfo(List<String> stackTraceinfo) {
        this.stackTraceinfo = stackTraceinfo;
    }

    @Override
    //type이 res인 dump JSON object 생성하기
    public JSONObject getDumpData() {
        JSONObject resData = new JSONObject();
        JSONObject durationData = new JSONObject();
        JSONObject cpuData = new JSONObject();
        JSONObject memoryData = new JSONObject();
        JSONArray activiyData = new JSONArray();
        JSONObject networkData = new JSONObject();

        try {
            //type
            resData.put("type", "res");

            //duration_time
            durationData.put("start", getStartTime());
            durationData.put("end", getEndTime());
            resData.put("duration_time", durationData);

            //cpu
            String[] labelCpu = {"user", "nice", "system", "idle", "iowait", "irq", "softirq", "steal", "guest", "guest_nice"};
            for(int i=0;i<labelCpu.length;i++) {
                try {
                    cpuData.put(labelCpu[i], getCpuInfo().get(i));
                } catch(IndexOutOfBoundsException e) {
                    cpuData.put(labelCpu[i], -1);
                }
            }
            resData.put("cpu", cpuData);

            //memory
            String[] labelMemory = {"max", "total", "alloc", "free"};
            for(int i=0;i<labelMemory.length;i++) {
                memoryData.put(labelMemory[i], getMemoryInfo().get(i));
            }
            resData.put("memory", memoryData);

            //battery
            resData.put("battery", "battery stat");

            //activity_stack
            for(int i=0;i<getActivityStackInfo().size();i++) {
                activiyData.put(getActivityStackInfo().get(i));
            }
            resData.put("activity_stack", activiyData);

            //network_usage
            String[] labelNetwork = {"type", "rx", "tx"};
            for(int i=0;i<labelNetwork.length;i++) {
                if(i==0) {
                    networkData.put(labelNetwork[i], getNetworkUsageInfo().get(i));
                } else {
                    networkData.put(labelNetwork[i], Long.parseLong(getNetworkUsageInfo().get(i)));
                }
            }
            resData.put("network_usage", networkData);

            //thread_trace
            resData.put("thread_trace", "traced list");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resData;
    }
}
