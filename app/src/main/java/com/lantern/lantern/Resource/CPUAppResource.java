package com.lantern.lantern.Resource;

import android.app.ActivityManager;
import android.util.Log;

import com.lantern.lantern.RYLA;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by YS on 2017-02-15.
 */

public class CPUAppResource implements Resource {
    private static final String TAG = "CPUAppResource";

    String processAppName;
    List<String> statList;

    public CPUAppResource() {

        // CPU INFO 중 top 사용하여 현재 앱 process 사용량 가져오기 위해
        // 실행중인 Task Process 가져오기
        ArrayList<ProcessInfo> taskProcessInfo = new ArrayList<>();
        ActivityManager activityManager = (ActivityManager) RYLA.getInstance().getContext().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager
                .getRunningAppProcesses();
        ActivityManager.RunningAppProcessInfo runningAppProcess;
        Iterator<ActivityManager.RunningAppProcessInfo> rAppProcess = procInfos.iterator();
        while (rAppProcess.hasNext()) {
            runningAppProcess = rAppProcess.next();
            ProcessInfo processInfo = new ProcessInfo(runningAppProcess.processName, runningAppProcess.pid);
            taskProcessInfo.add(processInfo);
            Log.d("TASK INFO", runningAppProcess.processName);
        }

        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/" + taskProcessInfo.get(0).pid + "/stat", "r");
            String load = reader.readLine();

            List<String> toks = new LinkedList<String>(Arrays.asList(load.split(" +")));  // Split on one or more spaces
            toks.remove(0); // PID 버림
            processAppName = toks.remove(0); // App 정보 버림

            statList = new ArrayList<>();
            for (String token : toks) {
                statList.add(token);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public List<String> toList() {
        return statList;
    }

    @Override
    public String toString() {
        String rtn = "\nstate\t\tppid\t\tpgrp\t\tsession\t\ttty_nr\t\ttpgid\t\tflags\t\tminflt\t\tcminflt\t\t" +
                "majflt\t\tcmajflt\t\tutime\t\tstime\t\tcutime\t\tcstime\t\tpriority\tnice\t\tnum_threads\t" +
                "itrealvalue\tstarttime\tvsize\t\trss_\t\trsslim\t\tstartcode\tendcode\t\tstartstack\t" +
                "kstkesp\t\tkstkeip\t\tsignal\t\tblocked\t\tsigignore\tsigcatch\twchan\t\tnswap\t\tcnswap\t\t" +
                "exit_signal\tprocessor\trt_priority\tpolicy\t\tdly_io_tic\tguest_time\t" +
                "cguest_time\tstart_data\tenddata\t\tstr_brk\t\targ_str\t\targ_end\t\tenv_str\t\t" +
                "env_end\t\text_cod\n";

        for (int i = 0; i < statList.size(); i++) {
            if (statList.get(i).length() > 7) {
                rtn += statList.get(i) + "\t";
            } else if (statList.get(i).length() > 3) {
                rtn += statList.get(i) + "\t\t";
            } else {
                rtn += statList.get(i) + "\t\t\t";
            }
        }

        return rtn;
    }
}
