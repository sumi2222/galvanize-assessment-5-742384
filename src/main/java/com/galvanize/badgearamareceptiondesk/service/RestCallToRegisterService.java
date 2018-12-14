package com.galvanize.badgearamareceptiondesk.service;

import com.galvanize.badgearamareceptiondesk.entity.ExtendedPersonFrontEnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestCallToRegisterService {
    private RestCallToRegister restCallToRegister;
    @Autowired
    RestCallToRegisterService(RestCallToRegister restCallToRegister){
        this.restCallToRegister = restCallToRegister;
    }

    public ExtendedPersonFrontEnd getPersonFromRegisterByPhoneNumber(String phoneNumber){
    return restCallToRegister.getPersonFromPersonTable(phoneNumber);
    }
    public String updateRegister(ExtendedPersonFrontEnd extendedPersonFrontEnd){
        return restCallToRegister.updatePersonTable(extendedPersonFrontEnd);
    }
    public String deleteRegister(ExtendedPersonFrontEnd extendedPersonFrontEnd){
       return restCallToRegister.deletePersonTable(extendedPersonFrontEnd);
    }

}
