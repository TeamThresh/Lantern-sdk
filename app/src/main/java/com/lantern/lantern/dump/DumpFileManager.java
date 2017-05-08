package com.lantern.lantern.dump;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lantern.lantern.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yky on 2017. 2. 12..
 */

public class DumpFileManager {
    private final String TAG = "DumpFileManager";
    public static final String FILE_NAME_HEADER = "ryladump";
    private static String FILE_NAME = "";

    private Context mContext;

    private JSONObject dumpJson;
    private boolean isFirstData = false;

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

    //덤프 파일 생성하기 및 덤프 헤더 파일에 저장하기
    public void initDumpFile() {
        Logger.d(TAG,"initDumpFile");
        //덤프 헤더 파일 작성
        //dumpJson = getDumpHeader();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        FILE_NAME = FILE_NAME_HEADER + "_" +
                mContext.getPackageName() + "_" +
                calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH) + 1) +
                calendar.get(Calendar.DAY_OF_MONTH) + "_" + calendar.getTimeInMillis();
        Log.d("FILENAME", FILE_NAME);

        //덤프 헤더를 파일에 저장
        initDumpHead();
        //saveDumpFile(dumpJson.toString());
    }

    private void initDumpHead() {
        try {
            File f= new File(mContext.getFilesDir().toString()+"/"+FILE_NAME);

            if(!f.exists()) {
                f.createNewFile();
            }

            PrintWriter pww= new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
            pww.write("{\"launch_time\":\""+System.currentTimeMillis()+"\","+
                    "\"dump_interval\":\""+mContext.getSharedPreferences("pref", MODE_PRIVATE).getInt("dump_term", 1000)+"\","+
                    "\"package_name\":\""+mContext.getPackageName()+"\","+
                    "\"device_info\":{"+
                    "\"os\":\""+Build.VERSION.RELEASE+"\","+
                    "\"app\":\""+getAppVersion()+"\","+
                    "\"device\":\""+Build.MODEL+"\","+
                    "\"brand\":\""+Build.BRAND+"\"},"+
                    "\"data\":[");
            isFirstData = true;
            pww.flush();

            pww.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endDumpFooter() {
        try {
            File f= new File(mContext.getFilesDir().toString()+"/"+FILE_NAME);

            PrintWriter pww= new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
            pww.write("]}");

            pww.flush();

            pww.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //덤프 헤더 JSON 생성하기
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
            deviceInfo.put("device", Build.MODEL);
            deviceInfo.put("brand", Build.BRAND);
            // TODO 권한이 필요하므로 삭제
            deviceInfo.put("UUID", getDevicesUUID());

            dumpData.put("device_info", deviceInfo);
            dumpData.put("data", dumpContents);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dumpData;
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

    private String getDevicesUUID(){
        /*
        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
        */

        String uuid = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE).getString("uuid", null);

        return uuid;
    }

    //덤프 파일에 저장하기
    //JSON 형식의 String을 파일에 저장
    private void saveDumpFile(String dumpFileString) {
        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(dumpFileString.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized FileInputStream readDumpStream() {
        FileInputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String inputStr;
            inputStream = mContext.openFileInput(FILE_NAME);

        } catch (FileNotFoundException e) {
            // 파일 없을경우 새로 생성
            initDumpFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public synchronized FileInputStream readDumpStream(String fileName) {
        FileInputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String inputStr;
            inputStream = mContext.openFileInput(fileName);

        } catch (FileNotFoundException e) {
            // 파일 없을경우 새로 생성
            initDumpFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    //덤프 파일 읽어오기
    public synchronized String readDumpFile() {
        Log.d("Lantern","read Dump File");
        FileInputStream inputStream;
        StringBuilder builder = new StringBuilder();

        try {
            String inputStr;
            inputStream = mContext.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            inputStr = reader.readLine();

            while(inputStr != null) {
                //buffer.append(inputStr + "\n");
                //builder.append(inputStr + "\n");
                builder.append(inputStr);
                inputStr = reader.readLine();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            // 파일 없을경우 새로 생성
            initDumpFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    //덤프 파일 읽어오기
    public synchronized String readDumpFile(String fileName) {
        Log.d("Lantern","read Dump File");
        FileInputStream inputStream;
        StringBuilder builder = new StringBuilder();

        try {
            String inputStr;
            inputStream = mContext.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            inputStr = reader.readLine();

            while(inputStr != null) {
                //buffer.append(inputStr + "\n");
                //builder.append(inputStr + "\n");
                builder.append(inputStr);
                inputStr = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            // 파일 없을경우 새로 생성
            initDumpFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    //dump data 파일에 저장하기
    public synchronized void saveDumpData(DumpData dumpData) {
        Log.d("Lantern", "save Dump File" + dumpData.getClass());
        JSONObject resDumpJson = dumpData.getDumpData();
        /*JSONObject preSavedDumpData;

        try {
            preSavedDumpData = new JSONObject(readDumpFile());
            preSavedDumpData.getJSONArray("data").put(resDumpJson);
            saveDumpFile(preSavedDumpData.toString());

            //Logger.d(TAG, new JSONObject(readDumpFile()).toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        // File Append
        try {
            Log.d("dumpdata", resDumpJson.toString());
            Log.d("directory path",mContext.getFilesDir().toString()+"/"+FILE_NAME);
            File f= new File(mContext.getFilesDir().toString()+"/"+FILE_NAME);
            PrintWriter pww = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));

            if (isFirstData) {
                pww.write(resDumpJson.toString());
                isFirstData = false;
            } else pww.write(","+resDumpJson.toString());

            pww.flush();

            pww.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getFileList() {
        File file = mContext.getFilesDir();
        String[] allList = file.list();
        ArrayList<String> lanternList = new ArrayList<>();
        for(String name: allList) {
            Log.d("ALL FILELIST NAME", name);
            String[] splited = name.split("_");
            if (splited[0].equals(FILE_NAME_HEADER)
                    && splited[1].equals(mContext.getPackageName())) {
                // 현재 파일 제외 (아직 쓰고있음)
                if (!name.equals(FILE_NAME)) {
                    Log.d("FILELIST NAME", name);
                    lanternList.add(name);
                }
            } else if (splited[0].equals("ryla")) {
                mContext.deleteFile(name);
            }
        }
        Log.d("FILELIST", lanternList.toString());

        if (lanternList.size() == 0){
            return new String[] {};
        } else {
            return lanternList.toArray(new String[lanternList.size()]);
        }
    }

    public synchronized void deleteDumpFile(String fileName) {
        if (mContext.deleteFile(fileName)) {
            Log.d("File delete", "파일 지우기 성공");
        } else {
            Log.d("File delete", "파일 지우기 실패");
        }
    }
}
