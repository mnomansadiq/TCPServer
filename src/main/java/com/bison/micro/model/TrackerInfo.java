package com.bison.micro.model;

public class TrackerInfo {

    private final long id;

    private final String content;

    public TrackerInfo(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
