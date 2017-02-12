package com.lantern.lantern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;

/**
 * Created by YS on 2017-01-25.
 */

public class RYLA {
    private static Application mApplication;

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
    public RYLA setContext(Application application) {
        Log.d("RYLA", "setContext 실행");
        mApplication = application;

        // 디바이스 정보
        DeviceInfo.printDeviceInfo();

        // Custom Exception Handler 등록
        installExceptionHandler();

        // Activity List 를 가져오기위해 Callback에 등록
        mApplication.registerActivityLifecycleCallbacks(alcb);

        return mRYLA;
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
        // Instrumentation 실행
        // Instrumentation 에서 CPU, Memory, Battery, 화면 클릭 가져옴
        RylaInstrumentation.getInstance().excute();
    }

    // Thread Stack 가져옴
    public List<String> getThreadTracing() {
        List<String> stackTraceLines = new ArrayList<>();

        Thread t = Looper.getMainLooper().getThread();
        StackTraceElement[] stackTraceList = t.getStackTrace();
        for(StackTraceElement stackTrace : stackTraceList) {
            Log.d("STACK TRACE", stackTrace.toString());
            stackTraceLines.add(stackTrace.toString());
        }

        return stackTraceLines;

        // 현재 쓰레드 스택트레이스
        /*************************
         Thread ct = Thread.currentThread();
         StackTraceElement[] currentStackTraceList = ct.getStackTrace();
         for(StackTraceElement stackTrace : currentStackTraceList) {
         Log.d("STACK TRACE", stackTrace.toString());
         }
         */

        // 전체 스택트레이스
        /*************************
         Map<Thread, StackTraceElement[]> temp = Thread.getAllStackTraces();
         Set<Thread> tempKey = temp.keySet();
         for (Thread th : tempKey) {
         Log.i("KEY", th.getName());
         for (StackTraceElement element : temp.get(th)) {
         Log.i("ELEMENTS", element.toString());
         }
         }
         */
    }

    private static Application.ActivityLifecycleCallbacks alcb = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activityList.add(activity);
            Log.d("ACTIVITIES", "Add : "+activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d("Activity Life", "Started");
            RYLA.getInstance().getThreadTracing();

            // 백그라운드에서 포그라운드로 넘어온 경우
            if (isAppForeground() && !RylaInstrumentation.getInstance().isResThreadAlive()) {
                // Resource dump 재실행
                RylaInstrumentation.getInstance().setResThreadAlive(true);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d("Activity Life", "Resumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {

            // 포그라운드에서 백그라운드로 넘어가는 경우
            // TODO 이게 호출되는 시점은 항상 포그라운드 이기 때문에 소용 없음, 루프에서 체크
            if (!isAppForeground() && RylaInstrumentation.getInstance().isResThreadAlive()) {
                // Resource dump 일시중지
                RylaInstrumentation.getInstance().setResThreadAlive(false);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityList.remove(activity);
            Log.d("ACTIVITIES", "Delete : "+activity.getClass().getSimpleName());

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
        URL.setURLStreamHandlerFactory(new LanternURLStreamHandlerFactory());
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
