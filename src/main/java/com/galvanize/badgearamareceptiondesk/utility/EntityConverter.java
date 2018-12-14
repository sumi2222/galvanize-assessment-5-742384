package com.galvanize.badgearamareceptiondesk.utility;

import com.galvanize.badgearamareceptiondesk.entity.ExtendedPerson;
import com.galvanize.badgearamareceptiondesk.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamareceptiondesk.entity.Person;
import com.galvanize.badgearamareceptiondesk.entity.Visit;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EntityConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityConverter.class);

    public ExtendedPerson transformFrontEndPerson(ExtendedPersonFrontEnd personFE) {
        return ExtendedPerson.builder()
                .phoneNumber(stringToLongConversion(personFE.getPhoneNumber()))
                .firstName(personFE.getFirstName())
                .lastName(personFE.getLastName())
                .company(personFE.getCompany())
                .hostName(personFE.getHostName())
                .hostPhoneNumber(stringToLongConversion(personFE.getHostPhone()))
                .purposeOfVisit(personFE.getPurposeOfVisit())
                .reasonForDeletion(personFE.getReasonForDeletion())
                .badgeNumber(personFE.getBadgeNumber())
                .active(personFE.getActive())
                .status(personFE.getStatus())
                .build();
    }

    public ExtendedPersonFrontEnd transformPersonToFrontEndPerson(Person person) {
        return ExtendedPersonFrontEnd.builder()
                .phoneNumber(person.getPhoneNumber().toString())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .company(person.getCompany()).build();
    }

    public ExtendedPersonFrontEnd transformVisitToExtendedPersonFrontEnd(Visit visit) {
        String phone =convertLongToString(visit.getPhoneNumber());
        return ExtendedPersonFrontEnd.builder()
                .phoneNumber(phone)
                .registerDate(visit.getRegisterDate())
                .checkedInDate(visit.getCheckedInDate())
                .checkedOutDate(visit.getCheckedOutDate())
                .badgeNumber(visit.getBadgeNumber())
                .status(visit.getStatus())
                .hostName(visit.getHostName())
                .hostPhone(convertLongToString(visit.getHostPhoneNumber()))
                .purposeOfVisit(visit.getPurposeOfVisit())
                .build();
    }
    
    public Long stringToLongConversion(String phoneNumber) {
        LOGGER.info("String phone number before {}", phoneNumber);
        try {
            return Long.parseLong(phoneNumber.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    public String convertLongToString(Long phoneNumber) {
       if (phoneNumber == null || phoneNumber.equals(0L))
               return null;
       LOGGER.info("phone number after replace {}", phoneNumber.toString());
       return phoneNumber.toString();
   }
}
