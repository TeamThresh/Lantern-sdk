package com.lantern.lantern.dump;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YS on 2017-02-13.
 */

public class ActivityRenderData implements DumpData {
    private String activityName;
    private String lifecycleName;
    private Long callbackTime;

    public static final String CREATED = "onCreated";
    public static final String RESUMED = "onResumed";
    public static final String STARTED = "onStarted";
    public static final String PAUSED = "onPaused";
    public static final String STOPPED = "onStopped";
    public static final String DESTROYED = "onDestroyed";


    public ActivityRenderData() {
        this.activityName = "";
        this.lifecycleName = "";
        this.callbackTime = (long) 0;
    }

    public ActivityRenderData(String activityName, String lifecycleName, Long callbackTime) {
        this.activityName = activityName;
        this.lifecycleName = lifecycleName;
        this.callbackTime = callbackTime;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getLifecycleName() {
        return lifecycleName;
    }

    public void setLifecycleName(String lifecycleName) {
        this.lifecycleName = lifecycleName;
    }

    public Long getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Long callbackTime) {
        this.callbackTime = callbackTime;
    }

    @Override
    //type이 render인 dump JSON object 생성하기
    public JSONObject getDumpData() {
        JSONObject renderData = new JSONObject();

        try {
            //type
            renderData.put("type", "render");
            renderData.put("activity_name", getActivityName());
            renderData.put("lifecycle_name", getLifecycleName());
            renderData.put("callback_time", getCallbackTime());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return renderData;
    }
}
