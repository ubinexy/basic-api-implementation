package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private VoteRepository voteRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    private void setUp() {
        voteRepository.deleteAll();
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        UserDto userDto = UserDto.builder()
                .username("default")
                .gender("male")
                .age(18)
                .email("default@email.com")
                .phone("18888888888")
                .voteNum(10)
                .build();
        userRepository.save(userDto);
    }

    @Test
    void should_vote_success_given_voteNum_less_then_number_of_tickets_left() throws Exception {

        String firstEvent = "{\"eventName\":\"热搜事件名\",\"keyword\":\"关键字\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(firstEvent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        UserDto user = userRepository.findById(1).get();
        assertEquals(10, user.getVoteNum());

        String firstVote = "{\"voteNum\":3,\"userId\":1,\"voteTime\":\""+ printTime() + "\"}";
        mockMvc.perform(post("/rs/vote/1").content(firstVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        user = userRepository.findById(1).get();
        assertEquals(7, user.getVoteNum());

        String SecondVote = "{\"voteNum\":8,\"userId\":1,\"voteTime\":\""+ printTime() + "\"}";
        mockMvc.perform(post("/rs/vote/1").content(SecondVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String printTime() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }
}
