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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private User defaultUser;
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        defaultUser = new User("default", "male", 18, "default@email.com", "18888888888");
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
        RsEvent expectEvent = new RsEvent("热搜事件名", "关键字", 1);

        String jsonString = "{\"eventName\":\"热搜事件名\",\"keyword\":\"关键字\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventDto> all = rsEventRepository.findAll();
        RsEventDto actualEvent = all.get(0);
        assertEquals(expectEvent.getEventName(), actualEvent.getEventName());
        assertEquals(expectEvent.getKeyword(), actualEvent.getKeyword());
    }

    @Test
    void should_return_400_event_when_add_event_given_user_id_not_exist() throws Exception {
        String jsonString = "{\"eventName\":\"热搜事件名\",\"keyword\":\"关键字\",\"userId\":100}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_delete_corresponding_event_when_delete_user() throws Exception {
        String firstEvent = "{\"eventName\":\"热搜事件一\",\"keyword\":\"关键字一\",\"userId\":1}";
        String secondEvent = "{\"eventName\":\"热搜事件二\",\"keyword\":\"关键字二\",\"userId\":1}";

        mockMvc.perform(post("/rs/event").content(firstEvent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/rs/event").content(secondEvent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventDto> eventsBeforeDelete = rsEventRepository.findAll();

        assertEquals(2, eventsBeforeDelete.size());
        assertEquals(eventsBeforeDelete.get(0).getEventName(), "热搜事件一");
        assertEquals(eventsBeforeDelete.get(0).getKeyword(), "关键字一");
        assertEquals(eventsBeforeDelete.get(1).getEventName(), "热搜事件二");
        assertEquals(eventsBeforeDelete.get(1).getKeyword(), "关键字二");

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());

        List<RsEventDto> eventsAfterDelete = rsEventRepository.findAll();
        assertEquals(0, eventsAfterDelete.size());
    }
}
