package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.eception.Error;
import com.thoughtworks.rslist.eception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {RsController.class})
public class RsControllerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(RsController.class);

    @ExceptionHandler({MyException.class, MethodArgumentNotValidException.class})
    ResponseEntity rsControllerHandler(Exception e) {
        if (e instanceof MyException) {
            Error error = new Error(e.getMessage());
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } else {
            Error error = new Error("invalid param");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
