package com.lantern.lantern.network;

/**
 * Created by YS on 2017-04-16.
 */

public class NetworkCallbackData {
    Long startTime, endTime;
    String hostName, status;

    public NetworkCallbackData() {

    }

    public NetworkCallbackData(long startTime, long endTime, String hostName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.hostName = hostName;
    }

    public NetworkCallbackData(String status) {
        this.status = status;
    }

    public boolean isComplete() {
        if (startTime != null
        && endTime != null
        && hostName != null
        && status != null) {
            return true;
        }
        return false;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
