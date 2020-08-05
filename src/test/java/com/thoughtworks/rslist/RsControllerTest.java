package com.thoughtworks.rslist;
import com.sun.xml.internal.ws.api.model.MEP;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.api.RsController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class RsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RsController rsController;

    @BeforeEach
    public void setUp() {
        rsController.rsList = rsController.initRsEventList();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void should_return_rsList_when_get_event_List() throws Exception {

        mvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName").value("事件1"))
                .andExpect(jsonPath("$[0].keyword").value("无"))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName").value("事件2"))
                .andExpect(jsonPath("$[1].keyword").value("无"))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName").value("事件3"))
                .andExpect(jsonPath("$[2].keyword").value("无"))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))));

        mvc.perform(get("/rs/list?start=1&end=2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName").value("事件1"))
                .andExpect(jsonPath("$[0].keyword").value("无"))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName").value("事件2"))
                .andExpect(jsonPath("$[1].keyword").value("无"))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))));
    }

    @Test
    void should_return_event_when_get_event_by_id() throws Exception {
        mvc.perform(get("/rs/event/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value("事件3"))
                .andExpect(jsonPath("$.keyword").value("无"));
    }


    @Test
    void should_update_rslist_when_modify_an_event() throws Exception {
        String jsonStr = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"username\":\"shiqi\",\"gender\":\"male\",\"age\":20,\"email\":\"default@default.com\",\"phone\":\"18888888888\"}}";

        mvc.perform(patch("/rs/event/1").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName").value("涨价了"))
                .andExpect(jsonPath("$[0].keyword").value("经济"))
                .andExpect(jsonPath("$[1].eventName").value("事件2"))
                .andExpect(jsonPath("$[1].keyword").value("无"))
                .andExpect(jsonPath("$[2].eventName").value("事件3"))
                .andExpect(jsonPath("$[2].keyword").value("无"));
    }

    @Test
    void should_update_rslist_when_add_an_event() throws Exception {
        String jsonStr = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"username\":\"default\",\"gender\":\"male\",\"age\":20,\"email\":\"default@default.com\",\"phone\":\"18888888888\"}}";

        mvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON))
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

    @Test
    void should_update_rslist_when_delete_an_event() throws Exception {
        String jsonStr = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"username\":\"default\",\"gender\":\"male\",\"age\":20,\"email\":\"default@default.com\",\"phone\":\"18888888888\"}}";

        mvc.perform(delete("/rs/event/1").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName").value("事件2"))
                .andExpect(jsonPath("$[0].keyword").value("无"))
                .andExpect(jsonPath("$[1].eventName").value("事件3"))
                .andExpect(jsonPath("$[1].keyword").value("无"));
    }
}
