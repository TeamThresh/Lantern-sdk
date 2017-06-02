package com.lantern.lantern.eventpath;

/**
 * Created by yky on 2017. 5. 11..
 */

public class EventPathItem {
    private String datetime;
    private String className;
    private String methodName;
    private String eventLabel;
    private int lineNum;

    public EventPathItem(String datetime, String className, String methodName, int lineNum, String eventLabel) {
        this.datetime = datetime;
        this.className = className;
        this.methodName = methodName;
        this.lineNum = lineNum;
        this.eventLabel = eventLabel;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    public void setEventLabel(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
