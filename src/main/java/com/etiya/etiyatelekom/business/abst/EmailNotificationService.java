package com.etiya.etiyatelekom.business.abst;

import com.etiya.etiyatelekom.entity.Ticket;

public interface EmailNotificationService {

    void sendMail(Ticket ticket);

}
