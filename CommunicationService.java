package com.swymnation.dms.service;

import com.swymnation.dms.model.Notification;

public interface CommunicationService {
    void dispatch(Notification notification);
    String getChannelName();
}