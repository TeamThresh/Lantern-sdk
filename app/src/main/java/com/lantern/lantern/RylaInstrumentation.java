package com.lantern.lantern;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lantern.lantern.Resource.CPUAppResource;
import com.lantern.lantern.Resource.CPUResource;
import com.lantern.lantern.Resource.MemoryResource;
import com.lantern.lantern.Resource.NetworkResource;
import com.lantern.lantern.Resource.StatResource;
import com.lantern.lantern.Resource.ThreadTrace;
import com.lantern.lantern.dump.DataUploadTask;
import com.lantern.lantern.dump.DumpFileManager;
import com.lantern.lantern.dump.ShallowDumpData;
import com.lantern.lantern.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    static List<Long> usages1 = new ArrayList<>();

    private static int dumpTerm;

    public RylaInstrumentation() {
        SharedPreferences pref = RYLA.getInstance().getContext().getSharedPreferences("pref", MODE_PRIVATE);
        dumpTerm = pref.getInt("dump_term", 10000);
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

    public void excute() {
        Logger.d("RylaInstrumentation", "excute");
        isResThreadAlive = true;

        new DataUploadTask(RYLA.getInstance().getContext()).execute();

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

    public void setResThreadAlive(boolean isAlive) {
        isResThreadAlive = isAlive;

        // 터치
        if (isAlive) {
            new DataUploadTask(RYLA.getInstance().getContext()).execute();
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
        while (true) {
            if (isResThreadAlive) {
                if (!isAppForeground()) {
                    isResThreadAlive = false;
                    stopTouchTracing();
                    continue;
                }

                //Dataset for dump file
                Long dumpStartTime, dumpEndTime;
                NetworkResource networkInfo;
                CPUResource cpuInfo;
                CPUAppResource cpuAppInfo;
                MemoryResource memoryInfo;
                StatResource vmstatInfo;
                List<String> activityStackList = new ArrayList<>();
                HashMap<String, List<StackTraceElement>> stackTraceInfo;

                // 각각 걸리는 시간 계산
                long tempTime = 0;
                HashMap<String, Long> taskTime = new HashMap<>();

                // 시작시간
                dumpStartTime = System.currentTimeMillis();
                Logger.d("DUMP TIME", "====== "+ dumpStartTime +" =======");

                // dumpTerm 마다 쓰레드 트레이싱으로 문제가 되는 부분을 한번에 확인 가능
                tempTime = System.currentTimeMillis();
                stackTraceInfo = new ThreadTrace().getAllThreadTracing();
                taskTime.put("thread_dump_time", System.currentTimeMillis() - tempTime);

                for (Activity activity : RYLA.getInstance().getActivityList()) {
                    Logger.d("ACTIVITIES", activity.getClass().getSimpleName());
                    activityStackList.add(activity.getClass().getSimpleName());
                }

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

                // Logging
                Logger.d("NETWORK INFO", networkInfo.toString());
                memoryInfo.printMemoryInfo();
                Logger.d("CPU INFO", cpuInfo.toString());
                Logger.d("CPU APP INFO", cpuAppInfo.toString());
                Logger.d("VMSTAT INFO", vmstatInfo.toString());

                // 종료시간
                dumpEndTime = System.currentTimeMillis();
                Logger.d("DUMP TIME", "====== "+ dumpEndTime +" =======");

                //save res dump file
                DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                        new ShallowDumpData(
                                dumpStartTime,
                                dumpEndTime,
                                cpuInfo.toList(),
                                cpuAppInfo.toList(),
                                vmstatInfo.toList(),
                                memoryInfo.toList(),
                                activityStackList,
                                networkInfo,
                                stackTraceInfo,
                                taskTime
                        )
                );
            }
            try {
                Thread.sleep(dumpTerm);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startTouchTracing(Context mApplication) {
        // 이방법으로 하면 ACTION 의 이름을 가져올수 없음

//        mWindowManager = (WindowManager) mApplication.getSystemService(WINDOW_SERVICE);
//        touchLayout = new LinearLayout(mApplication);
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, WindowManager.LayoutParams.MATCH_PARENT);
//        touchLayout.setLayoutParams(params);
//        touchLayout.setOnTouchListener(touchListener);
//
//
//        WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
//                1,  // width
//                1,  // height
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSPARENT
//        );
//        params.gravity = Gravity.LEFT | Gravity.TOP;
//        mWindowManager.addView(touchLayout, params2);
    }

    public void stopTouchTracing() {
//        if(mWindowManager != null) {
//            if(touchLayout != null) mWindowManager.removeView(touchLayout);
//        }
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Logger.d("TOUCH INFO", event.toString());
            Logger.d("TOUCH INFO", event.getX()+"("+event.getRawX()+")" +"/"+ event.getY()+"("+event.getRawY()+")");
            return false;
        }
    };
}
