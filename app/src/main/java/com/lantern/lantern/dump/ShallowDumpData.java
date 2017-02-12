package com.lantern.lantern.dump;

import java.util.List;

/**
 * Created by yky on 2017. 2. 13..
 */

public class ShallowDumpData {
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
}
