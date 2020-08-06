package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.eception.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {UserController.class})
public class UserControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity exceptionHandler(Exception e) {
        Error error = new Error("invalid user");
        return ResponseEntity.badRequest().body(error);
    }
}
