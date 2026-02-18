package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.request.emailNotificationRequest.EmailNotificationCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse.EmailNotificationListResponse;
import com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse.EmailNotificationResponse;

public interface EmailNotificationService {

    EmailNotificationResponse create(EmailNotificationCreateRequest request);

    EmailNotificationResponse getById(Long id);

    EmailNotificationListResponse getAll();

    EmailNotificationListResponse getByTicket(Long ticketId);

    EmailNotificationListResponse getByStatus(String status);

    EmailNotificationResponse markAsSent(Long id); // sentAt = now

    EmailNotificationResponse markAsFailed(Long id);
}
