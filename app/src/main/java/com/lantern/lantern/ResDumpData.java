package com.lantern.lantern;

import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YS on 2017-01-25.
 */

public class ResDumpData {
    private static final String TAG = "ResDumpData";

    private long natHeapSize;
    private long natHeapFreeSize;
    private long pss;
    private int loadedClassCount;
    private Debug.MemoryInfo memoryInfo;

    public ResDumpData(long natHeapSize, long natHeapFreeSize, long pss, int loadedClassCount, Debug.MemoryInfo memoryInfo) {
        this.natHeapSize = natHeapSize;
        this.natHeapFreeSize = natHeapFreeSize;
        this.pss = pss;
        this.loadedClassCount = loadedClassCount;
        this.memoryInfo = memoryInfo;
    }

    public void printMemoryInfo() {
        Log.d(TAG, "========= START memory info =========");

        Log.d("MEMORY INFO", "Max Memory: "+Runtime.getRuntime().maxMemory());
        Log.d("MEMORY INFO", "Total Memory: "+Runtime.getRuntime().totalMemory());
        Log.d("MEMORY INFO", "Free Memory: "+Runtime.getRuntime().freeMemory());
        Log.d("MEMORY INFO", "Allocated Memory: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

        //Log.d(TAG, ""+memoryInfo.getTotalSwappablePss());
        Log.d(TAG, "TotalSharedDirty: "+memoryInfo.getTotalSharedDirty());
        Log.d(TAG, "TotalPrivateDirty: "+memoryInfo.getTotalPrivateDirty());
        Log.d(TAG, "TotalPss: "+memoryInfo.getTotalPss());

        Log.d(TAG, "dalvikPrivateDirty: "+memoryInfo.dalvikPrivateDirty);
        Log.d(TAG, "dalvikPss: "+memoryInfo.dalvikPss);
        Log.d(TAG, "dalvikSharedDirty: "+memoryInfo.dalvikSharedDirty);

        Log.d(TAG, "nativePrivateDirty: "+memoryInfo.nativePrivateDirty);
        Log.d(TAG, "nativePss: "+memoryInfo.nativePss);
        Log.d(TAG, "nativeSharedDirty: "+memoryInfo.nativeSharedDirty);

        Log.d(TAG, "otherPrivateDirty: "+memoryInfo.otherPrivateDirty);
        Log.d(TAG, "otherPss: "+memoryInfo.otherPss);
        Log.d(TAG, "otherSharedDirty: "+memoryInfo.otherSharedDirty);

        //Log.d(TAG, ""+memoryInfo.getTotalPrivateClean());
        //Log.d(TAG, ""+memoryInfo.getTotalSharedClean());

        Log.d(TAG, "========= END memory info =========");
    }

    public List<Long> getMemoryInfoForDump() {
        List<Long> memoryInfoList = new ArrayList<>();

        memoryInfoList.add(Runtime.getRuntime().maxMemory());
        memoryInfoList.add(Runtime.getRuntime().totalMemory());
        memoryInfoList.add(Runtime.getRuntime().freeMemory());
        memoryInfoList.add(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        return memoryInfoList;
    }
}
