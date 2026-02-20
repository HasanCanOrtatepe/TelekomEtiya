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


    @Override
    public void sendMail(Ticket ticket) {

    }
}
