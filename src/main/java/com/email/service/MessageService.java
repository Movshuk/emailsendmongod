package com.email.service;

import com.email.model.Message;

import java.util.List;

public interface MessageService {
    // CRUD
    Message addEmail(Message message);

    List<Message> getAllEmailsSortedByDate(String sortParam);
}
