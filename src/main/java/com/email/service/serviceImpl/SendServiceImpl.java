package com.email.service.serviceImpl;

import com.email.model.Message;
import com.email.repository.MessageRepository;
import com.email.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendServiceImpl implements SendService {

    private JavaMailSender javaMailSender;

    @Autowired
    MessageRepository messageRepository;

    @Value("${USERNAME}")
    String emailFrom;

    @Autowired
    public SendServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public Map<String, Boolean> sendSimpleEmail(Message message) throws MailException {

        Map<String, Boolean> response = new HashMap<>();
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(message.getTo());
        email.setFrom(message.getFrom());
        email.setSubject(message.getSubject());
        email.setText(message.getText());

        try {
            javaMailSender.send(email);

        } catch (MailException ex) {
            response.put("Email was not sent.", Boolean.FALSE);
        } finally {
            if(response.isEmpty()) {
                response.put("Email was sent.", Boolean.TRUE);
            }
            return response;
        }
    }

    public Map<String, Boolean> sendEmailWithAttachment(Message message)
            throws MessagingException, FileNotFoundException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setTo(message.getTo());
        messageHelper.setFrom(emailFrom);
        messageHelper.setSubject(message.getSubject());
        messageHelper.setText(message.getText());

        if (message.getAttachments() != null) {
            Object[] attachments = message.getAttachments().toArray();
            for(int i=0; i < attachments.length; i++){
                FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachments[i].toString()));
                messageHelper.addAttachment(file.getFilename(), file);
            }
        }

        if (message.getCopyTo() != null) {
            messageHelper.setCc(
                    message.getCopyTo()
                        .stream()
                        .filter(e -> !e.isEmpty())
                        .toArray(String[]::new)
            );
        }

        Map<String, Boolean> response = new HashMap<>();

        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException ex) {
            response.put("Email was not sent.", Boolean.FALSE);
        } finally {
            if(response.isEmpty()) {
                response.put("Email was sent.", Boolean.TRUE);
            }
            return response;
        }
    }


}
