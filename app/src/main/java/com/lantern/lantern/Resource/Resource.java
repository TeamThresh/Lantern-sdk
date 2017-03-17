package com.lantern.lantern.Resource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by YS on 2017-02-15.
 */

public interface Resource {
    public List toList();
    public String toString();
    public JSONObject toJson();
    public JSONArray toJsonArray();
}
