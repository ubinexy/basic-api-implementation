package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.omg.CORBA.RepositoryIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {
    UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) {
        UserDto userDto = UserDto.builder()
                .username(user.getUsername())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        userRepository.save(userDto);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        if(userRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        UserDto userDto = userRepository.findById(id).get();
        return ResponseEntity.ok()
                .body(new User(userDto.getUsername(), userDto.getGender(), userDto.getAge(), userDto.getEmail(), userDto.getPhone()));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) {
        if(userRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
