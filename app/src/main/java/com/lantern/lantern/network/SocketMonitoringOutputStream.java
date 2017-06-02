package com.lantern.lantern.network;

import android.util.Log;

import com.lantern.lantern.util.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class SocketMonitoringOutputStream extends OutputStream {
	private final OutputStream out;
	//private final Socket socket;

	public SocketMonitoringOutputStream(OutputStream out) throws IOException {
		this.out = out;
		//this.socket = socket;
	}

	public void write(int b) throws IOException {
		long resTime = System.currentTimeMillis();
		Logger.d("Socket Resp out Start", ""+resTime);
		System.out.print('>');
		out.write(b);

		Logger.d("Socket Response Out", ""+ (System.currentTimeMillis() - resTime));
		Logger.d("Socket Response write", ""+ System.currentTimeMillis());
	}

	public void write(byte[] b, int off, int length) throws IOException {
		long resTime = System.currentTimeMillis();
		Logger.d("Socket Resp out Start", ""+resTime);
		System.out.print('>');
		out.write(b, off, length);

		Logger.d("Socket Response Out", ""+ (System.currentTimeMillis() - resTime));
		Logger.d("Socket Response write", ""+ System.currentTimeMillis());
	}
}