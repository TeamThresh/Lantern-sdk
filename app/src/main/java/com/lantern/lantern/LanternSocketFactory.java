package com.lantern.lantern;

import android.util.Log;

import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YS on 2017-02-07.
 */

public class LanternSocketFactory implements SocketImplFactory
{
    private List<SocketImpl> _openSockets;

    public LanternSocketFactory() {
        _openSockets = new LinkedList<>();
    }

    @Override
    public SocketImpl createSocketImpl() {

        Log.d("Socket Factory", "create");
        return new LanternSocketImpl();
        /*try {
            Constructor cons = null;
            cons = Class.forName("java.net.PlainSocketImpl").getDeclaredConstructor();
            cons.setAccessible(true);
            SocketImpl socketImpl = (SocketImpl) cons.newInstance();
            Log.d("SOCKET", socketImpl.toString());
            _openSockets.add(socketImpl);
            return socketImpl;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;*/
    }
/*
    public void closeAll() {
        for (SocketImpl socket : _openSockets) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static LanternSocketFactory register()
    {
        LanternSocketFactory fact =  new LanternSocketFactory();
        try {
            Socket.setSocketImplFactory(fact);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fact;
    }*/
}
