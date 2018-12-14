package com.galvanize.badgearamareceptiondesk.exception;


import antlr.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GuestNotFoundException extends RuntimeException{
    public GuestNotFoundException(Long phoneNumber) {
        String.format("The Phone number %s doesnot exits",phoneNumber);
    }
}
