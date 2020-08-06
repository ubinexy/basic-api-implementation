package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.eception.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public static List<User> userList;

    public static List<User> initUserList() {
        List<User> users = new ArrayList<>();
        User user = new User("default", "male", 20, "default@email.com", "18888888888");
        users.add(user);
        return users;
    }

    public static boolean isContain(User user) {
        return userList.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()));
    }

    @GetMapping("/users")
    public List<User> getUserList() {
        return userList;
    }

    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) {
        userList.add(user);
        return ResponseEntity.created(null).build();
    }

}