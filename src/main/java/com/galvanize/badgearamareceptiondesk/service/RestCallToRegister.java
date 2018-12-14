package com.galvanize.badgearamareceptiondesk.service;

import com.galvanize.badgearamareceptiondesk.entity.ExtendedPersonFrontEnd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
 class RestCallToRegister {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestCallToRegister.class);

    @Value("${registration.endpoint.person.lookup}")
    private String registrationEndPTGetPerson;
    @Value("${registration.endpoint.person.update}")
    private String registrationEndPTUpdatePerson;
    @Value("${registration.endpoint.person.delete}")
    private String registrationEndPTDeletePerson;

    ExtendedPersonFrontEnd getPersonFromPersonTable(String phoneNum) {
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.info(" From GET REST CALL ===== ");
        try {

            //HttpHeaders headers = new HttpHeaders();
            //headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            //String url = "http://localhost:8181/visitor/lookup/{phoneNumber}";
            String url = registrationEndPTGetPerson;
            ExtendedPersonFrontEnd frontend = restTemplate.getForObject(url, ExtendedPersonFrontEnd.class, phoneNum);
            LOGGER.info(" Register-Rest return ======================:: {} ", frontend.toString());
            //return restTemplate.getForObject(url, ExtendedPersonFrontEnd.class, phoneNum);
            return frontend;
        } catch (Exception e) {
            LOGGER.info(" Register-Rest call Exception :: {} ", e);
            return null;
        }
    }

    String updatePersonTable(ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.info(" From UPDATE REST CALL ===== ");
        try {
            //String url = "http://localhost:8181/visitor/update";
            String url =registrationEndPTUpdatePerson;
            restTemplate.put(url, extendedPersonFrontEnd);
            return "Person updated ... ";
        } catch (Exception e) {
            LOGGER.info(String.format(" Register-Rest call Exception :: %s ", e));
            return null;
        }
    }

    String deletePersonTable(ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.info(" From DELETE REST CALL ===== ");
        LOGGER.info(" ExtendedPersonFrontEnd details :: "+extendedPersonFrontEnd.toString());
        try {
            //String url = "http://localhost:8181/visitor/delete";
            String url = registrationEndPTDeletePerson;
            restTemplate.put(url, extendedPersonFrontEnd);
            LOGGER.info("Person deleted ... ");
            //visitorService.deleteVisitHistoryByPhoneNumber(extendedPersonFrontEnd.getPhoneNumber());
            return "Person with phone no. " +extendedPersonFrontEnd.getPhoneNumber()+ " deleted ... ";
        } catch (Exception e) {
            LOGGER.info(" Register-Rest call Exception :: {} ", e);
            return null;
        }
    }
}
