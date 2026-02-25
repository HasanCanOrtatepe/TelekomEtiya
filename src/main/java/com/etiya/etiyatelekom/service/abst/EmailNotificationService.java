package com.etiya.etiyatelekom.service.abst;

import com.etiya.etiyatelekom.entity.Ticket;

public interface EmailNotificationService {

    void sendMail(Ticket ticket);

}
