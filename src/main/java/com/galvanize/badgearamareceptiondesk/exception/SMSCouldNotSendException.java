package com.galvanize.badgearamareceptiondesk.exception;

public class SMSCouldNotSendException extends RuntimeException {
    public SMSCouldNotSendException(){
        System.out.println("=========There was a Exception while sending SMS ============");
    }
}
