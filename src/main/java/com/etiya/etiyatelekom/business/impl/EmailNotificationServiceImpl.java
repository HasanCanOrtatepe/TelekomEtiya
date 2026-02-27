package com.etiya.etiyatelekom.business.impl;


import com.etiya.etiyatelekom.entity.Ticket;
import com.etiya.etiyatelekom.business.abst.EmailNotificationService;
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
