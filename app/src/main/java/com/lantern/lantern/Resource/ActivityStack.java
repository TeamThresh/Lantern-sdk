package com.lantern.lantern.Resource;

import android.app.Activity;

import com.lantern.lantern.RYLA;
import com.lantern.lantern.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YS on 2017-03-17.
 */

public class ActivityStack implements Resource {
    List<String> activityStackList = new ArrayList<>();

    public ActivityStack() {
        for (Activity activity : RYLA.getInstance().getActivityList()) {
            Logger.d("ACTIVITIES", activity.getClass().getSimpleName());
            activityStackList.add(activity.getClass().getSimpleName());
        }
    }

    @Override
    public List toList() {
        return null;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public JSONArray toJsonArray() {
        JSONArray activityData = new JSONArray();

        for (int i = 0; i < activityStackList.size(); i++) {
            activityData.put(activityStackList.get(i));
        }

        return activityData;
    }
}
