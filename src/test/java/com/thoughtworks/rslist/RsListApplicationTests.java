package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsListApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    void should_return_EventList_when_getEventList() throws Exception {

        mvc.perform(get("/event/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName").value("事件1"))
                .andExpect(jsonPath("$[0].keyword").value("无"))
                .andExpect(jsonPath("$[1].eventName").value("事件2"))
                .andExpect(jsonPath("$[1].keyword").value("无"))
                .andExpect(jsonPath("$[2].eventName").value("事件3"))
                .andExpect(jsonPath("$[2].keyword").value("无"));

        mvc.perform(get("/event/list?start=1&end=2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName").value("事件1"))
                .andExpect(jsonPath("$[0].keyword").value("无"))
                .andExpect(jsonPath("$[1].eventName").value("事件2"))
                .andExpect(jsonPath("$[1].keyword").value("无"));
    }

    @Test
    @Order(2)
    void should_return_the_event_when_get_event_given_event_id() throws Exception {
        mvc.perform(get("/event/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value("事件3"))
                .andExpect(jsonPath("$.keyword").value("无"));
    }

    @Test
    @Order(3)
    void should_return_modified_list_when_getEventList_given_add_an_event() throws Exception {
        RsEvent rsEvent = new RsEvent("涨价了", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(rsEvent);

        mvc.perform(post("/add/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/event/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName").value("事件1"))
                .andExpect(jsonPath("$[0].keyword").value("无"))
                .andExpect(jsonPath("$[1].eventName").value("事件2"))
                .andExpect(jsonPath("$[1].keyword").value("无"))
                .andExpect(jsonPath("$[2].eventName").value("事件3"))
                .andExpect(jsonPath("$[2].keyword").value("无"))
                .andExpect(jsonPath("$[3].eventName").value("涨价了"))
                .andExpect(jsonPath("$[3].keyword").value("经济"));
    }
}
