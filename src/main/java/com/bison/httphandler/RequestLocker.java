package com.bison.httphandler;

public class RequestLocker {
    public String trackerId;
    public Object response;

    public RequestLocker(String trackerId, Object response) {
        this.trackerId = trackerId;
        this.response = response;
    }
}
