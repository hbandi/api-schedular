package com.hb.schedular.task;

import java.io.Serializable;

public class Task  implements Serializable {

    private String id;
    private String context;
    private String schedule;
    private String endPoint;
    private String successCallBackEndPoint;
    private String failureCallBackEndPoint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getSuccessCallBackEndPoint() {
        return successCallBackEndPoint;
    }

    public void setSuccessCallBackEndPoint(String successCallBackEndPoint) {
        this.successCallBackEndPoint = successCallBackEndPoint;
    }

    public String getFailureCallBackEndPoint() {
        return failureCallBackEndPoint;
    }

    public void setFailureCallBackEndPoint(String failureCallBackEndPoint) {
        this.failureCallBackEndPoint = failureCallBackEndPoint;
    }
}
