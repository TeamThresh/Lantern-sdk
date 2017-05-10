package com.lantern.lantern.Resource;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YS on 2017-02-15.
 */

public class StatResource implements Resource {

    List<String> vmstatInfo = new ArrayList<>();

    public StatResource() {
        // vmstat INFO
        // TODO 구형 폰에서 오래걸림

        Process process;
        String cmd = "vmstat";
        int index = 0;
        try {
            process = Runtime.getRuntime().exec(cmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line ;
            while ((line = br.readLine()) != null) {
                String segs[] = line.trim().split("[ ]+");
                if (2 == index++) {
                    vmstatInfo = new ArrayList<String>(Arrays.asList(segs));
                    break;
                }
            }

            process.destroy();
        } catch (Exception e) {
            e.fillInStackTrace();
            Log.e("Process Manager", "Unable to execute "+cmd+" command");
        }

    }

    @Override
    public List<String> toList() {
        return vmstatInfo;
    }

    @Override
    public JSONObject toJson() {
        JSONObject vmstatData = new JSONObject();

        try {
            // TODO vmstat의 데이터가 기종, 버전마다 다름
            String[] labelVmStat = {"r", "b", "swpd", "free", "buff", "cache", "si", "so", "bi", "bo", "in", "cs", "us", "sy", "id", "wa"};
            for (int i = 0; i < labelVmStat.length; i++) {
                try {
                    vmstatData.put(labelVmStat[i], vmstatInfo.get(i));
                } catch (IndexOutOfBoundsException e) {
                    vmstatData.put(labelVmStat[i], -1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vmstatData;
    }

    @Override
    public JSONArray toJsonArray() {
        return null;
    }

    @Override
    public String toString() {
        JSONObject vmstatData = new JSONObject();
        try {
            String[] labelVmStat = {"r", "b", "swpd", "free", "buff", "cache", "si", "so", "bi", "bo", "in", "cs", "us", "sy", "id", "wa"};
            for (int i = 0; i < labelVmStat.length; i++) {
                try {
                    vmstatData.put(labelVmStat[i], vmstatInfo.get(i));
                } catch (IndexOutOfBoundsException e) {
                    vmstatData.put(labelVmStat[i], -1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vmstatData.toString();
    }
}
