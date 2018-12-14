package com.galvanize.badgearamareceptiondesk.entity;

import com.galvanize.badgearamareceptiondesk.enums.VisitorStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ExtendedPersonFrontEnd {

    private String phoneNumber;        // populated by user
    private String firstName;          // populated by user
    private String lastName;           // populated by user
    private String company;            // populated by user
    private String hostName;           // populated by user
    private String hostPhone;          // populated by user
    private String purposeOfVisit;     // populated by user
    private String checkedInBy;        // populated by system
    private String checkedOutBy;       // populated by system
    private String reasonForDeletion;  // populated by guard
    private String badgeNumber;        // populated by guard

    private Date registerDate;         // populated by system
    private Date checkedInDate;        // populated by system
    private Date checkedOutDate;       // populated by system

    private Boolean active;            // populated by system/guard
    private VisitorStatus status;        // populated by system
    private Long waitingTime;   // populated by user

    @Builder
    public ExtendedPersonFrontEnd(String phoneNumber, String firstName, String lastName, String company, String hostName, String hostPhone, String purposeOfVisit, String checkedInBy, String checkedOutBy, String reasonForDeletion, String badgeNumber, Date registerDate, Date checkedInDate, Date checkedOutDate, Boolean active, VisitorStatus status, Long waitingTime ) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.hostName = hostName;
        this.hostPhone = hostPhone;
        this.purposeOfVisit = purposeOfVisit;
        this.checkedInBy = checkedInBy;
        this.checkedOutBy = checkedOutBy;
        this.reasonForDeletion = reasonForDeletion;
        this.badgeNumber = badgeNumber;
        this.registerDate = registerDate;
        this.checkedInDate = checkedInDate;
        this.checkedOutDate = checkedOutDate;
        this.active = active;
        this.status = status;
        this.waitingTime = waitingTime;
    }
}
