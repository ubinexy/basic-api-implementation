package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    @Scope(value = "prototype")
    public RsService rsServiceBeanFactory(UserRepository userRepository,RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        return new RsService(userRepository, rsEventRepository, voteRepository);
    }
}
