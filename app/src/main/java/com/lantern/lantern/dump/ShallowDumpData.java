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
    private List<String> cpuAppInfo;
    private List<String> vmstatInfo;
    private List<Long> memoryInfo;
    private List<String> activityStackInfo;
    private List<String> networkUsageInfo;
    private List<String> stackTraceinfo;


    public ShallowDumpData(Long startTime, Long endTime, List<Long> cpuInfo,
                           List<String> cpuAppInfo, List<String> vmstatInfo,
                           List<Long> memoryInfo, List<String> activityStackInfo,
                           List<String> networkUsageInfo, List<String> stackTraceinfo) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.cpuInfo = cpuInfo;
        this.cpuAppInfo = cpuAppInfo;
        this.vmstatInfo = vmstatInfo;
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

    public List<String> getCpuAppInfo() {
        return cpuAppInfo;
    }

    public void setCpuAppInfo(List<String> cpuAppInfo) {
        this.cpuAppInfo = cpuAppInfo;
    }

    public List<String> getVmstatInfo() {
        return vmstatInfo;
    }

    public void setVmstatInfo(List<String> vmstatInfo) {
        this.vmstatInfo = vmstatInfo;
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

        try {
            //type
            resData.put("type", "res");

            //duration_time
            durationData.put("start", getStartTime());
            durationData.put("end", getEndTime());
            resData.put("duration_time", durationData);

            // parse os data
            resData.put("os", getParsedOsData());

            // parse app data
            resData.put("app", getParsedAppData());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resData;
    }

    private JSONObject getParsedOsData() {
        JSONObject osData = new JSONObject();
        JSONObject cpuData = new JSONObject();
        JSONObject vmstatData = new JSONObject();
        JSONObject networkData = new JSONObject();

        try {
            //cpu
            String[] labelCpu = {"user", "nice", "system", "idle", "iowait", "irq", "softirq", "steal", "guest", "guest_nice"};
            for(int i=0;i<labelCpu.length;i++) {
                try {
                    cpuData.put(labelCpu[i], getCpuInfo().get(i));
                } catch(IndexOutOfBoundsException e) {
                    cpuData.put(labelCpu[i], -1);
                }
            }
            osData.put("cpu", cpuData);

            // vmstat
            String[] labelVmStat= {"r","b","swpd","free","buff","cache","si","so","bi","bo","in","cs","us","sy","id","wa"};
            for(int i=0;i<labelVmStat.length;i++) {
                try {
                    vmstatData.put(labelVmStat[i], getVmstatInfo().get(i));
                } catch(IndexOutOfBoundsException e) {
                    vmstatData.put(labelVmStat[i], -1);
                }
            }
            osData.put("vmstat", vmstatData);

            //battery
            osData.put("battery", 10);

            //network_usage
            String[] labelNetwork = {"type", "rx", "tx"};
            for(int i=0;i<labelNetwork.length;i++) {
                if(i==0) {
                    networkData.put(labelNetwork[i], getNetworkUsageInfo().get(i));
                } else {
                    networkData.put(labelNetwork[i], Long.parseLong(getNetworkUsageInfo().get(i)));
                }
            }
            osData.put("network_usage", networkData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return osData;
    }

    private JSONObject getParsedAppData() {
        JSONObject appData = new JSONObject();
        JSONObject cpuAppData = new JSONObject();
        JSONObject memoryData = new JSONObject();
        JSONArray activiyData = new JSONArray();

        try {
            // cpu app
            String[] labelCpuApp = {"state","ppid","pgrp","session","tty_nr","tpgid","flags","minflt","cminflt" +
                    "majflt","cmajflt","utime","stime","cutime","cstime","priority","nice","num_threads" +
                    "itrealvalue","starttime","vsize","rss_","rsslim","startcode","endcode","startstack" +
                    "kstkesp","kstkeip","signal","blocked","sigignore","sigcatch","wchan","nswap","cnswap" +
                    "exit_signal","processor","rt_priority","policy","dly_io_tic","guest_time" +
                    "cguest_time","start_data","enddata","str_brk","arg_str","arg_end","env_str" +
                    "env_end","ext_cod"};
            for(int i=0;i<labelCpuApp.length;i++) {
                cpuAppData.put(labelCpuApp[i], getCpuAppInfo().get(i));
            }
            appData.put("cpu_app", cpuAppData);

            //memory
            String[] labelMemory = {"max", "total", "alloc", "free"};
            for(int i=0;i<labelMemory.length;i++) {
                memoryData.put(labelMemory[i], getMemoryInfo().get(i));
            }
            appData.put("memory", memoryData);


            //activity_stack
            for(int i=0;i<getActivityStackInfo().size();i++) {
                activiyData.put(getActivityStackInfo().get(i));
            }
            appData.put("activity_stack", activiyData);


            //thread_trace
            appData.put("thread_trace", "traced list");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return appData;

    }
}
