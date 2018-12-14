package com.galvanize.badgearamareceptiondesk.service;

import com.galvanize.badgearamareceptiondesk.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamareceptiondesk.entity.Visit;
import com.galvanize.badgearamareceptiondesk.enums.VisitorStatus;
import com.galvanize.badgearamareceptiondesk.repository.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class TrackWaitingTime extends TimerTask{
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackWaitingTime.class);
    private VisitRepository visitRepository;


    @Autowired
    private TrackWaitingTime(VisitRepository visitRepository){
        this.visitRepository = visitRepository;
    }

    Toolkit toolkit;
    Timer timer;
    List<ExtendedPersonFrontEnd> waitingList;
    public TrackWaitingTime(List<ExtendedPersonFrontEnd> waitingList) {
      this.waitingList = waitingList;
    }

    public TrackWaitingTime() {
        toolkit = Toolkit.getDefaultToolkit();
        timer = new Timer();
        timer.schedule(new TrackWaitingTime(waitingList),
                0,        //initial delay
                60 * 1000);  //subsequent rate
    }

        int waitingTime = 10;
        public void run() {
            if (waitingTime > 0) {
               List<Visit>  waitingList =  visitRepository.findAllByStatus(VisitorStatus.WAITING);
                waitingList.forEach(visitor ->{
                    Long checkedInTime = visitor.getCheckedInDate().getTime();
                    Long totalWaitTime = (new Date().getTime()) - checkedInTime;
                    if(totalWaitTime >= (600* 1000)){
                        toolkit.beep();
                    }
                });

                System.out.println("Beep!");
                waitingTime--;
            } else {
                toolkit.beep();
                System.out.println("Time's up!");
                //timer.cancel();
                System.exit(0);
            }
        }
    }



