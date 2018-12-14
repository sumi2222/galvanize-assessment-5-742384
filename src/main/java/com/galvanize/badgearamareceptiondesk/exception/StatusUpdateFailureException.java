package com.galvanize.badgearamareceptiondesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class StatusUpdateFailureException extends RuntimeException {
public StatusUpdateFailureException(String msg){
        System.out.println("Status is not changed.");
    }
}
