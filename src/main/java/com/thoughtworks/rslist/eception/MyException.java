package com.thoughtworks.rslist.eception;

public class MyException extends RuntimeException {
    private String msg;
    public MyException(String message) {
        super(message);
        msg = message;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
