package com.lantern.lantern.Resource;

import android.util.Log;

import com.lantern.lantern.util.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YS on 2017-02-15.
 */

public class CPUResource implements Resource {
    private static final String TAG = "CPUResource";
    static List<Long> usages1 = new ArrayList<>();

    List<Long> res;

    public CPUResource() {
        List<Long> cpuInfoList = new ArrayList<>();

        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            List<String> toks = new LinkedList<String>(Arrays.asList(load.split(" +")));  // Split on one or more spaces
            toks.remove(0);

            List<Long> usages2 = new ArrayList<>();
            for (String token : toks) {
                Logger.d(TAG, token);
                usages2.add(Long.parseLong(token));
            }

            if (usages1.isEmpty()) {
                Logger.d(TAG, "항상 여기");
                usages1 = usages2;
                cpuInfoList.add(-1L);
                this.res = cpuInfoList;
                return;
            }

            for (int i = 0; i < usages1.size(); i++) {
                cpuInfoList.add(usages2.get(i) - usages1.get(i));
                Logger.d(TAG, "아님 여기");
                Logger.d(TAG, usages2.get(i) +"-"+usages1.get(i) + "="+(usages2.get(i) - usages1.get(i)));
            }

            usages1 = usages2;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        this.res = cpuInfoList;
    }

    @Override
    public List<Long> toList() {
        return this.res;
    }

    @Override
    public String toString() {
        // user nice system idle iowait  irq  softirq steal guest guest_nice
        String rtn = "\nuser\tnice\tsystem\tidle\tiowait\tirq\tsoftirq\tsteal\tguest\tguest_nice\n";

        for (int i = 0; i < res.size(); i++) {
            rtn += res.get(i) + "\t\t";
        }

        return rtn;
    }
}
