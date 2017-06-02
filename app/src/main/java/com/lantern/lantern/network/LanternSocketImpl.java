package com.lantern.lantern.network;

import android.os.Message;
import android.util.Log;

import com.lantern.lantern.RYLA;
import com.lantern.lantern.dump.DumpFileManager;
import com.lantern.lantern.dump.NetworkResponseData;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

/**
 * Created by YS on 2017-02-07.
 */

public class LanternSocketImpl extends SocketImpl {

    private final Delegator delegator;
    private String name = "NA";
    private String method;
    private boolean http;
    private String protocol = "NA";
    private long startTime;
    private boolean readingDone = false;

    private NetworkCallbackData networkCallbackData;
    public NetworkCallback networkCallback;

    public static final int CLOSE_MSG = 100;
    public static final int STATUS_MSG = 101;

    public LanternSocketImpl() {
        this.delegator = new Delegator(this, SocketImpl.class, "java.net.PlainSocketImpl");

        networkCallback = new NetworkCallback() {
            @Override
            public void complete(Message msg) {
                switch (msg.what) {
                    case CLOSE_MSG:
                        NetworkCallbackData closeCallbackData = (NetworkCallbackData) msg.obj;
                        if (networkCallbackData == null) {
                            networkCallbackData = closeCallbackData ;
                        } else {
                            networkCallbackData.setHostName(closeCallbackData.getHostName());
                            networkCallbackData.setStartTime(closeCallbackData.getStartTime());
                            networkCallbackData.setEndTime(closeCallbackData.getEndTime());
                        }
                        break;
                    case STATUS_MSG:
                        NetworkCallbackData statusCallbackData = (NetworkCallbackData) msg.obj;
                        if (networkCallbackData == null) {
                            networkCallbackData = statusCallbackData ;
                        } else {
                            networkCallbackData.setStatus(statusCallbackData.getStatus());
                        }
                        break;
                }

                if (networkCallbackData.isComplete()) {
                    DumpFileManager.getInstance(RYLA.getInstance().getContext()).saveDumpData(
                            new NetworkResponseData(networkCallbackData)
                    );
                }
            }
        };
    }

    protected void create(boolean isStreaming) throws IOException {
        Log.d("Socket Impl", "create");
        try {
            this.delegator.invoke(new Object[]{Boolean.valueOf(isStreaming)});
        } catch (Exception var5) {
            Exception e = var5;
            if(var5 instanceof IOException) {
                throw (IOException)var5;
            }
            try {
                throw e.getCause();
            } catch (Throwable var4) {
                var4.printStackTrace();
            }
        }

    }

    protected void bind(InetAddress address, int port) throws IOException {
        this.name = address.getHostName();
        Log.d("Socket bind", this.name);
        try {
            this.delegator.invoke(new Object[]{address, Integer.valueOf(port)});
        } catch (Exception var6) {
            Exception e = var6;
            if(var6 instanceof IOException) {
                throw (IOException)var6;
            }
            try {
                throw e.getCause();
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }

        this.startTime = System.currentTimeMillis();
    }

    protected void accept(SocketImpl newSocket) throws IOException {
        try {
            this.delegator.invoke(new Object[]{newSocket});
        } catch (Exception var5) {
            Exception e = var5;
            if(var5 instanceof IOException) {
                throw (IOException)var5;
            }
            try {
                throw e.getCause();
            } catch (Throwable var4) {
                var4.printStackTrace();
            }
        }

    }

    protected int available() throws IOException {
        try {
            Integer e = (Integer)this.delegator.invoke(new Object[0]);
            return e.intValue();
        } catch (Exception var2) {
            if(var2 instanceof IOException) {
                throw (IOException)var2;
            } else {
                return 0;
            }
        }
    }

    protected void connect(InetAddress address, int port) throws IOException {
        this.name = address.getHostName();
        Log.d("Socket connect", this.name);
        try {
            this.delegator.delegateTo("connect", new Class[]{InetAddress.class, Integer.TYPE}).invoke(new Object[]{address, Integer.valueOf(port)});
        } catch (Exception var6) {
            Exception e = var6;
            if(var6 instanceof IOException) {
                throw (IOException)var6;
            }
            try {
                throw e.getCause();
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }

        this.startTime = System.currentTimeMillis();
        this.setProtocolFromPort(port);
    }

    protected void connect(SocketAddress remoteAddr, int timeout) throws IOException {
        if(remoteAddr instanceof InetSocketAddress) {
            InetSocketAddress e = (InetSocketAddress)remoteAddr;
            this.name = e.getHostName();
            this.setProtocolFromPort(e.getPort());
        } else {
            this.name = remoteAddr.toString();
        }
        Log.d("Socket connect", this.name);

        try {
            this.delegator.invoke(new Object[]{remoteAddr, Integer.valueOf(timeout)});
        } catch (Exception var6) {
            Exception e1 = var6;
            if(var6 instanceof IOException) {
                throw (IOException)var6;
            }
            try {
                throw e1.getCause();
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }

        this.startTime = System.currentTimeMillis();
    }

    protected void connect(String host, int port) throws IOException {
        this.name = host;
        Log.d("Socket connect", this.name);

        try {
            this.delegator.invoke(new Object[]{host, Integer.valueOf(port)});
        } catch (Exception var6) {
            Exception e = var6;
            if(var6 instanceof IOException) {
                throw (IOException)var6;
            }
            try {
                throw e.getCause();
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }

        this.startTime = System.currentTimeMillis();
        this.setProtocolFromPort(port);
    }

    protected void close() throws IOException {
        Log.d("Socket connection time", (System.currentTimeMillis() - this.startTime) + "");

        Message msg = new Message();
        msg.what = CLOSE_MSG; // CLOSE 호출 메세지
        msg.obj = new NetworkCallbackData(this.startTime, System.currentTimeMillis(), this.name);

        // callback 등록
        networkCallback.complete(msg);

        try {
            this.delegator.invoke(new Object[0]);
        } catch (Exception var4) {
            Exception e = var4;
            if(var4 instanceof IOException) {
                throw (IOException)var4;
            }
            try {
                throw e.getCause();
            } catch (Throwable var3) {
                var3.printStackTrace();
            }
        }

    }

    protected InputStream getInputStream() throws IOException {
        InputStream stream = null;

        try {
            stream = (InputStream)this.delegator.invoke(new Object[0]);
        } catch (Exception var3) {
            if(var3 instanceof IOException) {
                throw (IOException)var3;
            }
        }
        //return stream;
        return new SocketMonitoringInputStream(stream, networkCallback);
    }

    public Object getOption(int optID) throws SocketException {
        try {
            return this.delegator.invoke(new Object[]{Integer.valueOf(optID)});
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    protected OutputStream getOutputStream() throws IOException {
        OutputStream out = null;

        try {
            out = (OutputStream)this.delegator.invoke(new Object[0]);
        } catch (Exception var3) {
            if(var3 instanceof IOException) {
                throw (IOException)var3;
            }

            var3.printStackTrace();
        }
        return out;
        //return new SocketMonitoringOutputStream(out);
    }

    protected void listen(int backlog) throws IOException {
        try {
            this.delegator.invoke(new Object[]{Integer.valueOf(backlog)});
        } catch (Exception var3) {
            if(var3 instanceof IOException) {
                throw (IOException)var3;
            }

            var3.printStackTrace();
        }

    }

    protected void sendUrgentData(int value) throws IOException {
        try {
            this.delegator.invoke(new Object[]{Integer.valueOf(value)});
        } catch (Exception var3) {
            if(var3 instanceof IOException) {
                throw (IOException)var3;
            }

            var3.printStackTrace();
        }

    }

    public void setOption(int optID, Object val) throws SocketException {
        try {
            this.delegator.invoke(new Object[]{Integer.valueOf(optID), val});
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    protected FileDescriptor getFileDescriptor() {
        try {
            return (FileDescriptor)this.delegator.invoke(new Object[0]);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    protected InetAddress getInetAddress() {
        try {
            return (InetAddress)this.delegator.invoke(new Object[0]);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    protected int getLocalPort() {
        try {
            Integer e = (Integer)this.delegator.invoke(new Object[0]);
            return e.intValue();
        } catch (Exception var2) {
            var2.printStackTrace();
            return -1;
        }
    }

    protected int getPort() {
        try {
            Integer e = (Integer)this.delegator.invoke(new Object[0]);
            return e.intValue();
        } catch (Exception var2) {
            var2.printStackTrace();
            return -1;
        }
    }

    protected void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        try {
            this.delegator.invoke(new Object[]{Integer.valueOf(connectionTime), Integer.valueOf(latency), Integer.valueOf(bandwidth)});
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    protected void shutdownInput() throws IOException {
        try {
            this.delegator.invoke(new Object[0]);
        } catch (IOException var2) {
            throw var2;
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void shutdownOutput() throws IOException {
        try {
            this.delegator.invoke(new Object[0]);
        } catch (IOException var2) {
            throw var2;
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected boolean supportsUrgentData() {
        try {
            Boolean e = (Boolean)this.delegator.invoke(new Object[0]);
            return e.booleanValue();
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public String getMethod() {
        return this.method;
    }

    public boolean isHttp() {
        return this.http;
    }

    private void setProtocolFromPort(int port) {
        if(port == 80) {
            this.protocol = "HTTP";
        } else if(port == 443) {
            this.protocol = "HTTPS";
        }

    }
}
