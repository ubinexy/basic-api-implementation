package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
@RestController
public class VoteController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private VoteRepository voteRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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

    @GetMapping("/rs/vote")
    ResponseEntity<List<Vote>> getVote(@RequestParam int page, @RequestParam String from, @RequestParam String to) {
        LocalDateTime start = LocalDateTime.parse(from, formatter);
        LocalDateTime end = LocalDateTime.parse(to, formatter);
        Pageable pageable = PageRequest.of(page, 5);

        List<VoteDto> all = voteRepository.findByLocalDateTimeBetween(start, end, pageable);

        List<Vote> v = all.stream().map(dto->Vote.builder()
                .rsEventId(dto.getUser().getId())
                .userId(dto.getUser().getId())
                .voteNum(dto.getNum())
                .voteTime(dto.getLocalDateTime())
                .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok().body(v);
    }
}
