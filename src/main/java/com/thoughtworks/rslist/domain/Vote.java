package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
public class Vote {
    private int userId;
    private int rsEventId;
    private LocalDateTime voteTime;
    private int voteNum;

    public Vote(int userId, String voteTime, int voteNum) {
        this.userId = userId;
        this.voteTime = LocalDateTime.parse("2017-09-28 17:07:05");
        this.voteNum = voteNum;
    }

    public Vote() {
    }
}