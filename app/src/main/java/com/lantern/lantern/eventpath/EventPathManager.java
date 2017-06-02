package com.lantern.lantern.eventpath;

import android.content.Context;

import com.lantern.lantern.RYLA;
import com.lantern.lantern.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yky on 2017. 5. 11..
 */

public class EventPathManager {
    private static EventPathManager eventPathManager;

    private Context mContext;
    private static final int MAX_NUM_EVENT_PATH_ITEM = 10;

    private static EventPathItem eventPath[] = new EventPathItem[MAX_NUM_EVENT_PATH_ITEM];
    private static int eventPathCounter = 0;

    private EventPathManager(Context context) { mContext = context; }

    public static EventPathManager getInstance(Context context) {
        if(eventPathManager == null) {
            eventPathManager = new EventPathManager(context);
        }

        return eventPathManager;
    }

    static synchronized public void createEventPathItem(int stackTraceStep, String label) {
        StackTraceElement[] stackTraceElements = new Exception().getStackTrace();

        EventPathItem eventPathItem = new EventPathItem(Util.getDate(RYLA.getInstance().getContext()),
                stackTraceElements[stackTraceStep].getClassName(),
                stackTraceElements[stackTraceStep].getMethodName(),
                stackTraceElements[stackTraceStep].getLineNumber(),
                label
                );

        shiftEventPath();
        eventPath[MAX_NUM_EVENT_PATH_ITEM - 1] = eventPathItem;
        if(eventPathCounter != MAX_NUM_EVENT_PATH_ITEM) {
            eventPathCounter++;
        }
    }

    static private void shiftEventPath() {
        for(int i=0;i< MAX_NUM_EVENT_PATH_ITEM - 1; i++) {
            eventPath[i] = eventPath[i+1];
        }
    }

    public List<EventPathItem> getEventPathList() {
        List<EventPathItem> eventPathList = new ArrayList<EventPathItem>();

        for(int i = MAX_NUM_EVENT_PATH_ITEM - eventPathCounter; i<MAX_NUM_EVENT_PATH_ITEM;i++) {
            eventPathList.add(eventPath[i]);
        }

        return eventPathList;
    }
}
