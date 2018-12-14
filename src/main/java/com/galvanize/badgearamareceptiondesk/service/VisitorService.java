package com.galvanize.badgearamareceptiondesk.service;

import com.galvanize.badgearamareceptiondesk.entity.ExtendedPerson;
import com.galvanize.badgearamareceptiondesk.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamareceptiondesk.entity.Visit;
import com.galvanize.badgearamareceptiondesk.enums.VisitorStatus;
import com.galvanize.badgearamareceptiondesk.exception.*;
import com.galvanize.badgearamareceptiondesk.repository.VisitRepository;
import com.galvanize.badgearamareceptiondesk.utility.EntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VisitorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorService.class);
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private VisitRepository visitRepository;
    private EntityConverter entityConverter;
    private RestCallToRegisterService restCallToRegisterService;
    private SendSMS sendSMS;

    @Autowired
    VisitorService(VisitRepository visitRepository,EntityConverter entityConverter, RestCallToRegisterService restCallToRegisterService, SendSMS sendSMS){
        this.visitRepository = visitRepository;
        this.entityConverter = entityConverter;
        this.restCallToRegisterService = restCallToRegisterService;
        this.sendSMS = sendSMS;
    }

    void savePersonToVisitTableSentByRabbitMQ_name_verify(ExtendedPerson extendedPerson) {

        Optional<Visit> visit = visitRepository.findFirstByPhoneNumberAndStatusOrderByRegisterDateDesc(
                extendedPerson.getPhoneNumber(), VisitorStatus.UNVERIFIED);
        if ((visit.isPresent()) && (visit.get().getCheckedOutDate() == null)) {
            LOGGER.info("Phone no. {} already registered in the system ", extendedPerson.getPhoneNumber());
            throw new DuplicateRegistrationTrialException(extendedPerson.getPhoneNumber());
        }else if((visit.isPresent() && (visit.get().getCheckedOutDate() != null)) || (!visit.isPresent() )) {
            visitRepository.save(Visit.builder()
            .phoneNumber(extendedPerson.getPhoneNumber())
            .hostName(extendedPerson.getHostName())
            .hostPhoneNumber(extendedPerson.getHostPhoneNumber())
            .purposeOfVisit(extendedPerson.getPurposeOfVisit())
            .active(true)
            .registerDate(new Date())
            .status(VisitorStatus.UNVERIFIED)
            .build());
        }
    }

    @Transactional
    public List<ExtendedPersonFrontEnd> findVisitorsOnStatus(VisitorStatus status) {
        List<ExtendedPersonFrontEnd> result = new ArrayList<>();
        List<Visit> visitorList = new ArrayList<>();
        boolean waitingVisitor = status.equals(VisitorStatus.WAITING) ? true : false;
        if(waitingVisitor){
            visitorList = visitRepository.findAllByStatusOrStatus(VisitorStatus.WAITING, VisitorStatus.UNVERIFIED);
            LOGGER.info(" ************ visitorList +++++++++++++  :: " + visitorList.toString());
            visitorList.forEach(visit-> {
                long waitingTime = visit.getRegisterDate().getTime();
                LOGGER.info(" ************ Registered time in nano-seconds :: " + waitingTime);
                long totalWaitTime = (new Date().getTime()) - waitingTime;
                LOGGER.info(" ************ Total waiting time in nano-seconds  :: " + totalWaitTime);
                ExtendedPersonFrontEnd frontEndPerson = entityConverter.transformVisitToExtendedPersonFrontEnd(visit);
                frontEndPerson.setWaitingTime(totalWaitTime);
                result.add(frontEndPerson);
            });
        }else {
            visitorList = visitRepository.findAllByStatus(status);
            visitorList.forEach(visit-> {
                result.add(entityConverter.transformVisitToExtendedPersonFrontEnd(visit));
            });
        }
        LOGGER.info(" ************ visits :" + visitorList.toString());
        return result;
    }

    @Transactional
    public ExtendedPersonFrontEnd findVisitorByPhone(String phoneNumber) {
        Long phone = stringToLongConversion(phoneNumber);
        Optional<Visit> visitor = visitRepository.findFirstByPhoneNumberOrderByRegisterDateDesc(phone);
        LOGGER.info(" ************ visits :" + visitor.toString());
        return entityConverter.transformVisitToExtendedPersonFrontEnd(visitor.get());
    }

    @Transactional
    public ExtendedPersonFrontEnd findVisitorByPhoneAndStatus(String phoneNumber, VisitorStatus status) {
        Long phone = stringToLongConversion(phoneNumber);
        Optional<Visit> visitor = visitRepository.findFirstByPhoneNumberAndStatusOrderByRegisterDateDesc(phone, status);
        LOGGER.info(" ************ visits :" + visitor.toString());
        return entityConverter.transformVisitToExtendedPersonFrontEnd(visitor.get());
    }

    @Transactional
    public List<ExtendedPersonFrontEnd> findAllVisitors( ) {
        List<ExtendedPersonFrontEnd> result = new ArrayList<>();
        Iterable<Visit> visitorList = visitRepository.findAll();
        LOGGER.info(" ************All visits :" + visitorList.toString());
        visitorList.forEach(visit->{
            result.add(entityConverter.transformVisitToExtendedPersonFrontEnd(visit));
        });
        return result;
    }

    @Transactional
    public String processCheckout(ExtendedPersonFrontEnd extendedPersonFrontEnd, VisitorStatus status){
        final String statusUpdate = "You are checked out !!!!!";
        return updateRecord(extendedPersonFrontEnd, status, statusUpdate);
    }

    @Transactional
    public String verifyVisitor(ExtendedPersonFrontEnd extendedPersonFrontEnd, VisitorStatus status){
        final String statusUpdate = "Status changed to Waiting !!!!!";
        return updateRecord(extendedPersonFrontEnd, status, statusUpdate);
    }

    @Transactional
    public String pickupVisitorByHost(ExtendedPersonFrontEnd extendedPersonFrontEnd, VisitorStatus status){
        final String statusUpdate = "Status changed to checked-IN !!!!!";
        return updateRecord(extendedPersonFrontEnd, status, statusUpdate);
    }

    @Transactional
    public String updateVisitTable(ExtendedPersonFrontEnd extendedPersonFrontEnd){
        String phoneNumber = extendedPersonFrontEnd.getPhoneNumber();
        Optional<Visit> visit = visitRepository.findFirstByPhoneNumberOrderByRegisterDateDesc(stringToLongConversion(phoneNumber));
        if(visit.isPresent()) {
            Date dateInTime = visit.get().getCheckedInDate();
            Date dateOutTime = visit.get().getCheckedOutDate();
            VisitorStatus visitorStatus;
            if( extendedPersonFrontEnd != null){visitorStatus = extendedPersonFrontEnd.getStatus();} else{visitorStatus = visit.get().getStatus();}
            saveRecordInVisitTable(extendedPersonFrontEnd, visit.get(), dateInTime, dateOutTime, true, visitorStatus);
         return String.format("Record updated successfully for phone number %s ...",phoneNumber);
        }else{
            return null;
        }
    }

    private String updateRecord(ExtendedPersonFrontEnd extendedPersonFrontEnd, VisitorStatus status, String statusUpdate) {
        Date dateInTime = null;
        Date dateOutTime = null;
        Boolean activeStatus = false;
        VisitorStatus setUpdatedStatus = null;

        try {
            Long phone = stringToLongConversion(extendedPersonFrontEnd.getPhoneNumber());
            Optional<Visit> visit = visitRepository.findFirstByPhoneNumberAndStatusOrderByRegisterDateDesc(phone, status);
            Visit updatePerson;

            if (visit.isPresent()) {
                updatePerson = visit.get();
                LOGGER.info("Visitor before status change" + updatePerson.toString());
                //if(! statusUpdate.equalsIgnoreCase("NO")) {
                    if (status.equals(VisitorStatus.UNVERIFIED) && updatePerson.getStatus().equals(VisitorStatus.UNVERIFIED)) {
                        dateInTime  = new Date();
                        setUpdatedStatus = VisitorStatus.WAITING;
                        activeStatus = true;
                    }
                    if (status.equals(VisitorStatus.WAITING) && updatePerson.getStatus().equals(VisitorStatus.WAITING)) {
                        setUpdatedStatus = VisitorStatus.IN;
                        dateInTime = new Date();
                        activeStatus = true;
                    }
                    if (status.equals(VisitorStatus.IN) && updatePerson.getStatus().equals(VisitorStatus.IN)) {
                        setUpdatedStatus = VisitorStatus.OUT;
                        dateInTime = updatePerson.getCheckedInDate();
                        dateOutTime = new Date();
                        activeStatus = false;
                    }
                saveRecordInVisitTable(extendedPersonFrontEnd, updatePerson, dateInTime, dateOutTime, activeStatus, setUpdatedStatus);

                // **********  Send SMS if Guard verified visitor  *********************

                if(setUpdatedStatus != null && (setUpdatedStatus.equals(VisitorStatus.WAITING))){
                              sendSMSToHost(extendedPersonFrontEnd);
                }
                LOGGER.info("Visitor after status change" + visitRepository.findById(updatePerson.getId()));
                return statusUpdate;
            } else {
                LOGGER.info("The Visitor list is empty ======== ");
                return null;
            }
        } catch(StatusUpdateFailureException ex){
            LOGGER.info("StatusUpdateFailureException :: "+ex);
            return null;                                                   // As per Ray's front end wants to consume.
        }
    }

    @Transactional
    public void deleteVisitHistoryByPhoneNumber(String phoneNumber){
        Long phone = stringToLongConversion(phoneNumber);
        visitRepository.deleteByPhoneNumber(phone);
    }

    @Transactional
    public String deletePersonFromRegistryByPhoneNumber(ExtendedPersonFrontEnd extendedPersonFrontEnd){
        deleteVisitHistoryByPhoneNumber(extendedPersonFrontEnd.getPhoneNumber());
        return restCallToRegisterService.deleteRegister(extendedPersonFrontEnd);
    }

    public String sendSMSToHost( ExtendedPersonFrontEnd extendedPersonFrontEnd){
        try {
            String visitorPhone = extendedPersonFrontEnd.getPhoneNumber();
            ExtendedPersonFrontEnd visitor = restCallToRegisterService.getPersonFromRegisterByPhoneNumber(visitorPhone);
            if(visitor != null) {
                final String gName = visitor.getFirstName() + " " + visitor.getLastName();
                final String cName = visitor.getCompany().toUpperCase();
                LOGGER.info("SMS SENDER  name is {} and company is {} ",gName,cName);
                Optional<Visit> visit = visitRepository.findFirstByPhoneNumberOrderByRegisterDateDesc(stringToLongConversion(visitorPhone));
                if(visit.isPresent()){
                    //final Long smsToPhone  = visit.get().getHostPhoneNumber();
                }
                final Long smsToPhone = 12037709809L;
                sendSMS.send(gName, cName, smsToPhone);
            }else {
                LOGGER.info(String.format("Visitor phone number %s could not find in registration database. ",visitorPhone));
                throw new GuestNotFoundException(stringToLongConversion(visitorPhone));
            }
        }catch(Exception e){
            LOGGER.info("SMS could not send :: "+e);
            throw new SMSCouldNotSendException();
        }
        return "SMS was sent successfully ...";
    }

    private Long stringToLongConversion(String phoneNumber) {
        try {
            return Long.parseLong(phoneNumber.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    private void saveRecordInVisitTable(ExtendedPersonFrontEnd extendedPersonFrontEnd, Visit updatePerson, Date dateInTime, Date dateOutTime, Boolean activeStatus, VisitorStatus setUpdatedStatus){
        Long hostPhone;
        String hostName;
        String checkedInBy;
        String purposeOfVisit;
        String badgeNum;

        if (extendedPersonFrontEnd.getHostPhone()== null){hostPhone = updatePerson.getHostPhoneNumber();} else {hostPhone = stringToLongConversion(extendedPersonFrontEnd.getHostPhone());}
        if (extendedPersonFrontEnd.getHostName()== null){hostName = updatePerson.getHostName();} else {hostName = extendedPersonFrontEnd.getHostName();}
        if (extendedPersonFrontEnd.getCheckedInBy()== null){checkedInBy = updatePerson.getCheckedInBy();} else {checkedInBy = extendedPersonFrontEnd.getCheckedInBy();}
        if (extendedPersonFrontEnd.getPurposeOfVisit()== null){purposeOfVisit = updatePerson.getPurposeOfVisit();} else {purposeOfVisit = extendedPersonFrontEnd.getPurposeOfVisit();}
        if (extendedPersonFrontEnd.getBadgeNumber()== null){badgeNum = updatePerson.getBadgeNumber();} else {badgeNum = extendedPersonFrontEnd.getBadgeNumber();}

        visitRepository.save(Visit.builder()
            .id(updatePerson.getId())
            .phoneNumber(updatePerson.getPhoneNumber())
            .hostName(hostName)
            .hostPhoneNumber(hostPhone)
            .purposeOfVisit(purposeOfVisit)
            .checkedInDate(dateInTime)
            .checkedOutDate(dateOutTime)
            .checkedInBy(checkedInBy)
            .badgeNumber(badgeNum)
            .active(activeStatus)
            .registerDate(updatePerson.getRegisterDate())
            .status(setUpdatedStatus)
            .build());
    }
}


