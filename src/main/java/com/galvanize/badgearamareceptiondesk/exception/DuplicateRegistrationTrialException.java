package com.galvanize.badgearamareceptiondesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class DuplicateRegistrationTrialException extends RuntimeException{
    public DuplicateRegistrationTrialException(Long phoneNumber) {
        String.format("Duplicate registration!! %s already registered and didnot checkout yet !!", phoneNumber);
    }
}
