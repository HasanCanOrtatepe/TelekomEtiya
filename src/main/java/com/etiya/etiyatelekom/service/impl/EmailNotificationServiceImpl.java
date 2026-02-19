package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.api.dto.request.emailNotificationRequest.EmailNotificationCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse.EmailNotificationListResponse;
import com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse.EmailNotificationResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.EmailNotification;
import com.etiya.etiyatelekom.entity.Ticket;
import com.etiya.etiyatelekom.repository.EmailNotificationRepository;
import com.etiya.etiyatelekom.repository.TicketRepository;
import com.etiya.etiyatelekom.service.abst.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final EmailNotificationRepository emailNotificationRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapperService modelMapperService;


    @Override
    public EmailNotificationResponse create(EmailNotificationCreateRequest request) {

        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() ->
                        new ResourceAlreadyExistsException("Ticket","id",request.getTicketId().toString()));

        EmailNotification notification = EmailNotification.builder()
                .ticket(ticket)
                .recipientEmail(request.getRecipientEmail())
                .status("PENDING")
                .build();

        EmailNotification saved = emailNotificationRepository.save(notification);

        EmailNotificationResponse emailNotificationResponse= modelMapperService.forResponse()
                .map(saved, EmailNotificationResponse.class);

        return emailNotificationResponse;
    }





    @Override
    public EmailNotificationResponse getById(Long id) {

        EmailNotification notification =
                emailNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("EmailNotification","id",id));

        EmailNotificationResponse emailNotificationResponse=modelMapperService.forResponse()
                .map(notification, EmailNotificationResponse.class);
        return emailNotificationResponse;
    }


    @Override
    public EmailNotificationListResponse getAll() {

        if (emailNotificationRepository.count()==0){
            throw  new ResourceNotFoundException();
        }
        List<EmailNotification> emailNotifications=emailNotificationRepository.findAll();
        List<EmailNotificationResponse> emailNotificationResponses=emailNotifications.stream()
                .map(emailNotification -> modelMapperService.forResponse().map(emailNotification,EmailNotificationResponse.class))
                .toList();

        EmailNotificationListResponse emailNotificationListResponse=EmailNotificationListResponse.builder()
                .items(emailNotificationResponses)
                .count(emailNotificationResponses.size())
                .build();

        return emailNotificationListResponse;

    }

    @Override
    public EmailNotificationListResponse getByTicket(Long ticketId) {

        if (!emailNotificationRepository.existsByTicket_Id(ticketId)){
            throw  new ResourceNotFoundException("EmailNotification","Ticket",ticketId);
        }
        List<EmailNotification> emailNotifications=emailNotificationRepository.findByTicket_Id(ticketId);
        List<EmailNotificationResponse> emailNotificationResponses=emailNotifications.stream()
                .map(emailNotification -> modelMapperService.forResponse().map(emailNotification,EmailNotificationResponse.class))
                .toList();

        EmailNotificationListResponse emailNotificationListResponse=EmailNotificationListResponse.builder()
                .items(emailNotificationResponses)
                .count(emailNotificationResponses.size())
                .build();

        return emailNotificationListResponse;
    }

    @Override
    public EmailNotificationListResponse getByStatus(String status) {

        if (!emailNotificationRepository.existsByStatus(status)){
            throw  new ResourceNotFoundException("EmailNotification","Status",status);
        }
        List<EmailNotification> emailNotifications=emailNotificationRepository.findByStatus(status);
        List<EmailNotificationResponse> emailNotificationResponses=emailNotifications.stream()
                .map(emailNotification -> modelMapperService.forResponse().map(emailNotification,EmailNotificationResponse.class))
                .toList();

        EmailNotificationListResponse emailNotificationListResponse=EmailNotificationListResponse.builder()
                .items(emailNotificationResponses)
                .count(emailNotificationResponses.size())
                .build();

        return emailNotificationListResponse;
    }


    @Override
    public EmailNotificationResponse markAsSent(Long id) {

        EmailNotification notification = emailNotificationRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("EmailNotification","id",id));

        notification.setStatus("SENT");

        EmailNotification emailNotification = emailNotificationRepository.save(notification);

        EmailNotificationResponse emailNotificationResponse=modelMapperService.forResponse()
                .map(emailNotification, EmailNotificationResponse.class);

        return emailNotificationResponse;
    }


    @Override
    public EmailNotificationResponse markAsFailed(Long id) {

        EmailNotification notification =
                emailNotificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("EmailNotification","id",id));

        notification.setStatus("FAILED");

        EmailNotification emailNotification = emailNotificationRepository.save(notification);

        EmailNotificationResponse emailNotificationResponse=modelMapperService.forResponse()
                .map(emailNotification, EmailNotificationResponse.class);

        return emailNotificationResponse;
    }

}
