package com.galvanize.badgearamareceptiondesk.service;

import com.galvanize.badgearamareceptiondesk.entity.ExtendedPerson;
import com.galvanize.badgearamareceptiondesk.enums.VisitorStatus;
import com.galvanize.badgearamareceptiondesk.exception.DuplicateRegistrationTrialException;
import com.galvanize.badgearamareceptiondesk.exception.GuestNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;

@Service
public class VerifyListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyListener.class);

    @Autowired
    VisitorService visitService;

    @RabbitListener(queues = "${amqp.queue.name}")

    public void receiveMessageForApp(final ExtendedPerson extendedPerson) throws ParseException {
        LOGGER.info("Received message: {} from sender queue.", extendedPerson);
        try {
            if ((extendedPerson.getPhoneNumber() != null) && (extendedPerson.getStatus().equals(VisitorStatus.UNVERIFIED)))
                visitService.savePersonToVisitTableSentByRabbitMQ_name_verify(extendedPerson);
            else throw new GuestNotFoundException(extendedPerson.getPhoneNumber());
        }catch(DuplicateRegistrationTrialException | ListenerExecutionFailedException e){
           LOGGER.info(" An exception occurred :: {} ", e);
        }
    }
}
