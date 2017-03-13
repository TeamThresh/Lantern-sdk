package com.lantern.lantern.network;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class SocketMonitoringInputStream extends InputStream {
    //private final Socket socket;
    private final InputStream in;

    public SocketMonitoringInputStream(InputStream in) throws IOException {
        //this.socket = socket;
        this.in = in;
    }

    public int read() throws IOException {
        long resTime = System.currentTimeMillis();
        Log.d("Socket Response Start", ""+resTime);
        System.out.print('<');
        int result = in.read();
        Log.d("Socket Response Time", ""+ (System.currentTimeMillis() - resTime));
        Log.d("Socket Response read", ""+ System.currentTimeMillis());
        return result;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        long resTime = System.currentTimeMillis();
        Log.d("Socket Response Start", ""+resTime);
		System.out.print('<');
		int length = in.read(b, off, len);
        Log.d("Socket Response Time", ""+ (System.currentTimeMillis() - resTime));
        Log.d("Socket Response read", ""+ System.currentTimeMillis());
        return length;
    }
}