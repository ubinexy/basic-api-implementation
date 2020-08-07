package com.thoughtworks.rslist.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyword;
    @NotNull
    private int userId;

    public RsEvent(@NotNull String eventName, @NotNull String keyword, @NotNull int userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }

    public RsEvent() {
    }
}
