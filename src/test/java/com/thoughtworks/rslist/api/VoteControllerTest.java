package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private UserDto defaultUser;
    private RsEventDto eventDto;

    @BeforeEach
    private void setUp() {
        voteRepository.deleteAll();
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        defaultUser = UserDto.builder()
                .username("default")
                .gender("male")
                .age(18)
                .email("default@email.com")
                .phone("18888888888")
                .voteNum(10)
                .build();
        userRepository.save(defaultUser);

        eventDto = RsEventDto.builder()
                .eventName("热搜事件名")
                .keyword("关键字")
                .userDto(defaultUser)
                .build();
        rsEventRepository.save(eventDto);

        VoteDto voteDto1 = VoteDto.builder()
                .num(2)
                .localDateTime(LocalDateTime.parse("2020-08-01T10:00:00", formatter))
                .user(defaultUser)
                .rsEvent(eventDto)
                .build();

        VoteDto voteDto2 = VoteDto.builder()
                .num(4)
                .localDateTime(LocalDateTime.parse("2020-08-02T10:00:00", formatter))
                .user(defaultUser)
                .rsEvent(eventDto)
                .build();

        voteRepository.save(voteDto1);
        voteRepository.save(voteDto2);
    }

    @Test
    void should_vote_success_given_voteNum_less_then_number_of_tickets_left() throws Exception {

        UserDto user = userRepository.findById(defaultUser.getId()).get();
        assertEquals(10, user.getVoteNum());

        String firstVote = "{\"voteNum\":3,\"userId\":"+defaultUser.getId() +",\"voteTime\":\""+ printTime() + "\"}";
        mockMvc.perform(post("/rs/vote/"+ eventDto.getId()).content(firstVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        user = userRepository.findById(defaultUser.getId()).get();
        assertEquals(7, user.getVoteNum());

        String SecondVote = "{\"voteNum\":8,\"userId\":"+defaultUser.getId() +",\"voteTime\":\""+ printTime() + "\"}";
        mockMvc.perform(post("/rs/vote/"+ eventDto.getId()).content(SecondVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String printTime() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }

    @Test
    void should_return_votes_between_start_and_end_dateTime() throws Exception {
        String startDateTime = "2020-08-01T09:00:00";
        String endDateTime = "2020-08-01T11:00:00";

        mockMvc.perform(get("/rs/vote?page=1")
                .param("from", startDateTime)
                .param("to",endDateTime)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vote_dateTime", is("2020-08-01T10:00:00")))
                .andExpect(jsonPath("$[0].vote_number", is(2)));

        endDateTime = "2020-08-02T11:00:00";

        mockMvc.perform(get("/rs/vote?page=1")
                .param("from", startDateTime)
                .param("to",endDateTime)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vote_dateTime", is("2020-08-01T10:00:00")))
                .andExpect(jsonPath("$[0].vote_number", is(2)))
                .andExpect(jsonPath("$[1].vote_dateTime", is("2020-08-02T10:00:00")))
                .andExpect(jsonPath("$[1].vote_number", is(4)));
    }
}
