package com.thoughtworks.rslist;

import com.thoughtworks.rslist.service.RsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class RsListApplicationTests {
    @Autowired
    BeanFactory beanFactory;
    @Test
    void contextLoads() {
        RsService rsService = beanFactory.getBean(RsService.class);
    }

}
