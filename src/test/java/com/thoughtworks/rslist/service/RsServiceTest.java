package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RsServiceTest {
    @Autowired
    BeanFactory beanFactory;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RsEventRepository rsEventRepository;
    @Mock
    private VoteRepository voteRepository;
    private RsEventDto eventDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userDto = UserDto.builder()
                .id(1)
                .username("default")
                .gender("male")
                .age(20)
                .email("default@email.com")
                .phone("18888888888")
                .voteNum(10)
                .build();

        eventDto = RsEventDto.builder()
                .id(1)
                .eventName("热搜事件名")
                .keyword("关键字")
                .voteNum(0)
                .build();
    }

    @Test
    void should_vote_success() {
        // Given
        RsService rsService = beanFactory.getBean(RsService.class, userRepository, rsEventRepository, voteRepository);

        LocalDateTime now = LocalDateTime.now();
        Vote vote = Vote.builder()
                .userId(1)
                .rsEventId(1)
                .voteNum(3)
                .voteTime(now)
                .build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userDto));
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(eventDto));

        // When
        rsService.vote(vote);

        // Then
        assertEquals(7, userDto.getVoteNum());
        assertEquals(3, eventDto.getVoteNum());
        verify(userRepository).save(userDto);
        verify(rsEventRepository).save(eventDto);
        verify(voteRepository).save(VoteDto.builder()
                .user(userDto)
                .rsEvent(eventDto)
                .num(3)
                .localDateTime(now)
                .build()
        );
    }

    @Test
    void should_throw_exception_when_voteNum_is_greater_than_user_has() {
        // Given
        RsService rsService = beanFactory.getBean(RsService.class, userRepository, rsEventRepository, voteRepository);

        LocalDateTime now = LocalDateTime.now();
        Vote vote = Vote.builder()
                .userId(1)
                .rsEventId(1)
                .voteNum(11)
                .voteTime(now)
                .build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userDto));
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(eventDto));

        // When & Then
        assertThrows(RuntimeException.class, ()->rsService.vote(vote));
    }

    @Test
    void should_throw_exception_given_userId_is_not_exist() {
        // Given
        RsService rsService = beanFactory.getBean(RsService.class, userRepository, rsEventRepository, voteRepository);

        LocalDateTime now = LocalDateTime.now();
        Vote vote = Vote.builder()
                .userId(1)
                .rsEventId(1)
                .voteNum(3)
                .voteTime(now)
                .build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(eventDto));

        // When & Then
        assertThrows(RuntimeException.class, ()->rsService.vote(vote));
    }

    @Test
    void should_throw_exception_given_rsEventId_is_not_exist() {
        // Given
        RsService rsService = beanFactory.getBean(RsService.class, userRepository, rsEventRepository, voteRepository);

        LocalDateTime now = LocalDateTime.now();
        Vote vote = Vote.builder()
                .userId(1)
                .rsEventId(1)
                .voteNum(3)
                .voteTime(now)
                .build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userDto));
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, ()->rsService.vote(vote));
    }
}
