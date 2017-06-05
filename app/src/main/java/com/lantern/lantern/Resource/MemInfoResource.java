package com.lantern.lantern.Resource;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yky on 2017. 6. 5..
 */

public class MemInfoResource implements Resource {

    private List<String> memInfoLabelList;
    private List<Long> memInfoValueList;

    public MemInfoResource() {
        memInfoLabelList = new ArrayList<>();
        memInfoValueList = new ArrayList<>();

        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");

            while(true) {
                String line = reader.readLine();
                if(line == null) {
                    break;
                }

                String [] splitValues = line.split("\\p{Z}");
                splitValues[0] = splitValues[0].replace(":", "");

                String memInfoValue = "0";
                for(int i=1;i<splitValues.length;i++) {
                    if(!splitValues[i].equals("") && !splitValues[i].isEmpty()) {
                        memInfoValue = splitValues[i];
                        break;
                    }
                }

                memInfoLabelList.add(splitValues[0]);
                memInfoValueList.add(Long.parseLong(memInfoValue));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List toList() {
        return null;
    }

    @Override
    public JSONObject toJson() {
        JSONObject memInfoData = new JSONObject();

        try {
            for (int i = 0; i < memInfoLabelList.size(); i++) {
                memInfoData.put(memInfoLabelList.get(i), memInfoValueList.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return memInfoData;
    }

    @Override
    public JSONArray toJsonArray() {
        return null;
    }
}
