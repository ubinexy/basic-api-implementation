package com.thoughtworks.rslist;
import com.fasterxml.jackson.databind.MapperFeature;
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
                .andExpect(status().isCreated());

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

    @Test
    void should_update_user_list_when_add_an_event_given_a_new_user() throws Exception {
        User user = new User("shiqi", "male", 20, "abc@twu.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":" + objectMapper.writeValueAsString(user) + "}";

        mvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("default")))
                .andExpect(jsonPath("$[1].username", is("shiqi")));
    }


    @Test
    void should_return_400_when_add_an_event_given_null_eventName() throws Exception {
        User user = new User("shiqi", "male", 20, "ab@twu.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = "{\"keyword\":\"经济\",\"user\":" + objectMapper.writeValueAsString(user) + "}";

        mvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_add_an_event_given_null_keyword() throws Exception {
        User user = new User("shiqi", "male", 20, "ab@twu.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = "{\"eventName\":\"涨价了\",\"user\":" + objectMapper.writeValueAsString(user) + "}";

        mvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_add_an_event_given_null_user() throws Exception {
        String jsonStr = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\"}";

        mvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_add_an_event_given_username_longer_than_8_characters() throws Exception {
        String longName = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"username\":\"long_name\",\"gender\":\"male\",\"age\":18,\"email\":\"a@b.com\",\"phone\":\"12345678901\"}}";
        mvc.perform(post("/rs/event").content(longName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_add_an_event_given_username_is_null() throws Exception {
        String longName = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"gender\":\"male\",\"age\":18,\"email\":\"a@b.com\",\"phone\":\"12345678901\"}}";
        mvc.perform(post("/rs/event").content(longName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_add_an_event_given_gender_is_null() throws Exception {
        String jsonStr = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"username\":\"shiqi\",\"age\":18,\"email\":\"a@b.com\",\"phone\":\"12345678901\"}}";

        mvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_add_an_event_given_user_age_out_of_range() throws Exception {

        String ageUnder18 = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"username\":\"shiqi\",\"gender\":\"male\",\"age\":17,\"email\":\"a@b.com\",\"phone\":\"12345678901\"}}";
        mvc.perform(post("/rs/event").content(ageUnder18).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

        String ageOver100 = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"username\":\"shiqi\",\"gender\":\"male\",\"age\":101,\"email\":\"a@b.com\",\"phone\":\"12345678901\"}}";
        mvc.perform(post("/rs/event").content(ageOver100).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_add_an_event_given_user_age_is_null() throws Exception {
        String jsonStr = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"username\":\"shiqi\",\"gender\":\"male\",\"email\":\"a@b.com\",\"phone\":\"12345678901\"}}";
        mvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

    }

    @Test
    void should_return_400_when_add_an_event_given_incorrect_email_format() throws Exception {
        String format1 = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"name\":\"shiqi\",\"gender\":\"male\",\"age\":18,\"email\":\"@b.com\",\"phone\":\"12345678901\"}}";
        String format2 = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"name\":\"shiqi\",\"gender\":\"male\",\"age\":18,\"email\":\"a@\",\"phone\":\"12345678901\"}}";
        String format3 = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"name\":\"shiqi\",\"gender\":\"male\",\"age\":18,\"email\":\"ab.com\",\"phone\":\"12345678901\"}}";

        mvc.perform(post("/rs/event").content(format1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

        mvc.perform(post("/rs/event").content(format2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

        mvc.perform(post("/rs/event").content(format3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_add_an_event_given_incorrect_phone_number_format() throws Exception {
        String notStartWith1 = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"name\":\"shiqi\",\"gender\":\"male\",\"age\":18,\"email\":\"a@b.com\",\"phone\":\"22345678901\"}}";
        String lessThan11digits = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"name\":\"shiqi\",\"gender\":\"male\",\"age\":18,\"email\":\"a@b.com\",\"phone\":\"1234567890\"}}";
        String moreThan11digits = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"name\":\"shiqi\",\"gender\":\"male\",\"age\":18,\"email\":\"a@b.com\",\"phone\":\"123456789012\"}}";
        String nonDigitalCharacter = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"name\":\"shiqi\",\"gender\":\"male\",\"age\":18,\"email\":\"a@b.com\",\"phone\":\"123-4567890\"}}";

        mvc.perform(post("/rs/event").content(notStartWith1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

        mvc.perform(post("/rs/event").content(lessThan11digits).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

        mvc.perform(post("/rs/event").content(moreThan11digits).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

        mvc.perform(post("/rs/event").content(nonDigitalCharacter).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_add_an_event_given_phone_number_is_null() throws Exception {
        String jsonStr = "{\"eventName\":\"涨价了\",\"keyword\":\"经济\",\"user\":{\"name\":\"shiqi\",\"gender\":\"male\",\"age\":18,\"email\":\"a@b.com\"}}";

        mvc.perform(post("/rs/event").content(jsonStr).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }
}
