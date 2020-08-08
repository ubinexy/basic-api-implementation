package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
public class Vote {
    private int userId;
    private int rsEventId;
    private LocalDateTime voteTime;
    private int voteNum;

    public Vote(int userId, String voteTime, int voteNum) {
        this.userId = userId;
        this.voteTime = LocalDateTime.parse(voteTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.voteNum = voteNum;
    }

    public Vote() {
    }
}