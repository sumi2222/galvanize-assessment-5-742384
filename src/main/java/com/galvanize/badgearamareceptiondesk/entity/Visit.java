package com.galvanize.badgearamareceptiondesk.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.galvanize.badgearamareceptiondesk.enums.VisitorStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private Long phoneNumber;

    private String hostName;
    private Long hostPhoneNumber;
    private String purposeOfVisit;
    private String checkedInBy;        // populated by system
    private String checkedOutBy;       // populated by system
    private String reasonForDeletion;  // populated by guard
    private String badgeNumber;        // populated by guard

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date  registerDate;         // populated by system
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkedInDate;        // populated by system
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkedOutDate;       // populated by system
    private Boolean active;            // populated by system/guard
    @Enumerated(EnumType.STRING)
    private VisitorStatus status;        // populated by system

    @Builder
    public Visit(Long id, Long phoneNumber, String hostName, Long hostPhoneNumber, String purposeOfVisit,
                 String checkedInBy, String checkedOutBy, String reasonForDeletion, String badgeNumber,
                 Date registerDate, Date checkedInDate, Date checkedOutDate,
                 Boolean active, VisitorStatus status) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.hostName = hostName;
        this.hostPhoneNumber = hostPhoneNumber;
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
    }
}
