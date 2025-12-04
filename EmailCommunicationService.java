package com.swymnation.dms.service;

import com.swymnation.dms.model.Notification;
import org.springframework.stereotype.Service;

@Service
public class EmailCommunicationService implements CommunicationService {

    @Override
    public void dispatch(Notification notification) {
        // [Sec NFR] Simulates TLS-secured email transmission
        System.out.printf("[EMAIL_TLS_OUT] To: %s | Subject: %s | Body: %s%n",
            notification.targetUser().getEmail(),
            notification.subject(),
            notification.body()
        );
    }

    @Override
    public String getChannelName() {
        return "EMAIL";
    }
}