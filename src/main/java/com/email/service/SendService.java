package com.email.service;

import com.email.model.Message;
import com.email.model.MessageStatus;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface SendService {
    Map<String, Boolean> sendSimpleEmail(Message message);
    Map<String, Boolean> sendEmailWithAttachment(Message message) throws MessagingException, FileNotFoundException;


}
