package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {

  private List<RsEvent> rsList = initRsEventList();

  public List<RsEvent> initRsEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new RsEvent("事件1","无"));
    rsEventList.add(new RsEvent("事件2","无"));
    rsEventList.add(new RsEvent("事件3","无"));
    return rsEventList;
  }

  @GetMapping("/event/list")
  public List<RsEvent> getEventList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if(start != null && end != null) {
      return rsList.subList(start-1, end);
    }
    return rsList;
  }

  @GetMapping("/event/{id}")
  public RsEvent getEvent(@PathVariable int id) {
    return rsList.get(id-1);
  }

  @PostMapping("/add/event")
  public void addEvent(@RequestBody RsEvent event) {
    rsList.add(event);
  }

  @PostMapping("/modify/event/{id}")
  public void modifyEvent(@RequestBody RsEvent event, @PathVariable int id) {
    rsList.set(id-1, event);
  }

  @PostMapping("/delete/event/{id}")
  public void deleteEvent(@PathVariable int id) {
    rsList.remove(rsList.get(id-1));
  }
}
