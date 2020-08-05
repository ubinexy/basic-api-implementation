package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public List<User> userList = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getUserList() {
        return userList;
    }

    @PostMapping("/user")
    public ResponseEntity User(@RequestBody User user) {
        userList.add(user);
        return ResponseEntity.created(null).build();
    }
}