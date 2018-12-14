package com.galvanize.badgearamareceptiondesk.controller;

import com.galvanize.badgearamareceptiondesk.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamareceptiondesk.enums.VisitorStatus;
import com.galvanize.badgearamareceptiondesk.service.RestCallToRegisterService;
import com.galvanize.badgearamareceptiondesk.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/visit")

public class VisitController {

    @Autowired
    private VisitorService visitService;
    @Autowired
    RestCallToRegisterService restCallToRegisterService;

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitController.class);

    // Guard changes status from UNVARIFIED to WAITING after Id varification ,SMS sent.
    @PutMapping("/visitor/verify")
    public ResponseEntity<String> verifyVisitorByGuard(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd){
        String response  = visitService.verifyVisitor(extendedPersonFrontEnd, VisitorStatus.UNVERIFIED);
        if(response != null)
            return ResponseEntity.ok().body(response);
        else return ResponseEntity.notFound().build();
    }

    //  HOST change status from WAITING to IN
    @PutMapping("/visitor/checkin")
    public ResponseEntity<String> pickupVisitorByHost(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = visitService.pickupVisitorByHost(extendedPersonFrontEnd, VisitorStatus.WAITING);
        if(response != null)
            return ResponseEntity.ok().body(response);
        else return ResponseEntity.notFound().build();
    }

     @PutMapping("/visitor/checkout")
    public ResponseEntity<String> getVisitorCheckoutByPhoneNumber(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = visitService.processCheckout(extendedPersonFrontEnd, VisitorStatus.IN);
         if(response != null)
             return ResponseEntity.ok().body(response);
         else return ResponseEntity.notFound().build();
    }

    @PutMapping("/visitor/update")
    public ResponseEntity<String> updateVisitTable(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = visitService.updateVisitTable(extendedPersonFrontEnd);
        if(response != null)
            return ResponseEntity.ok().body(response);
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/visitor/notify")
    public ResponseEntity<String> notifyHost(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = visitService.sendSMSToHost(extendedPersonFrontEnd);
        if(response != null)
            return ResponseEntity.ok().body(response);
        else return ResponseEntity.notFound().build();
    }

    //=========================== All reports===============================


    @GetMapping("/visitors/lookup")
    public ResponseEntity<List<ExtendedPersonFrontEnd>> getAllVisitors() {
        List<ExtendedPersonFrontEnd> extPerFrontEndList = visitService.findAllVisitors();
        LOGGER.info("ExtendedPersonFrontEnd List is :: "+extPerFrontEndList.toString());
        if(extPerFrontEndList != null)
            return ResponseEntity.ok().body(extPerFrontEndList);
        else return ResponseEntity.notFound().build();
    }

    // GUARD find all Waiting visitors
    @GetMapping("/visitors/waiting")
    public ResponseEntity<List<ExtendedPersonFrontEnd>> getAllWaitingVisitors() {
        List<ExtendedPersonFrontEnd> extPerFrontEndList = visitService.findVisitorsOnStatus(VisitorStatus.WAITING);
        LOGGER.info("ExtendedPersonFrontEnd List is :: "+extPerFrontEndList.toString());
        if(extPerFrontEndList != null)
            return ResponseEntity.ok().body(extPerFrontEndList);
        else return ResponseEntity.notFound().build();
    }



    @GetMapping("/visitor/lookup/{phoneNumber}")
    public ResponseEntity<ExtendedPersonFrontEnd> getVisitorByPhone(@PathVariable String phoneNumber) {
        ExtendedPersonFrontEnd extendedPersonFrontEnd = visitService.findVisitorByPhone(phoneNumber);
        LOGGER.info("ExtendedPersonFrontEnd is :: "+extendedPersonFrontEnd.toString());
        if(extendedPersonFrontEnd != null)
            return ResponseEntity.ok().body(extendedPersonFrontEnd);
        else return ResponseEntity.notFound().build();

    }

    @GetMapping("/visitors/checkedin")
    public ResponseEntity<List<ExtendedPersonFrontEnd>> getAllInVisitors() {
        List<ExtendedPersonFrontEnd> extPerFrontEndList = visitService.findVisitorsOnStatus(VisitorStatus.IN);
        LOGGER.info("ExtendedPersonFrontEnd List is :: "+extPerFrontEndList.toString());
        if(extPerFrontEndList != null)
            return ResponseEntity.ok().body(extPerFrontEndList);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/visitors/checkedout")
    public ResponseEntity<List<ExtendedPersonFrontEnd>> getAllOUTVisitors() {
        List<ExtendedPersonFrontEnd> extPerFrontEndList = visitService.findVisitorsOnStatus(VisitorStatus.OUT);
        LOGGER.info("ExtendedPersonFrontEnd List is :: "+extPerFrontEndList.toString());
        if(extPerFrontEndList != null)
            return ResponseEntity.ok().body(extPerFrontEndList);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/visitor/waiting/{phoneNumber}")
    public ResponseEntity<ExtendedPersonFrontEnd> getVisitorWaitingnByPhone(@PathVariable String phoneNumber) {
        ExtendedPersonFrontEnd extendedPersonFrontEnd =  visitService.findVisitorByPhoneAndStatus(phoneNumber, VisitorStatus.WAITING);
        if(extendedPersonFrontEnd != null)
            return ResponseEntity.ok().body(extendedPersonFrontEnd);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/visitor/checkedin/{phoneNumber}")
    public ResponseEntity<ExtendedPersonFrontEnd> getVisitorCheckedInByPhone(@PathVariable String phoneNumber) {
        ExtendedPersonFrontEnd extendedPersonFrontEnd =  visitService.findVisitorByPhoneAndStatus(phoneNumber, VisitorStatus.IN);
        if(extendedPersonFrontEnd != null)
            return ResponseEntity.ok().body(extendedPersonFrontEnd);
        else return ResponseEntity.notFound().build();
    }

    //********************** REST Calls to register *******************************************
    @GetMapping("/lookup/{phoneNumber}")
    public ResponseEntity<ExtendedPersonFrontEnd> getPersonFromRegisterByPhone(@PathVariable String phoneNumber) {
        ExtendedPersonFrontEnd extendedPersonFrontEnd =  restCallToRegisterService.getPersonFromRegisterByPhoneNumber(phoneNumber);
        if(extendedPersonFrontEnd != null)
            return ResponseEntity.ok().body(extendedPersonFrontEnd);
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/person")
    public ResponseEntity<String> updatePersonToRegister(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = restCallToRegisterService.updateRegister(extendedPersonFrontEnd);
        if(response != null) {
            LOGGER.info(response);
            return ResponseEntity.ok().body(response);
        }
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/delete/person")
    public ResponseEntity<String> deletePersonToRegister(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = visitService.deletePersonFromRegistryByPhoneNumber(extendedPersonFrontEnd);
        if(response != null) {
            LOGGER.info(response);
            return ResponseEntity.ok().body(response);
        }
        else return ResponseEntity.notFound().build();
    }
}