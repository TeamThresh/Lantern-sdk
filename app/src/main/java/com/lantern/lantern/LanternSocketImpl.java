package com.lantern.lantern;

import android.util.Log;

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

    public LanternSocketImpl() {
        //this.method = ReflectionUtil.extractCallingMethod(SYSTEM_PACKAGES);
        //this.http = ReflectionUtil.callingClassAnyOf(HTTP_CLASSES);
        this.delegator = new Delegator(this, SocketImpl.class, "java.net.PlainSocketImpl");
        //this.registry = registry;
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
/*
    public void readingDone() {
        if(!this.readingDone) {
            this.readingDone = true;
            if(Properties.isKitKat) {
                if(this.mOutputStreamMonitor != null && this.mInputStreamMonitorKitKat != null) {
                    this.createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), this.mOutputStreamMonitor.getHeaders(), this.mInputStreamMonitorKitKat.getHeaders(), System.currentTimeMillis(), this.mInputStreamMonitorKitKat.exception, (String)null);
                }
            } else if(this.mOutputStreamMonitor != null && this.mInputStreamMonitor != null) {
                this.createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), this.mOutputStreamMonitor.getHeaders(), this.mInputStreamMonitor.getHeaders(), System.currentTimeMillis(), this.mInputStreamMonitor.exception, (String)null);
            }
        }

    }*/
/*

    private void createActionEventFromCollectedStats(ArrayList<Metric<?>> metrics, HashMap<String, List<String>> outputHeaders, HashMap<String, List<String>> inputHeaders, long endTime, String exception, String exceptionUrl) {
        String url = "";
        if(outputHeaders != null) {
            try {
                url = (String)((List)outputHeaders.get("Host")).get(0);
            } catch (Exception var18) {
                ;
            }

            try {
                url = url + (String)((List)outputHeaders.get("splk-host2")).get(0);
            } catch (Exception var17) {
                ;
            }
        }

        if(exception != null) {
            url = exceptionUrl;
        }

        int statuscode = 0;
        if(inputHeaders != null) {
            try {
                statuscode = Integer.valueOf((String)((List)inputHeaders.get("splk-statuscode")).get(0)).intValue();
            } catch (Exception var16) {
                ;
            }
        }

        Long bytesOut = Long.valueOf(0L);
        long bytesIn = 0L;
        Iterator e = metrics.iterator();

        while(e.hasNext()) {
            Metric m = (Metric)e.next();
            if(m instanceof Counter) {
                if(((Counter)m).getName().endsWith("-bytes-out")) {
                    bytesOut = (Long)m.getValue();
                } else if(((Counter)m).getName().endsWith("-bytes-in")) {
                    bytesIn = ((Long)m.getValue()).longValue();
                }
            }
        }

        try {
            bytesIn = Long.valueOf((String)((List)inputHeaders.get("Content-Length")).get(0)).longValue();
        } catch (Exception var15) {
            Logger.logInfo("Could not read the Content-Length HTTP header value");
        }

        NetLogManager.getInstance().logNetworkRequest(url, this.protocol, this.startTime, endTime, statuscode, bytesOut.longValue(), bytesIn, exception, (HashMap)null);
    }
*/

    protected InputStream getInputStream() throws IOException {
        InputStream stream = null;

        try {
            stream = (InputStream)this.delegator.invoke(new Object[0]);
        } catch (Exception var3) {
            if(var3 instanceof IOException) {
                throw (IOException)var3;
            }
        }
        return stream;
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
/*
        if(Properties.isKitKat) {
            if(this.mInputStreamMonitorKitKat != null) {
                this.mInputStreamMonitorKitKat.close();
            }
        } else if(this.mInputStreamMonitor != null) {
            this.mInputStreamMonitor.close();
        }
*/

        try {
            this.delegator.invoke(new Object[0]);
        } catch (IOException var2) {
            throw var2;
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void shutdownOutput() throws IOException {
        /*if(this.mOutputStreamMonitor != null) {
            this.mOutputStreamMonitor.close();
        }
*/
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
    /*
    // For SOCKS support. A SOCKS bind() uses the last
    // host connected to in its request.
    private static InetAddress lastConnectedAddress;
    private static int lastConnectedPort;
    private boolean streaming = true;
    private boolean shutdownInput;
    private Proxy proxy;
    private final CloseGuard guard = CloseGuard.get();
    public PlainSocketImpl(FileDescriptor fd) {
        this.fd = fd;
        if (fd.valid()) {
            guard.open("close");
        }
    }
    public PlainSocketImpl(Proxy proxy) {
        this(new FileDescriptor());
        this.proxy = proxy;
    }
    public PlainSocketImpl() {
        this(new FileDescriptor());
    }
    public PlainSocketImpl(FileDescriptor fd, int localport, InetAddress addr, int port) {
        this.fd = fd;
        this.localport = localport;
        this.address = addr;
        this.port = port;
        if (fd.valid()) {
            guard.open("close");
        }
    }
    @Override
    protected void accept(SocketImpl newImpl) throws IOException {
        if (usingSocks()) {
            ((PlainSocketImpl) newImpl).socksBind();
            ((PlainSocketImpl) newImpl).socksAccept();
            return;
        }
        Platform.NETWORK.accept(fd, newImpl, newImpl.getFileDescriptor());
    }
    private boolean usingSocks() {
        return proxy != null && proxy.type() == Proxy.Type.SOCKS;
    }
    public void initLocalPort(int localPort) {
        this.localport = localPort;
    }
    public void initRemoteAddressAndPort(InetAddress remoteAddress, int remotePort) {
        this.address = remoteAddress;
        this.port = remotePort;
    }
    private void checkNotClosed() throws IOException {
        if (!fd.valid()) {
            throw new SocketException("Socket is closed");
        }
    }
    @Override
    protected synchronized int available() throws IOException {
        checkNotClosed();
        // we need to check if the input has been shutdown. If so
        // we should return that there is no data to be read
        if (shutdownInput) {
            return 0;
        }
        return IoUtils.available(fd);
    }
    @Override
    protected void bind(InetAddress address, int port) throws IOException {
        Platform.NETWORK.bind(fd, address, port);
        this.address = address;
        if (port != 0) {
            this.localport = port;
        } else {
            this.localport = IoUtils.getSocketLocalPort(fd);
        }
    }
    @Override
    protected synchronized void close() throws IOException {
        guard.close();
        Platform.NETWORK.close(fd);
    }
    @Override
    protected void connect(String aHost, int aPort) throws IOException {
        connect(InetAddress.getByName(aHost), aPort);
    }
    @Override
    protected void connect(InetAddress anAddr, int aPort) throws IOException {
        connect(anAddr, aPort, 0);
    }
    *//**
     * Connects this socket to the specified remote host address/port.
     *
     * @param anAddr
     *            the remote host address to connect to
     * @param aPort
     *            the remote port to connect to
     * @param timeout
     *            a timeout where supported. 0 means no timeout
     * @throws IOException
     *             if an error occurs while connecting
     *//*
    private void connect(InetAddress anAddr, int aPort, int timeout) throws IOException {
        InetAddress normalAddr = anAddr.isAnyLocalAddress() ? InetAddress.getLocalHost() : anAddr;
        try {
            if (streaming && usingSocks()) {
                socksConnect(anAddr, aPort, 0);
            } else {
                IoUtils.connect(fd, normalAddr, aPort, timeout);
            }
        } catch (ConnectException e) {
            throw new ConnectException(anAddr + " (port " + aPort + "): " + e.getMessage());
        }
        super.address = normalAddr;
        super.port = aPort;
    }
    @Override
    protected void create(boolean streaming) throws IOException {
        this.streaming = streaming;
        this.fd = IoUtils.socket(streaming);
    }
    @Override protected void finalize() throws Throwable {
        try {
            if (guard != null) {
                guard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }
    @Override protected synchronized InputStream getInputStream() throws IOException {
        checkNotClosed();
        return new PlainSocketInputStream(this);
    }
    private static class PlainSocketInputStream extends InputStream {
        private final PlainSocketImpl socketImpl;
        public PlainSocketInputStream(PlainSocketImpl socketImpl) {
            this.socketImpl = socketImpl;
        }
        @Override public int available() throws IOException {
            return socketImpl.available();
        }
        @Override public void close() throws IOException {
            socketImpl.close();
        }
        @Override public int read() throws IOException {
            return Streams.readSingleByte(this);
        }
        @Override public int read(byte[] buffer, int offset, int byteCount) throws IOException {
            return socketImpl.read(buffer, offset, byteCount);
        }
    }
    @Override public Object getOption(int option) throws SocketException {
        return IoUtils.getSocketOption(fd, option);
    }
    @Override protected synchronized OutputStream getOutputStream() throws IOException {
        checkNotClosed();
        return new PlainSocketOutputStream(this);
    }
    private static class PlainSocketOutputStream extends OutputStream {
        private final PlainSocketImpl socketImpl;
        public PlainSocketOutputStream(PlainSocketImpl socketImpl) {
            this.socketImpl = socketImpl;
        }
        @Override public void close() throws IOException {
            socketImpl.close();
        }
        @Override public void write(int oneByte) throws IOException {
            Streams.writeSingleByte(this, oneByte);
        }
        @Override public void write(byte[] buffer, int offset, int byteCount) throws IOException {
            socketImpl.write(buffer, offset, byteCount);
        }
    }
    @Override
    protected void listen(int backlog) throws IOException {
        if (usingSocks()) {
            // Do nothing for a SOCKS connection. The listen occurs on the
            // server during the bind.
            return;
        }
        try {
            Libcore.os.listen(fd, backlog);
        } catch (ErrnoException errnoException) {
            throw errnoException.rethrowAsSocketException();
        }
    }
    @Override
    public void setOption(int option, Object value) throws SocketException {
        IoUtils.setSocketOption(fd, option, value);
    }
    *//**
     * Gets the SOCKS proxy server port.
     *//*
    private int socksGetServerPort() {
        // get socks server port from proxy. It is unnecessary to check
        // "socksProxyPort" property, since proxy setting should only be
        // determined by ProxySelector.
        InetSocketAddress addr = (InetSocketAddress) proxy.address();
        return addr.getPort();
    }
    *//**
     * Gets the InetAddress of the SOCKS proxy server.
     *//*
    private InetAddress socksGetServerAddress() throws UnknownHostException {
        String proxyName;
        // get socks server address from proxy. It is unnecessary to check
        // "socksProxyHost" property, since all proxy setting should be
        // determined by ProxySelector.
        InetSocketAddress addr = (InetSocketAddress) proxy.address();
        proxyName = addr.getHostName();
        if (proxyName == null) {
            proxyName = addr.getAddress().getHostAddress();
        }
        return InetAddress.getByName(proxyName);
    }
    *//**
     * Connect using a SOCKS server.
     *//*
    private void socksConnect(InetAddress applicationServerAddress, int applicationServerPort, int timeout) throws IOException {
        try {
            IoUtils.connect(fd, socksGetServerAddress(), socksGetServerPort(), timeout);
        } catch (Exception e) {
            throw new SocketException("SOCKS connection failed", e);
        }
        socksRequestConnection(applicationServerAddress, applicationServerPort);
        lastConnectedAddress = applicationServerAddress;
        lastConnectedPort = applicationServerPort;
    }
    *//**
     * Request a SOCKS connection to the application server given. If the
     * request fails to complete successfully, an exception is thrown.
     *//*
    private void socksRequestConnection(InetAddress applicationServerAddress,
                                        int applicationServerPort) throws IOException {
        socksSendRequest(Socks4Message.COMMAND_CONNECT,
                applicationServerAddress, applicationServerPort);
        Socks4Message reply = socksReadReply();
        if (reply.getCommandOrResult() != Socks4Message.RETURN_SUCCESS) {
            throw new IOException(reply.getErrorString(reply.getCommandOrResult()));
        }
    }
    *//**
     * Perform an accept for a SOCKS bind.
     *//*
    public void socksAccept() throws IOException {
        Socks4Message reply = socksReadReply();
        if (reply.getCommandOrResult() != Socks4Message.RETURN_SUCCESS) {
            throw new IOException(reply.getErrorString(reply.getCommandOrResult()));
        }
    }
    *//**
     * Shutdown the input portion of the socket.
     *//*
    @Override
    protected void shutdownInput() throws IOException {
        shutdownInput = true;
        try {
            Libcore.os.shutdown(fd, SHUT_RD);
        } catch (ErrnoException errnoException) {
            throw errnoException.rethrowAsSocketException();
        }
    }
    *//**
     * Shutdown the output portion of the socket.
     *//*
    @Override
    protected void shutdownOutput() throws IOException {
        try {
            Libcore.os.shutdown(fd, SHUT_WR);
        } catch (ErrnoException errnoException) {
            throw errnoException.rethrowAsSocketException();
        }
    }
    *//**
     * Bind using a SOCKS server.
     *//*
    private void socksBind() throws IOException {
        try {
            IoUtils.connect(fd, socksGetServerAddress(), socksGetServerPort());
        } catch (Exception e) {
            throw new IOException("Unable to connect to SOCKS server", e);
        }
        // There must be a connection to an application host for the bind to work.
        if (lastConnectedAddress == null) {
            throw new SocketException("Invalid SOCKS client");
        }
        // Use the last connected address and port in the bind request.
        socksSendRequest(Socks4Message.COMMAND_BIND, lastConnectedAddress,
                lastConnectedPort);
        Socks4Message reply = socksReadReply();
        if (reply.getCommandOrResult() != Socks4Message.RETURN_SUCCESS) {
            throw new IOException(reply.getErrorString(reply.getCommandOrResult()));
        }
        // A peculiarity of socks 4 - if the address returned is 0, use the
        // original socks server address.
        if (reply.getIP() == 0) {
            address = socksGetServerAddress();
        } else {
            // IPv6 support not yet required as
            // currently the Socks4Message.getIP() only returns int,
            // so only works with IPv4 4byte addresses
            byte[] replyBytes = new byte[4];
            Memory.pokeInt(replyBytes, 0, reply.getIP(), ByteOrder.BIG_ENDIAN);
            address = InetAddress.getByAddress(replyBytes);
        }
        localport = reply.getPort();
    }
    *//**
     * Send a SOCKS V4 request.
     *//*
    private void socksSendRequest(int command, InetAddress address, int port) throws IOException {
        Socks4Message request = new Socks4Message();
        request.setCommandOrResult(command);
        request.setPort(port);
        request.setIP(address.getAddress());
        request.setUserId("default");
        getOutputStream().write(request.getBytes(), 0, request.getLength());
    }
    *//**
     * Read a SOCKS V4 reply.
     *//*
    private Socks4Message socksReadReply() throws IOException {
        Socks4Message reply = new Socks4Message();
        int bytesRead = 0;
        while (bytesRead < Socks4Message.REPLY_LENGTH) {
            int count = getInputStream().read(reply.getBytes(), bytesRead,
                    Socks4Message.REPLY_LENGTH - bytesRead);
            if (count == -1) {
                break;
            }
            bytesRead += count;
        }
        if (Socks4Message.REPLY_LENGTH != bytesRead) {
            throw new SocketException("Malformed reply from SOCKS server");
        }
        return reply;
    }
    @Override
    protected void connect(SocketAddress remoteAddr, int timeout) throws IOException {
        InetSocketAddress inetAddr = (InetSocketAddress) remoteAddr;
        connect(inetAddr.getAddress(), inetAddr.getPort(), timeout);
    }
    @Override
    protected boolean supportsUrgentData() {
        return true;
    }
    @Override
    protected void sendUrgentData(int value) throws IOException {
        Platform.NETWORK.sendUrgentData(fd, (byte) value);
    }
    *//**
     * For PlainSocketInputStream.
     *//*
    private int read(byte[] buffer, int offset, int byteCount) throws IOException {
        if (byteCount == 0) {
            return 0;
        }
        Arrays.checkOffsetAndCount(buffer.length, offset, byteCount);
        if (shutdownInput) {
            return -1;
        }
        int read = Platform.NETWORK.read(fd, buffer, offset, byteCount);
        // Return of zero bytes for a blocking socket means a timeout occurred
        if (read == 0) {
            throw new SocketTimeoutException();
        }
        // Return of -1 indicates the peer was closed
        if (read == -1) {
            shutdownInput = true;
        }
        return read;
    }
    *//**
     * For PlainSocketOutputStream.
     *//*
    private void write(byte[] buffer, int offset, int byteCount) throws IOException {
        Arrays.checkOffsetAndCount(buffer.length, offset, byteCount);
        if (streaming) {
            while (byteCount > 0) {
                int bytesWritten = Platform.NETWORK.write(fd, buffer, offset, byteCount);
                byteCount -= bytesWritten;
                offset += bytesWritten;
            }
        } else {
            // Unlike writes to a streaming socket, writes to a datagram
            // socket are all-or-nothing, so we don't need a loop here.
            // http://code.google.com/p/android/issues/detail?id=15304
            Platform.NETWORK.send(fd, buffer, offset, byteCount, port, address);
        }
    }*/
}
