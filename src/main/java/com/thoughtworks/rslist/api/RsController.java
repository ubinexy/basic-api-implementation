package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RsEventRepository rsEventRepository;

  @PostMapping("/rs/event")
  ResponseEntity addEvent(@RequestBody @Valid RsEvent event) {
    if(!userRepository.existsById(event.getUserId())) return ResponseEntity.badRequest().build();

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
}
