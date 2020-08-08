package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private VoteRepository voteRepository;

    @PostMapping("/rs/vote/{rsEventId}")
    ResponseEntity voteRsEvent(@PathVariable int rsEventId, @RequestBody Vote vote) {
        int userId = vote.getUserId();
        if(!userRepository.existsById(userId)) return ResponseEntity.badRequest().build();
        if(!rsEventRepository.existsById(rsEventId)) return ResponseEntity.badRequest().build();

        UserDto user = userRepository.findById(userId).get();
        RsEventDto event = rsEventRepository.findById(rsEventId).get();

        if(user.getVoteNum() < vote.getVoteNum()) return ResponseEntity.badRequest().build();
        user.vote(vote.getVoteNum());
        event.vote(vote.getVoteNum());
        userRepository.save(user);
        rsEventRepository.save(event);
        voteRepository.save(VoteDto.builder()
                .localDateTime(vote.getVoteTime())
                .rsEvent(event)
                .user(user)
                .num(vote.getVoteNum())
                .build()
        );
        return ResponseEntity.created(null).build();
    }
}
