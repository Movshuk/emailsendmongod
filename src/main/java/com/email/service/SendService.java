package com.email.service;

import com.email.model.Message;

import java.util.Map;

public interface SendService {
    Map<String, Boolean> sendSimpleEmail(Message message);
    Map<String, Boolean> sendEmailWithAttachment(Message message);
}
