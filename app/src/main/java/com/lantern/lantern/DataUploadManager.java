package com.lantern.lantern;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.lantern.lantern.dump.DumpFileManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by YS on 2017-02-06.
 */

public class DataUploadManager {
    URL Url;

    // TODO Constants 만들어서 상수 관리
    //String strURL = "http://www.naver.com"; //탐색하고 싶은 URL이다.
    private final String strURL = "https://52.78.70.54:9500/test";
    // TODO 파일 위치 결정
    String strFilePath = "pathName";

    JSONObject result;

    private final String TAG = "DumpFileManager";
    private Context mContext;

    private static DataUploadManager dataUploadManager;

    public DataUploadManager(Context context) {
        mContext = context;
    }

    public static DataUploadManager getInstance(Context context) {
        if(dataUploadManager == null) {
            dataUploadManager = new DataUploadManager(context.getApplicationContext());
        }

        return dataUploadManager;
    }

    public void sendData(final String httpMethod) {
        new AsyncTask<String,Void,JSONObject>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... values) {
                try {
                    Url = new URL(strURL);
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // connection 객체 생성
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(10000);

                    // Method 지정
                    conn.setRequestMethod(httpMethod);
                    conn.setDoOutput(true); // 쓰기모드 지정
                    conn.setDoInput(true); // 읽기모드 지정
                    conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                    conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                    /*
                    File file = new File(strFilePath);
                    String jsonData = readFile(file.getAbsolutePath());
                    */

                    /*
                    POST로 변경시
                    파일에서 읽어서 Output Stream으로 보낼것
                    합쳐서 파일 Stream과 연계해서 보내는 것도 괜찮을거 같은데...

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8")); //캐릭터셋 설정

                    writer.write(
                            "key1=value1" +
                                    + "&key2=value2" +
                                    + "&key3=value3"
                    ); //요청 파라미터를 입력
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();
                    */


//                    InputStream is = conn.getInputStream(); //input스트림 개방
//
//                    StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //문자열 셋 세팅
//                    String line;
//
//                    while ((line = reader.readLine()) != null) {
//                        builder.append(line + "\n");
//                    }

                    String requestData = DumpFileManager.getInstance(RYLA.getInstance().getContext()).readDumpFile();

                    //result = builder.toString();

                    result = new JSONObject(requestData);

                } catch (MalformedURLException | ProtocolException exception) {
                    exception.printStackTrace();
                } catch (IOException io) {
                    io.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return result;
            }

//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                System.out.println(result);
//            }

            @Override
            protected void onPostExecute(JSONObject result) {
                try {
                    String rtnCode = result.get("message").toString();
                    Log.d(TAG, "rtnCode : " + rtnCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    protected static String readFile(String filePath) throws Exception {
        if(filePath == null) {
            throw new IllegalArgumentException("filePath Argument is null");
        } else {
            StringBuilder builder = new StringBuilder();
            BufferedReader input = null;

            try {
                input = new BufferedReader(new FileReader(filePath));
                String e = null;

                while((e = input.readLine()) != null) {
                    builder.append(e);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if(input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        throw e;
                    }
                }

            }

            return builder.toString();
        }
    }
}
