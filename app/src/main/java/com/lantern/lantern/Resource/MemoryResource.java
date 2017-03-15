package com.lantern.lantern.Resource;

import android.os.Debug;
import android.util.Log;

import com.lantern.lantern.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YS on 2017-01-25.
 */

public class MemoryResource implements Resource {
    private static final String TAG = "MemoryResource";

    private long natHeapSize;
    private long natHeapFreeSize;
    private long pss;
    private int loadedClassCount;
    private Debug.MemoryInfo memoryInfo;

    List<Long> memoryInfoList = new ArrayList<>();

    public MemoryResource() {
        memoryInfoList.clear();

        this.natHeapSize = Debug.getNativeHeapSize();
        this.natHeapFreeSize = Debug.getNativeHeapFreeSize();
        this.pss = Debug.getPss();
        this.loadedClassCount = Debug.getLoadedClassCount();

        this.memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
    }

    public void printMemoryInfo() {
        Logger.d(TAG, "========= START memory info =========");

        Logger.d("MEMORY INFO", "Max Memory: "+Runtime.getRuntime().maxMemory());
        Logger.d("MEMORY INFO", "Total Memory: "+Runtime.getRuntime().totalMemory());
        Logger.d("MEMORY INFO", "Free Memory: "+Runtime.getRuntime().freeMemory());
        Logger.d("MEMORY INFO", "Allocated Memory: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

        //Logger.d(TAG, ""+memoryInfo.getTotalSwappablePss());
        Logger.d(TAG, "TotalSharedDirty: "+memoryInfo.getTotalSharedDirty());
        Logger.d(TAG, "TotalPrivateDirty: "+memoryInfo.getTotalPrivateDirty());
        Logger.d(TAG, "TotalPss: "+memoryInfo.getTotalPss());

        Logger.d(TAG, "dalvikPrivateDirty: "+memoryInfo.dalvikPrivateDirty);
        Logger.d(TAG, "dalvikPss: "+memoryInfo.dalvikPss);
        Logger.d(TAG, "dalvikSharedDirty: "+memoryInfo.dalvikSharedDirty);

        Logger.d(TAG, "nativePrivateDirty: "+memoryInfo.nativePrivateDirty);
        Logger.d(TAG, "nativePss: "+memoryInfo.nativePss);
        Logger.d(TAG, "nativeSharedDirty: "+memoryInfo.nativeSharedDirty);

        Logger.d(TAG, "otherPrivateDirty: "+memoryInfo.otherPrivateDirty);
        Logger.d(TAG, "otherPss: "+memoryInfo.otherPss);
        Logger.d(TAG, "otherSharedDirty: "+memoryInfo.otherSharedDirty);

        //Logger.d(TAG, ""+memoryInfo.getTotalPrivateClean());
        //Logger.d(TAG, ""+memoryInfo.getTotalSharedClean());

        Logger.d(TAG, "========= END memory info =========");
    }

    @Override
    public List<Long> toList() {

        memoryInfoList.add(Runtime.getRuntime().maxMemory());
        memoryInfoList.add(Runtime.getRuntime().totalMemory());
        memoryInfoList.add(Runtime.getRuntime().freeMemory());
        memoryInfoList.add(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        return memoryInfoList;
    }

    @Override
    public String toString() {
        return null;
    }
}
