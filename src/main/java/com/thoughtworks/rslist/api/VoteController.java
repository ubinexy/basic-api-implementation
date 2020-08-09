package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
@RestController
public class VoteController {

    private final VoteRepository voteRepository;
    private final RsService rsService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    public VoteController(VoteRepository voteRepository, RsService rsService) {
        this.voteRepository = voteRepository;
        this.rsService = rsService;
    }

    @PostMapping("/rs/vote/{rsEventId}")
    ResponseEntity voteRsEvent(@PathVariable int rsEventId, @RequestBody @Valid Vote vote) {
        vote.setRsEventId(rsEventId);
        try {
            rsService.vote(vote);
            return ResponseEntity.created(null).build();
        } catch(RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
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
