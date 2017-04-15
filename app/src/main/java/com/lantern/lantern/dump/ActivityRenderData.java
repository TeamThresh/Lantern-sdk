package com.lantern.lantern.dump;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YS on 2017-02-13.
 */

public class ActivityRenderData implements DumpData {
    private String activityName;
    private String lifecycleName;
    private Long startTime;
    private Long endTime;

    public static final String CREATED = "onCreate";
    public static final String RESUMED = "onResume";
    public static final String STARTED = "onStart";
    public static final String PAUSED = "onPause";
    public static final String STOPPED = "onStop";
    public static final String DESTROYED = "onDestroy";


    public ActivityRenderData() {
        this.activityName = "";
        this.lifecycleName = "";
        this.startTime = (long) 0;
        this.endTime= (long) 0;
    }

    public ActivityRenderData(String activityName, String lifecycleName, Long startTime, Long endTime) {
        this.activityName = activityName;
        this.lifecycleName = lifecycleName;
        this.startTime = startTime ;
        this.endTime = endTime ;
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

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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
            renderData.put("start_time", getStartTime());
            renderData.put("end_time", getEndTime());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return renderData;
    }
}
