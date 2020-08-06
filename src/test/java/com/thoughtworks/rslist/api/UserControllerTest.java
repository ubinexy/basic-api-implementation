package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
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
}
