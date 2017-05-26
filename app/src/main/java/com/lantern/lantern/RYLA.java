package com.lantern.lantern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.lantern.lantern.dump.ActivityRenderData;
import com.lantern.lantern.dump.DumpFileManager;
import com.lantern.lantern.network.LanternSocketFactory;
import com.lantern.lantern.util.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.SSLSocket;

/**
 * Created by YS on 2017-01-25.
 */

public class RYLA {
    private static Application mApplication;
    private static String activityName;
    private static long startTime;
    private static long endTime;

    RylaInstrumentation rylaInstrumentation;

    // 실행한 액티비티 리스트
    private static ArrayList<Activity> activityList = new ArrayList<>();


    private static RYLA mRYLA = new RYLA();

    private RYLA() {
        // 초기화
        activityList.clear();
    }

    public static RYLA getInstance() {
        return mRYLA;
    }

    public Context getContext() {
        return mApplication;
    }

    public ArrayList<Activity> getActivityList() {
        return activityList;
    }

    // 실행전 Context 설정
    public RYLA setContext(String projectKey, Application application) {
        Logger.d("RYLA", "setContext 실행");
        mApplication = application;

        generateUUID();
        saveProjectKey(projectKey);

        // 스크린 OnOff 등록
        installScreenOffListner();

        // 디바이스 정보
        DeviceInfo.printDeviceInfo();

        // Custom Exception Handler 등록
        installExceptionHandler();

        // Activity List 를 가져오기위해 Callback에 등록
        mApplication.registerActivityLifecycleCallbacks(alcb);

        // Network 요청 정보를 가져오기위해 Factory에 등록
        startNetworkTracing();

        // 덤프 파일 초기화
        //DumpFileManager.getInstance(RYLA.getInstance().getContext()).initDumpFile();
/*
        String[] fileList = DumpFileManager.getInstance(RYLA.getInstance().getContext()).getFileList();
        for (String file : fileList) {
            DumpFileManager.getInstance(RYLA.getInstance().getContext()).deleteDumpFile(file);
        }*/

        return mRYLA;
    }

    public RYLA setActivityContext(Context context) {
        activityName = ((Activity) context).getClass().getSimpleName();

        return mRYLA;
    }

    public void installScreenOffListner() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);

        BroadcastReceiver screenOnOff = new BroadcastReceiver() {
            public static final String ScreenOff = "android.intent.action.SCREEN_OFF";
            public static final String ScreenOn = "android.intent.action.SCREEN_ON";

            public void onReceive(Context contex, Intent intent) {
                if (intent.getAction().equals(ScreenOff)) {
                    // Resource dump 일시중지
                    RylaInstrumentation.getInstance().setResThreadAlive(false);
                } else if (intent.getAction().equals(ScreenOn)
                        && !RylaInstrumentation.getInstance().isResThreadAlive()) {
                    // Resource dump 재실행
                    RylaInstrumentation.getInstance().setResThreadAlive(true);
                }
            }
        };

        getContext().registerReceiver(screenOnOff, intentFilter);

    }

    // 예외처리 핸들러 등록
    public void installExceptionHandler() {
        Thread.UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if(!(currentHandler instanceof RylaExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new RylaExceptionHandler(currentHandler));
        }
    }

    // Res 덤프 시작
    public void startResDump() {
        Logger.d("RYLA", "startResDump()");

        // Instrumentation 실행
        // Instrumentation 에서 CPU, Memory, Battery, 화면 클릭 가져옴
        RylaInstrumentation.getInstance().execute();
    }

    private void generateUUID() {
        String uuid = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE).getString("uuid", null);

        if(uuid == null) {
            uuid = UUID.randomUUID().toString();

            SharedPreferences.Editor editor = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
            editor.putString("uuid", uuid);
            editor.apply();
        }
    }

    private void saveProjectKey(String projectKey) {
        String savedProjectKey = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE).getString("projectKey", "");

        if(!projectKey.equals(savedProjectKey)) {
            SharedPreferences.Editor editor = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
            editor.putString("projectKey", projectKey);
            editor.apply();
        }
    }

    // Declare Activity Lifecycle Callback
    private static Application.ActivityLifecycleCallbacks alcb = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Logger.d("Lifecycle", "CREATED");
            // 호출 시간 dump
            /*
            // Deprecated
            DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                    new ActivityRenderData(activity.getClass().getSimpleName(),
                            ActivityRenderData.CREATED,
                            System.currentTimeMillis())
            );*/

            activityList.add(activity);
            Logger.d("ACTIVITIES", "Add : "+activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Logger.d("Lifecycle", "Started");
            //RYLA.getInstance().endRender("onStart");
            /*
            // Deprecated
            DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                    new ActivityRenderData(activity.getClass().getSimpleName(),
                            ActivityRenderData.STARTED,
                            System.currentTimeMillis())
            );*/
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Logger.d("Lifecycle", "Resumed");
            //RYLA.getInstance().endRender("onResume");

            /*
            // Deprecated
            DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                    new ActivityRenderData(activity.getClass().getSimpleName(),
                            ActivityRenderData.RESUMED,
                            System.currentTimeMillis())
            );*/
            // 백그라운드에서 포그라운드로 넘어온 경우
            if (isAppForeground() && !RylaInstrumentation.getInstance().isResThreadAlive()) {
                // Resource dump 재실행
                RylaInstrumentation.getInstance().setResThreadAlive(true);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            /*
            // Deprecated
            DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                    new ActivityRenderData(activity.getClass().getSimpleName(),
                            ActivityRenderData.PAUSED,
                            System.currentTimeMillis())
            );*/
            // 포그라운드에서 백그라운드로 넘어가는 경우
            // TODO 이게 호출되는 시점은 항상 포그라운드 이기 때문에 소용 없음, 루프에서 체크
            if (!isAppForeground() && RylaInstrumentation.getInstance().isResThreadAlive()) {
                // Resource dump 일시중지
                RylaInstrumentation.getInstance().setResThreadAlive(false);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            /*
            // Deprecated
            DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                    new ActivityRenderData(activity.getClass().getSimpleName(),
                            ActivityRenderData.STOPPED,
                            System.currentTimeMillis())
            );*/
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            /*
            // Deprecated
            DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                    new ActivityRenderData(activity.getClass().getSimpleName(),
                            ActivityRenderData.DESTROYED,
                            System.currentTimeMillis())
            );*/

            activityList.remove(activity);
            Logger.d("ACTIVITIES", "Delete : "+activity.getClass().getSimpleName());

            if (activityList.size() == 0) {
                // 액티비티 스택이 0 이면 실행중인 화면이 없으므로 자원 수집 멈춤
                // TODO 이시점에도 해당 Process 는 남아있음, Process 종료시점에 맞추어 Destroy 시켜야함
                RylaInstrumentation.getInstance().setResThreadAlive(false);
            }
        }
    };

    // 앱 포그라운드 확인
    public static boolean isAppForeground() {
        ActivityManager activityManager = (ActivityManager) mApplication.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = mApplication.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }



    public void startNetworkTracing() {
        //URL.setURLStreamHandlerFactory(new LanternURLStreamHandlerFactory());
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            try {
                // TODO 응답 시간 가져올것 (connect ~ close 까지)
                LanternSocketFactory sf = new LanternSocketFactory();
                Socket.setSocketImplFactory(sf);
                SSLSocket.setSocketImplFactory(sf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startRender(String lifecycleName) {
        Log.d("ASM TEST", "TEST!!!!!!!!");

        startTime = System.currentTimeMillis();
        Log.d("START Activity Time", "Activity Name: " + activityName + ", LFC Name: "+ lifecycleName + ", Time: "+startTime);
    }

    public void endRender(String lifecycleName) {
        endTime = System.currentTimeMillis();
        Log.d("END Activity Time", "Activity Name: " + activityName + ", LFC Name: "+ lifecycleName + ", Time: "+endTime);
        // TODO Save Render time info
        DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
            new ActivityRenderData(activityName,
                    lifecycleName,
                    startTime,
                    endTime)
        );
    }
}
