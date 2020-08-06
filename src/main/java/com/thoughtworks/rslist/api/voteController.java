package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.jvm.hotspot.debugger.cdbg.VoidType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class voteController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam int userId, @RequestParam int rsEventId) {

        return ResponseEntity.ok(
            voteRepository.findAllByUserIdAndEventId(userId, rsEventId).stream().map(
                    item -> Vote.builder().voteNum(item.getNum).build()
            ).collect(Collectors.toList()));
    }
}
