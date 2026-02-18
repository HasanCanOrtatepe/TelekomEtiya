package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.api.dto.request.emailNotificationRequest.EmailNotificationCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse.EmailNotificationListResponse;
import com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse.EmailNotificationResponse;
import com.etiya.etiyatelekom.service.abst.EmailNotificationService;

public class EmailNotificationServiceImpl implements EmailNotificationService {


    @Override
    public EmailNotificationResponse create(EmailNotificationCreateRequest request) {
        return null;
    }

    @Override
    public EmailNotificationResponse getById(Long id) {
        return null;
    }

    @Override
    public EmailNotificationListResponse getAll() {
        return null;
    }

    @Override
    public EmailNotificationListResponse getByTicket(Long ticketId) {
        return null;
    }

    @Override
    public EmailNotificationListResponse getByStatus(String status) {
        return null;
    }

    @Override
    public EmailNotificationResponse markAsSent(Long id) {
        return null;
    }

    @Override
    public EmailNotificationResponse markAsFailed(Long id) {
        return null;
    }
}
