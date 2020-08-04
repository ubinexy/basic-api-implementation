package com.thoughtworks.rslist.domain;


public class RsEvent {
    private String eventName;
    private String keyword;

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public RsEvent() {}

    public String getEventName() {
        return eventName;
    }

    public String getKeyword() {
        return keyword;
    }
}
