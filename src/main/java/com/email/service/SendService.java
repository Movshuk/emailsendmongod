package com.email.service;

import com.email.model.Message;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.Map;

public interface SendService {
    Map<String, Boolean> sendSimpleEmail(Message message);
    Map<String, Boolean> sendEmailWithAttachment(Message message) throws MessagingException, FileNotFoundException;



}
