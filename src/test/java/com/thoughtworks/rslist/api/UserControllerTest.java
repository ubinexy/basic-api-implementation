package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void setd() {
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void should_add_user_to_database() throws Exception {
        User user = new User("default", "male", 18, "default@email.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserDto> all = userRepository.findAll();
        UserDto actualUser = all.get(0);
        assertEquals(user.getUsername(), actualUser.getUsername());
        assertEquals(user.getGender(), actualUser.getGender());
        assertEquals(user.getAge(), actualUser.getAge());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getPhone(), actualUser.getPhone());
    }

    @Test
    void should_get_user_from_database_by_user_id() throws Exception {
        UserDto userDto = UserDto.builder()
                .username("default")
                .gender("male")
                .age(18)
                .email("default@email.com")
                .phone("18888888888")
                .build();
        userRepository.save(userDto);

        mockMvc.perform(get("/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name", is("default")))
                .andExpect(jsonPath("$.user_age", is(18)))
                .andExpect(jsonPath("$.user_gender", is("male")))
                .andExpect(jsonPath("$.user_email", is("default@email.com")))
                .andExpect(jsonPath("$.user_phone", is("18888888888")));
    }

    @Test
    void should_delete_user_by_user_id() throws Exception {
        UserDto userBob = UserDto.builder()
                .username("Bob")
                .gender("male")
                .age(18)
                .email("bob@email.com")
                .phone("18888888888")
                .build();

        UserDto userAlice = UserDto.builder()
                .username("Alice")
                .gender("female")
                .age(36)
                .email("alice@email.com")
                .phone("13333333333")
                .build();

        userRepository.save(userBob);
        userRepository.save(userAlice);

        mockMvc.perform(delete("/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<UserDto> users = userRepository.findAll();
        UserDto remainUser = users.get(0);
        assertEquals(2, remainUser.getId());
        assertEquals("Alice" , remainUser.getUsername());
        assertEquals("female", remainUser.getGender());
        assertEquals(36, remainUser.getAge());
        assertEquals("alice@email.com", remainUser.getEmail());
        assertEquals("13333333333", remainUser.getPhone());
    }

    @Test
    void should_delete_corresponding_event_when_delete_user() throws Exception {
        UserDto userBob = UserDto.builder()
                .username("Bob")
                .gender("male")
                .age(18)
                .email("bob@email.com")
                .phone("18888888888")
                .build();



        mockMvc.perform(delete("/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
