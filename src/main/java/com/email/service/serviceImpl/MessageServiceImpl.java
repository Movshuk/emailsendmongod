package com.email.service.serviceImpl;

import com.email.model.Message;
import com.email.repository.MessageRepository;
import com.email.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    public Message addEmail(Message message) {

//        LocalDateTime date = LocalDateTime.now();
        return messageRepository.save(message);
    }
}
