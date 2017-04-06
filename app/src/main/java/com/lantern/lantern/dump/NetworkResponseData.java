package com.lantern.lantern.dump;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YS on 2017-04-06.
 */

public class NetworkResponseData implements DumpData {
    long requestTime;
    long responseTime;
    String host;

    public NetworkResponseData(long requestTime, long responseTime, String host) {
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.host = host;
    }

    @Override
    public JSONObject getDumpData() {
        JSONObject networkData = new JSONObject();

        try {
            //type
            networkData.put("type", "request");

            // network info
            networkData.put("request_time", this.requestTime);
            networkData.put("response_time", this.responseTime);
            networkData.put("host", this.host);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return networkData;
    }
}
