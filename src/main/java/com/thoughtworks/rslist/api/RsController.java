package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {

  public List<RsEvent> rsList = initRsEventList();

  public List<RsEvent> initRsEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    User user = new User("default", "male", 20, "default@default.com", "18888888888");
    rsEventList.add(new RsEvent("事件1","无", user));
    rsEventList.add(new RsEvent("事件2","无", user));
    rsEventList.add(new RsEvent("事件3","无", user));
    return rsEventList;
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getEventList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if(start != null && end != null) {
      return rsList.subList(start-1, end);
    }
    return rsList;
  }

  @GetMapping("/rs/event/{id}")
  public RsEvent getEvent(@PathVariable int id) {
    return rsList.get(id-1);
  }

  @PostMapping("/rs/event")
  public ResponseEntity addEvent(@RequestBody RsEvent event) {
      rsList.add(event);
      return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/event/{id}")
  public void modifyEvent(@RequestBody RsEvent event, @PathVariable int id) {
    rsList.set(id-1, event);
  }

  @DeleteMapping("/rs/event/{id}")
  public void deleteEvent(@PathVariable int id) {
    rsList.remove(rsList.get(id-1));
  }
}
