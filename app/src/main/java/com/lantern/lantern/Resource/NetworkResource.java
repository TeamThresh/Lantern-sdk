package com.lantern.lantern.Resource;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.util.Log;

import com.lantern.lantern.RYLA;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by YS on 2017-02-15.
 */

public class NetworkResource implements Resource {
    List<String> rxtxInfo = new ArrayList<>();

    public NetworkResource() {
        ConnectivityManager connManager;
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
        }
    }

    @Override
    public List<String> toList() {
        return rxtxInfo;
    }

    @Override
    public String toString() {
        String result = "\nNETWORK NAME : "+ rxtxInfo.get(0);
        if("-1".equals(rxtxInfo.get(1)) || "-1".equals(rxtxInfo.get(2)))
            result += "\nNETWORK USAGE : " + "지원안함";
        else
            result += "\nNETWORK USAGE : " + "Rx: " + rxtxInfo.get(1) + ", Tx: " + rxtxInfo.get(2);
        return result;
    }
}
