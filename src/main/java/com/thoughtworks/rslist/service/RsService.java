package com.thoughtworks.rslist.service;

import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

public class RsService {
    private UserRepository userRepository;
    private RsEventRepository rsEventRepository;
    private VoteRepository voteRepository;

    public RsService(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(@Valid Vote vote) {
        Optional<UserDto> user = userRepository.findById(vote.getUserId());
        Optional<RsEventDto> event = rsEventRepository.findById(vote.getRsEventId());
        if(!user.isPresent()) {
            throw new RuntimeException();
        }
        if(!event.isPresent()) {
            throw new RuntimeException();
        }
        if(user.get().getVoteNum() < vote.getVoteNum())  {
            throw new RuntimeException();
        }

        user.get().vote(vote.getVoteNum());
        event.get().vote(vote.getVoteNum());

        userRepository.save(user.get());
        rsEventRepository.save(event.get());
        voteRepository.save(VoteDto.builder()
                .localDateTime(vote.getVoteTime())
                .rsEvent(event.get())
                .user(user.get())
                .num(vote.getVoteNum())
                .build()
        );
    }
}
