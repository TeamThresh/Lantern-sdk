package com.lantern.lantern;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lantern.lantern.Resource.ActivityStack;
import com.lantern.lantern.Resource.CPUAppResource;
import com.lantern.lantern.Resource.CPUResource;
import com.lantern.lantern.Resource.MemoryResource;
import com.lantern.lantern.Resource.NetworkResource;
import com.lantern.lantern.Resource.StatResource;
import com.lantern.lantern.Resource.ThreadTrace;
import com.lantern.lantern.dump.DataUploadTask;
import com.lantern.lantern.dump.DumpFileManager;
import com.lantern.lantern.dump.ShallowDumpData;
import com.lantern.lantern.eventpath.EventPathManager;
import com.lantern.lantern.util.Logger;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.lantern.lantern.RYLA.isAppForeground;

/**
 * Created by YS on 2017-02-05.
 */

public class RylaInstrumentation extends Instrumentation {

    // window manager
    private WindowManager mWindowManager;

    // linear layout will use to detect touch event
    private LinearLayout touchLayout;

    private static int dumpTerm;
    private static int dumpCount = 0;

    public RylaInstrumentation() {
        SharedPreferences pref = RYLA.getInstance().getContext().getSharedPreferences("pref", MODE_PRIVATE);
        dumpTerm = pref.getInt("dump_term", 1000);
    }

    // Instrumentation 초기화 실행
    private static RylaInstrumentation rylaInstrumentation = new RylaInstrumentation();

    // Instrumentation Alive Check
    private boolean isResThreadAlive = false;

    public static RylaInstrumentation getInstance() {
        Logger.d("RylaInstrumentation", "getInstance");
        if (rylaInstrumentation == null) {
            rylaInstrumentation = new RylaInstrumentation();
        }
        return rylaInstrumentation;
    }

    public void execute() {
        Logger.d("RylaInstrumentation", "execute");
        isResThreadAlive = true;

        // TODO 보낼때 파일을 마무리하지않는게 좋을것 같음
        // TODO 아니면 저장 형태를 바꿀것
        new DataUploadTask(RYLA.getInstance().getContext()).execute();
        DumpFileManager.getInstance(RYLA.getInstance().getContext()).initDumpFile();
        dumpCount = 0;

        if (rylaInstrumentation != null) {
            rylaInstrumentation.start();
            startTouchTracing(RYLA.getInstance().getContext());
        }
    }

    public void stop() {
        isResThreadAlive = false;

        stopTouchTracing();

        if (rylaInstrumentation != null) {
            rylaInstrumentation.onDestroy();
        }
    }

    public boolean isResThreadAlive() {
        return isResThreadAlive;
    }

    // TODO RYLA 로 옮겨서 alive가 끝나면 instrumentation을 종료하고 새로 시작
    public void setResThreadAlive(boolean isAlive) {
        isResThreadAlive = isAlive;

        // 터치
        if (isAlive) {
            // TODO 보낼때 파일을 마무리하지않는게 좋을것 같음
            // TODO 아니면 저장 형태를 바꿀것
            new DataUploadTask(RYLA.getInstance().getContext()).execute();
            DumpFileManager.getInstance(RYLA.getInstance().getContext()).initDumpFile();

            dumpCount = 0;
            startTouchTracing(RYLA.getInstance().getContext());
        } else {
            stopTouchTracing();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Logger.d("eunchan", "MyInstrumentation::onDestory() : I'm being destroeyd!!! O.M.G.");
    }

    @Override
    public void onStart() {
        //WeakReference<ShallowDumpData> shallowDumpData = new WeakReference<>(new ShallowDumpData());
        ShallowDumpData shallowDumpData = new ShallowDumpData();
        while (true) {
            if (isResThreadAlive) {
                if (!isAppForeground()) {
                    isResThreadAlive = false;
                    stopTouchTracing();
                    continue;
                }

                if (dumpCount > 300) {  // 5분동안 실행
                    // 파일 재생성
                    DumpFileManager.getInstance(RYLA.getInstance().getContext()).initDumpFile();
                    // 카운트 초기화
                    dumpCount = 0;
                } else {
                    dumpCount++;
                }
                //Dataset for dump file
                Long dumpStartTime, dumpEndTime;
                float battery;
                NetworkResource networkInfo;
                CPUResource cpuInfo;
                CPUAppResource cpuAppInfo;
                MemoryResource memoryInfo;
                StatResource vmstatInfo;
                ActivityStack activityStackList;
                ThreadTrace stackTraceInfo;

                // 각각 걸리는 시간 계산
                long tempTime = 0;
                HashMap<String, Long> taskTime = new HashMap<>();

                // 시작시간
                dumpStartTime = System.currentTimeMillis();
                Logger.d("DUMP TIME", "====== "+ dumpStartTime +" =======");

                // dumpTerm 마다 쓰레드 트레이싱으로 문제가 되는 부분을 한번에 확인 가능
                tempTime = System.currentTimeMillis();
                stackTraceInfo = new ThreadTrace();
                taskTime.put("thread_dump_time", System.currentTimeMillis() - tempTime);

                activityStackList = new ActivityStack();

                // NETWORK USAGE INFO
                networkInfo = new NetworkResource();

                // MEMORY INFO
                tempTime = System.currentTimeMillis();
                memoryInfo = new MemoryResource();
                taskTime.put("memory_time", System.currentTimeMillis() - tempTime);

                // CPU INFO
                // top 방식 아닌 직접 가져오는 방식 사용
                tempTime = System.currentTimeMillis();
                cpuInfo = new CPUResource();
                taskTime.put("proc_stat_time", System.currentTimeMillis() - tempTime);
                tempTime = System.currentTimeMillis();
                cpuAppInfo = new CPUAppResource();
                taskTime.put("proc_pid_time", System.currentTimeMillis() - tempTime);

                // vmstat INFO
                tempTime = System.currentTimeMillis();
                vmstatInfo = new StatResource();
                taskTime.put("vmstat_time", System.currentTimeMillis() - tempTime);

                //battery
                battery = getBatteryPercent();

                // Logging
                Logger.d("NETWORK INFO", networkInfo.toString());
                memoryInfo.printMemoryInfo();
                Logger.d("CPU INFO", cpuInfo.toString());
                Logger.d("CPU APP INFO", cpuAppInfo.toString());
                Logger.d("VMSTAT INFO", vmstatInfo.toString());

                // 종료시간
                dumpEndTime = System.currentTimeMillis();
                Logger.d("DUMP TIME", "====== "+ dumpEndTime +" =======");
                shallowDumpData.setDumpData(
                        dumpStartTime,
                        dumpEndTime,
                        cpuInfo,
                        cpuAppInfo,
                        vmstatInfo,
                        memoryInfo,
                        activityStackList,
                        networkInfo,
                        stackTraceInfo,
                        taskTime,
                        battery
                );
                //save res dump file
                DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(shallowDumpData);
            }
            try {
                Thread.sleep(dumpTerm);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private float getBatteryPercent() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = RYLA.getInstance().getContext().registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return level / (float)scale * 100;
    }

    public void startTouchTracing(Context mApplication) {

    }

    public void stopTouchTracing() {

    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Logger.d("TOUCH INFO", event.toString());
            Logger.d("TOUCH INFO", event.getX()+"("+event.getRawX()+")" +"/"+ event.getY()+"("+event.getRawY()+")");
            return false;
        }
    };

    public static void markEventPath() {
        EventPathManager.getInstance(RYLA.getInstance().getContext()).createEventPathItem(2, "");
    }

    public static void markEventPath(String label) {
        Log.d("EventPath", "mark event path");
        EventPathManager.getInstance(RYLA.getInstance().getContext()).createEventPathItem(2, label);
    }
}
