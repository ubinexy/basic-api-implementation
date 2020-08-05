package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private List<User> userList = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getUserList() {
        return userList;
    }

    @PostMapping("/user")
    public void User(@RequestBody User user) {
        userList.add(user);
    }
}