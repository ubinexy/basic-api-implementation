package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@Builder
public class Vote {
    @NotNull
    private int userId;
    @NotNull
    private int rsEventId;
    @NotNull
    @JsonProperty("vote_dateTime")
    @JsonAlias("voteTime")
    private LocalDateTime voteTime;
    @NotNull
    @JsonProperty("vote_number")
    @JsonAlias("voteNum")
    private int voteNum;

    public Vote(int userId, int rsEventId, LocalDateTime voteTime, int voteNum) {
        this.userId = userId;
        this.voteTime = voteTime;
        this.voteNum = voteNum;
        this.rsEventId = rsEventId;

    }

    public Vote() {
    }
}