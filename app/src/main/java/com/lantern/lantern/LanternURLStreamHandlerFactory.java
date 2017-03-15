package com.lantern.lantern;

import android.util.Log;

import com.lantern.lantern.util.Logger;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * Created by YS on 2017-02-06.
 */

public class LanternURLStreamHandlerFactory implements URLStreamHandlerFactory {

    public LanternURLStreamHandlerFactory() {
    }

    public URLStreamHandler createURLStreamHandler(String protocol) {
        Logger.d("Custom FACTORY", "실행은 됨?");
        Logger.d("Custom FACTORY", "protocol : "+protocol);
        if (!protocol.equals("http") && !protocol.equals("https")) return null;
        return new LanternURLStreamHandler(protocol);
    }
}
