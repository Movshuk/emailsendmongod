package com.email.service;

import com.email.model.Message;
import com.email.model.MessagePattern;
import com.email.model.MessageStatus;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public interface MessageService {
    // CRUD
    Message addMessage(MessagePattern messagePattern);

    List<Message> getAllEmailsSortedByDate(String sortParam);

    Message getMessageById(String id);

    Map<String, Boolean> deleteMessageById(String id);

    void save(Message message);

    Map<String, Integer> getCountMessages();

    List<Message> getAllMessagesByParams(String id,
                                         String status,
                                         String subject,
                                         String from,
                                         String to);

    List<Message> getAllWithStatus(MessageStatus status);
}
