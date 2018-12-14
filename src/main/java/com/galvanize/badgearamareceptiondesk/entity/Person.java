package com.galvanize.badgearamareceptiondesk.entity;

import lombok.Builder;
import lombok.Data;

@Data

public class Person {

    private Long phoneNumber;
    private String firstName;
    private String lastName;
    private String company;

    @Builder
    public Person(Long phoneNumber, String firstName, String lastName, String company) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
    }
}
