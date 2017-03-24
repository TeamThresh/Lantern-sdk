package com.lantern.lantern.dump;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lantern.lantern.RYLA;
import com.lantern.lantern.util.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    //private final static String SERVER_URL = "http://172.16.100.61:3000/api/upload";
    private HttpURLConnection conn;

    public DataUploadTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Logger.d(TAG, "Prepare to upload dump data");

        String[] fileList = DumpFileManager.getInstance(RYLA.getInstance().getContext()).getFileList();
        Log.d(TAG, fileList.toString());
        // 저장된 파일 갯수 만큼 http 실행
        for (String file : fileList) {

            // 연결
            try {
                String server_url = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE).getString("server_url", SERVER_URL);
                URL url = new URL(server_url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(1000 * 60 * 60);
                conn.setReadTimeout(1000 * 60 * 60);
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                // 데이터
                // 전송
                OutputStream os = conn.getOutputStream();
                BufferedInputStream is = new BufferedInputStream(DumpFileManager.getInstance(RYLA.getInstance().getContext()).readDumpStream(file));
                byte[] buffer = new byte[1024]; // Adjust if you want
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                String footer = "]}";
                os.write(footer.getBytes(), 0, footer.length());

                os.flush();
                is.close();
                os.close();


                // 파일 전송을 완료한 경우 파일제거
                DumpFileManager.getInstance(RYLA.getInstance().getContext()).deleteDumpFile(file);

                /*OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                osw.write(DumpFileManager.getInstance(RYLA.getInstance().getContext()).readDumpFile());
                osw.flush();

                osw.close();
                os.close();*/


                // 응답
                InputStream inputStream;

                // 전송 응답 받음
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getErrorStream();
                } else {
                    inputStream = conn.getInputStream();
                }
                BufferedReader br = null;
                br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String line = null;
                while ((line = br.readLine()) != null) {
                    Logger.d(TAG, "response : " + line);
                }

                // 닫기
                inputStream.close();
                br.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result){
        //Log.d("Post Execute", result);
        Log.d(TAG, "complete to upload dump data");
        if (conn != null)
            conn.disconnect();
    }
}

