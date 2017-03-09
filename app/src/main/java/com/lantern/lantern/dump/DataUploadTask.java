package com.lantern.lantern.dump;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lantern.lantern.RYLA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by yky on 2017. 2. 13..
 */

public class DataUploadTask extends AsyncTask<Void, Void, String> {
    private final String TAG = "DataUploadTask";
    private Context mContext;
    private final static String SERVER_URL = "http://61.43.139.16:3000/api/upload";

    public DataUploadTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground");

        // 연결
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 데이터
            JSONObject jsonObject = new JSONObject(DumpFileManager.getInstance(RYLA.getInstance().getContext()).readDumpFile());
            String param = jsonObject.toString();

            // 전송
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(param);
            osw.flush();

            // 응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                Log.d(TAG, "response : " + line);
            }

            // 닫기
            osw.close();
            br.close();

            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute(){
        Log.d(TAG, "onPreExecute");
        DumpFileManager.getInstance(RYLA.getInstance().getContext()).initDumpFile();
    }

    @Override
    protected void onPostExecute(String result){
        Log.d(TAG, "onPostExecute");
        DumpFileManager.getInstance(RYLA.getInstance().getContext()).initDumpFile();
    }
}

