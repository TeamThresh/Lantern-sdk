package com.lantern.lantern.dump;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yky on 2017. 2. 12..
 */

public class DumpFileManager {
    private final String TAG = "DumpFileManager";
    private final String DUMP_FILE_NAME = "ryla_dump";

    private Context mContext;

    private JSONObject dumpJson;

    private static DumpFileManager dumpFileManager;

    private DumpFileManager(Context context) {
        mContext = context;
    }

    public static DumpFileManager getInstance(Context context) {
        if(dumpFileManager == null) {
            dumpFileManager = new DumpFileManager(context.getApplicationContext());
        }

        return dumpFileManager;
    }

    public void initDumpFile() {
        dumpJson = getDumpHeader();

        //Log.d(TAG, dumpJson.toString());

        saveDumpHeader(dumpJson.toString());

        //Log.d(TAG, readDumpFile());

    }

    private JSONObject getDumpHeader() {
        JSONObject dumpData = new JSONObject();
        JSONObject deviceInfo = new JSONObject();
        JSONArray dumpContents = new JSONArray();
        try {
            dumpData.put("launch_time", System.currentTimeMillis());
            dumpData.put("dump_interval", mContext.getSharedPreferences("pref", MODE_PRIVATE).getInt("dump_term", 1000));
            dumpData.put("package_name", mContext.getPackageName());

            deviceInfo.put("os", Build.VERSION.RELEASE);
            deviceInfo.put("app", getAppVersion());
            deviceInfo.put("device", Build.DEVICE);
            deviceInfo.put("brand", Build.BRAND);

            dumpData.put("device_info", deviceInfo);
            dumpData.put("data", dumpContents);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dumpData;
    }

    private void saveDumpHeader(String dumpHeaderString) {
        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(DUMP_FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(dumpHeaderString.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readDumpFile() {
        FileInputStream inputStream;
        StringBuffer buffer = new StringBuffer();
        int readCount = 0;

        try {
            String inputStr;
            inputStream = mContext.openFileInput(DUMP_FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            inputStr = reader.readLine();

            while(inputStr != null) {
                buffer.append(inputStr + "\n");
                inputStr = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private String getAppVersion() {
        String version = "";
        try {
            PackageInfo i = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            version = i.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }
}
