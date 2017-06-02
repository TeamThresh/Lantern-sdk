package com.lantern.lantern.dump;

import com.lantern.lantern.network.NetworkCallbackData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YS on 2017-04-06.
 */

public class NetworkResponseData implements DumpData {
    NetworkCallbackData networkResponseData;

    public NetworkResponseData(NetworkCallbackData networkResponseData) {
        this.networkResponseData = networkResponseData;
    }

    @Override
    public JSONObject getDumpData() {
        JSONObject networkData = new JSONObject();

        try {
            //type
            networkData.put("type", "request");

            // network info
            networkData.put("request_time", networkResponseData.getStartTime());
            networkData.put("response_time", networkResponseData.getEndTime());
            networkData.put("host", networkResponseData.getHostName());
            networkData.put("status", networkResponseData.getStatus());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return networkData;
    }
}
