package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.request.emailNotificationRequest.EmailNotificationCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse.EmailNotificationListResponse;
import com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse.EmailNotificationResponse;
import com.etiya.etiyatelekom.entity.Ticket;

public interface EmailNotificationService {

    void sendMail(Ticket ticket);

}
