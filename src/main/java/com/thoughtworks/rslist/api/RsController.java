package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RsController {

    private final UserRepository userRepository;
    private final RsEventRepository rsEventRepository;

    @Autowired
    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }

    @PostMapping("/rs/event")
    ResponseEntity addEvent(@RequestBody @Valid RsEvent event) {
        if(!userRepository.existsById(event.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        UserDto userDto = userRepository.findById(event.getUserId()).get();

        RsEventDto eventDto = RsEventDto.builder()
                .eventName(event.getEventName())
                .keyword(event.getKeyword())
                .userDto(userDto)
                .build();

        userDto.getRsEventDtoList().add(eventDto);

        rsEventRepository.save(eventDto);
        return ResponseEntity.created(null).build();
    }

    @PatchMapping("/rs/event/{id}")
    ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid RsEvent event) {
        if(!rsEventRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        RsEventDto eventDto = rsEventRepository.findById(id).get();
        if(eventDto.getUserDto().getId() != event.getUserId()) {
            return ResponseEntity.badRequest().build();
        }
        if(event.getEventName() != null) {
            eventDto.setEventName(event.getEventName());
        }
        if(event.getKeyword() != null) {
            eventDto.setKeyword(event.getKeyword());
        }
        rsEventRepository.save(eventDto);
            return ResponseEntity.ok().build();
    }
}
