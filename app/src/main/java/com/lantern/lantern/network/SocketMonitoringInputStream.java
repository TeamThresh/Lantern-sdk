package com.lantern.lantern.network;

import android.os.Message;

import java.io.IOException;
import java.io.InputStream;

import static com.lantern.lantern.network.LanternSocketImpl.STATUS_MSG;

public class SocketMonitoringInputStream extends InputStream {
    //private final Socket socket;
    private final InputStream in;
    private boolean connected = false;
    private NetworkCallback callbackHandler;

    public SocketMonitoringInputStream(InputStream in, NetworkCallback callbackHandler) throws IOException {
        //this.socket = socket;
        this.in = in;
        this.callbackHandler = callbackHandler;
    }

    public int read() throws IOException {
        long resTime = System.currentTimeMillis();
        //Log.d("SOCKET", "socket start <");
        //System.out.print('<');
        int result = in.read();
        //System.out.print(result);
        char c = (char) result;
        //Log.d("SOCKET", String.valueOf(result));
        return result;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        long resTime = System.currentTimeMillis();
        //System.out.print('<');
        //int length = in.read(b, off, len);
        //System.out.print(length);

        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int i = 1;
        try {
            for (; i < len ; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte)c;
            }
        } catch (IOException ee) {
        }

        if (!connected) {
            String lines = new String(b);
            String[] line = lines.split("\n");
            if (line[0].contains("HTTP/1.1")) {
                String[] word = line[0].split(" ");
                // 메시지 얻어오기
                Message msg = new Message();

                // 메시지 ID 설정
                msg.what = STATUS_MSG;

                // 메시지 정보 설정3 (Object 형식)
                msg.obj = new NetworkCallbackData(word[1]);

                callbackHandler.complete(msg);
            }
        }
        return i;

        //return length;
    }
}