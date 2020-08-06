package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        User defaultUser = new User("default", "male", 18, "default@email.com", "18888888888");
        UserDto userDto = UserDto.builder()
                .username(defaultUser.getUsername())
                .gender(defaultUser.getGender())
                .age(defaultUser.getAge())
                .email(defaultUser.getEmail())
                .phone(defaultUser.getPhone())
                .build();
        userRepository.save(userDto);
    }

    @Test
    void should_add_an_event_to_database() throws Exception {
        RsEvent expectEvent = new RsEvent("Price raised", "none", 1);

        String jsonString = "{\"eventName\":\"Price raised\",\"keyword\":\"none\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventDto> all = rsEventRepository.findAll();
        RsEventDto actualEvent = all.get(0);
        assertEquals(expectEvent.getEventName(), actualEvent.getEventName());
        assertEquals(expectEvent.getKeyword(), actualEvent.getKeyword());
        assertEquals(expectEvent.getUserId(), actualEvent.getUserId());
    }

    @Test
    void should_return_400_event_when_add_event_given_user_id_not_exist() throws Exception {
        String jsonString = "{\"eventName\":\"热搜事件名\",\"keyword\":\"关键字\",\"userId\":100}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
