package com.lantern.lantern.dump;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import com.lantern.lantern.R;
import com.lantern.lantern.RYLA;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yky on 2017. 5. 12..
 */

public class SystemServiceData implements DumpData {

    @Override
    public JSONObject getDumpData() {
        JSONObject systemServiceData = new JSONObject();

        try {
            systemServiceData.put("wifi", checkWifiNetworkOn(RYLA.getInstance().getContext()));
            systemServiceData.put("mobile_network", checkMobileNetworkOn(RYLA.getInstance().getContext()));
            systemServiceData.put("gps", checkGpsOn(RYLA.getInstance().getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return systemServiceData;
    }


    private boolean checkNetworkOn(Context context, int type) {
        boolean isNetworkOn = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE",
                    context.getPackageName()) == 0) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                isNetworkOn = connectivityManager.getNetworkInfo(type).isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isNetworkOn;
    }

    private boolean checkMobileNetworkOn(Context context) {
        return checkNetworkOn(context, ConnectivityManager.TYPE_MOBILE);
    }

    private boolean checkWifiNetworkOn(Context context) {
        return checkNetworkOn(context, ConnectivityManager.TYPE_WIFI);
    }


    private boolean checkGpsOn(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.checkPermission("android.permission.ACCESS_FINE_LOCATION", context.getPackageName()) == 0) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }
}
