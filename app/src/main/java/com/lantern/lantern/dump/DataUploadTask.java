package com.lantern.lantern.dump;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lantern.lantern.RYLA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    private HttpURLConnection conn;

    public DataUploadTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground");

        // 연결
        try {
            URL url = new URL(SERVER_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // 데이터
            JSONObject jsonObject = new JSONObject(DumpFileManager.getInstance(RYLA.getInstance().getContext()).readDumpFile());
            String param = jsonObject.toString();
            Log.d("CLASS NAME", "" +conn.getDoInput() +"/"+conn.getDoOutput());
            Log.d("CLASS NAME", conn.getClass().toString());
            // 전송
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(param);
            osw.flush();

            // 응답
            InputStream inputStream;

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                inputStream = conn.getErrorStream();
            } else {
                inputStream = conn.getInputStream();
            }
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                Log.d(TAG, "response : " + line);
            }

            // 닫기
            osw.close();
            inputStream.close();
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
    protected void onPostExecute(String result){
        Log.d(TAG, "onPostExecute");
        conn.disconnect();
        DumpFileManager.getInstance(RYLA.getInstance().getContext()).initDumpFile();
    }
}

