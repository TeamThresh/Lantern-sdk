package com.lantern.lantern.Resource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YS on 2017-02-15.
 */

public class NetworkResource implements Resource {
    List<String> rxtxInfo = new ArrayList<>();

    public NetworkResource() {
        // TODO 권한이 필요하므로 삭제
        /*ConnectivityManager connManager;
        connManager = (ConnectivityManager) RYLA.getInstance().getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mNetwork = connManager.getActiveNetworkInfo();

        Log.d("NETWORK NAME", mNetwork.getTypeName());
        rxtxInfo.add(mNetwork.getTypeName());

        long mRX = TrafficStats.getMobileRxBytes();
        long mTX = TrafficStats.getMobileTxBytes();

        if (mRX == TrafficStats.UNSUPPORTED || mTX == TrafficStats.UNSUPPORTED) {
            Log.d("NETWORK USAGE", "지원안함");
            rxtxInfo.add("-1");
            rxtxInfo.add("-1");
        } else {
            Log.d("NETWORK USAGE", "Rx: " + mRX + ", Tx: " + mTX);
            rxtxInfo.add(Long.toString(mRX));
            rxtxInfo.add(Long.toString(mTX));
        }*/
    }

    @Override
    public List<String> toList() {
        return rxtxInfo;
    }

    @Override
    public String toString() {
        if (rxtxInfo.size() != 0) {
            String result = "\nNETWORK NAME : " + rxtxInfo.get(0);
            if ("-1".equals(rxtxInfo.get(1)) || "-1".equals(rxtxInfo.get(2)))
                result += "\nNETWORK USAGE : " + "지원안함";
            else
                result += "\nNETWORK USAGE : " + "Rx: " + rxtxInfo.get(1) + ", Tx: " + rxtxInfo.get(2);
            return result;
        } else {
            return "0";
        }
    }

    public JSONObject toJSON() {
        JSONObject networkData = new JSONObject();
        try {
            if (rxtxInfo.size() != 0) {
                String[] labelNetwork = {"type", "rx", "tx"};
                for (int i = 0; i < labelNetwork.length; i++) {
                    if (i == 0) {
                        networkData.put(labelNetwork[i], rxtxInfo.get(i));
                    } else {
                        networkData.put(labelNetwork[i], Long.parseLong(rxtxInfo.get(i)));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return networkData;
    }
}
