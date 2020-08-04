package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");

  @GetMapping(path="/rs/list")
  public String getRsEventList() {
    return rsList.toString();
  }

  @GetMapping(path="/rs/event/{id}")
  public String getRsEvent(@PathVariable int id) {
    return rsList.get(id-1);
  }
}
