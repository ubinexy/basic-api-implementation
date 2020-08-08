package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "Vote")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    @Id
    @GeneratedValue
    private int id;
    private LocalDateTime localDateTime;
    private int num;
    @ManyToOne @JoinColumn(name = "user_id")
    private UserDto user;
    @ManyToOne @JoinColumn(name = "rs_event_id")
    private RsEventDto rsEvent;
}
