package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.entity.Ticket;
import com.etiya.etiyatelekom.service.abst.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class EmailNotificationServiceImpl implements EmailNotificationService {


    @Override
    public void sendMail(Ticket ticket) {

    }
}
