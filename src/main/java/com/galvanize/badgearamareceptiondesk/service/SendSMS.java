package com.galvanize.badgearamareceptiondesk.service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class SendSMS {
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACea8b16b9d319a0ceafc835f598414b91";
    public static final String AUTH_TOKEN = "61128b69036e98b71e5cf0f58dade4d2";

    public void send(String guestName, String companyName, Long phoneNum) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        PhoneNumber phoneNumber = new PhoneNumber("+" +phoneNum.toString());
        String constructMessage = String.format("%s from %s is waiting in the lobby.",guestName,companyName );
        //Message message = Message.creator(new PhoneNumber("+12037709809"), new PhoneNumber("+12039418943")
        //, "This is message is from Sumita Twilio....").create();
        Message message = Message.creator(phoneNumber, new PhoneNumber("+12039418943")
                ,constructMessage).create();
        System.out.println(message.getSid());
    }
}