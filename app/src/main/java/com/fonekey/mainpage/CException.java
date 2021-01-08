package com.fonekey.mainpage;

public class CException extends Exception {

    private final String message;

    public CException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}