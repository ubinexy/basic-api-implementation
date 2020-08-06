package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
public class voteControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder().age().build();

        rsEventDto = RsEventDto.builder(). . ;
        userDto = userRepository.save(userDto);
        VoteDto voteDto = VoteDto.builder().localDateTime(LocalDateTime.now()).rsEvent(rsEventDto).user(userDto).num(5);
        voteRepository.save(voteDto);
    }

    @Test
    void shouldGetVoteRecord() {
        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userDto.getId()))
                .param("rsEventId", String.valueOf(userDto.getId())))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userDto.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventDto.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(5)));
    }

    @Test
    void shouldGetVoteRecord() {
        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userDto.getId()))
                .param("rsEventId", String.valueOf(userDto.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userDto.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventDto.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(5)))
                .andExpect(jsonPath("$[1].voteNum", is(5)))
                .andExpect(jsonPath("$[2].voteNum", is(5)));
    }


}
