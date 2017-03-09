package com.lantern.lantern.Resource;

/**
 * Created by YS on 2017-02-14.
 */

public class ProcessInfo {
    String processName;
    int pid;

    public ProcessInfo(String processName, int pid) {
        this.pid = pid;
        this.processName = processName;
    }
}
