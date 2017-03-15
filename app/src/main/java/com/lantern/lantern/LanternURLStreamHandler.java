package com.lantern.lantern;

import android.util.Log;

import com.lantern.lantern.util.Logger;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Created by YS on 2017-02-06.
 */

public class LanternURLStreamHandler extends URLStreamHandler {
    private String protocol = "";
    public LanternURLStreamHandler(String protocol) {
        this.protocol = protocol;
    }

    protected URLConnection openConnection(URL url) throws IOException {
        Logger.d("URL INFO", url.toString());

        return new LanternHttpURLConnection(url);
    }

    protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        if(proxy == null) {
            return this.openConnection(url);
        } else {

            Logger.d("URL INFO", url.toString());
            return new LanternHttpURLConnection(url, proxy);
        }
    }

    @Override
    protected int getDefaultPort() {
        if (protocol.equals("http")) return 80;
        if (protocol.equals("https")) return 443;
        throw new AssertionError();
    }
}
