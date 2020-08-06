package com.thoughtworks.rslist.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Vote {
    private int userId;
    private int rsEventId;
    private LocalDateTime time;
    private int voteNum;
}
