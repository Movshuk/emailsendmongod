package com.email.controller;

import com.email.exception.ResourceNotFoundException;
import com.email.model.Message;
import com.email.model.MessagePattern;
import com.email.model.MessageStatus;
import com.email.model.UploadFileResponse;
import com.email.service.FileStorageService;
import com.email.service.serviceImpl.MessageComparatorImpl;
import com.email.service.serviceImpl.MessageServiceImpl;
import com.email.service.serviceImpl.SendServiceImpl;
import com.google.common.collect.ImmutableSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Api(value = "API Description")
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageServiceImpl messageServiceImpl;
    @Autowired
    SendServiceImpl sendServiceImpl;

    @Autowired
    private FileStorageService fileStorageServiceImpl;

    @CrossOrigin
    @ApiOperation(value = "Получить список всех писем с сортировкой (параметрически)",
            response = Message.class, responseContainer = "List")
    @RequestMapping(value = "/emails", method = RequestMethod.GET)
    public List<Message> getAllEmailsSortedByDate(String sortParam) {

        return messageServiceImpl.getAllEmailsSortedByDate(sortParam);
    }

    @CrossOrigin
    @ApiOperation(value = "Добавить письмо для отправки")
    @RequestMapping(value = "/emails/add", method = RequestMethod.POST)
    public Message createEmail(@RequestBody MessagePattern messagePattern) {
        return messageServiceImpl.addMessage(messagePattern);
    }

    @CrossOrigin
    @ApiOperation(value = "Получить сообщение по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 500, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping(value = "/emails/{id}", method = RequestMethod.GET)
    public Message getMessageById(@PathVariable("id") String id) throws ResourceNotFoundException {
        return messageServiceImpl.getMessageById(id);
    }

    @CrossOrigin
    @ApiOperation(value = "Удалить сообщение по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted")
    })
    @RequestMapping(value = "/emails/{id}/delete", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteMessageById(@PathVariable("id") String id) {
        return messageServiceImpl.deleteMessageById(id);
    }

    @CrossOrigin
    @ApiOperation(value = "Добавление файла к сообщению")
    @RequestMapping(value = "/emails/{id}/attach", produces = "application/json", method = RequestMethod.POST)
    public UploadFileResponse addAttachment(@PathVariable("id") String id,
                                            @RequestParam("file") MultipartFile file) throws ResourceNotFoundException {

        logger.info("Upload file [name:{}; type:{}; size: {}]", file.getOriginalFilename(), file.getContentType(), file.getSize());

        final Path path = fileStorageServiceImpl.storeFile(file);

        Message message = messageServiceImpl.getMessageById(id);
        final Set<String> attachments = message.getAttachments();

        if (attachments == null) {
            message.setAttachments(ImmutableSet.of(path.toString()));
        } else {
            message.getAttachments().add(path.toString());
        }
        messageServiceImpl.save(message);

        return new UploadFileResponse(
                path.toString(),
                path.toUri().toString(),
                file.getContentType(),
                file.getSize()
        );
    }

    @CrossOrigin
    @ApiOperation(value = "Удаление файла из сообщения")
    @RequestMapping(value = "/emails/{id}/attach", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> removeAttachment(@PathVariable("id") String id,
                                   @RequestParam("attachment") String attachment) {
        try {
            messageServiceImpl.removeAttachment(id, attachment);
            fileStorageServiceImpl.delete(attachment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Отправка простого сообщения по id")
    @RequestMapping(value = "/emails/{id}/simple-send", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> sendMessage(@PathVariable("id") String id) throws ResourceNotFoundException {


        Message email = messageServiceImpl.getMessageById(id);

        try {
            sendServiceImpl.sendSimpleEmail(email);
            email.setStatus(MessageStatus.SUCCESS);
            logger.info("Письмо с id: " + id + "отправлено.");
            messageServiceImpl.save(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MailException e) {
            email.setStatus(MessageStatus.ERROR);
            messageServiceImpl.save(email);
            logger.info("Ошибка отправки письма с id: " + id + " " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Отправка сообщения по id с вложением")
    @RequestMapping(value = "/emails/{id}/send", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> sendEmailWithAttachment(@PathVariable("id") String id) throws MessagingException, FileNotFoundException, ResourceNotFoundException {

        Message email = messageServiceImpl.getMessageById(id);
        try {
            sendServiceImpl.sendEmailWithAttachment(email);
            email.setStatus(MessageStatus.SUCCESS);
            messageServiceImpl.save(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            email.setStatus(MessageStatus.ERROR);
            messageServiceImpl.save(email);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Получить общее количество писем в хранилище")
    @RequestMapping(value = "/emails/count", method = RequestMethod.GET)
    public Map<String, Integer> getCountMessages() {
        return messageServiceImpl.getCountMessages();
    }

    @CrossOrigin
    @ApiOperation(value = "Редактирование письма по заданному id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully.")
    })
    @RequestMapping(value = "/emails/{id}/update", produces = "application/json", method = RequestMethod.PUT)
    public Message updateMessageById(@RequestBody Message messageNewVersion) {

        Message messageLastVersion = messageServiceImpl.getMessageById(messageNewVersion.getId());

        if (messageLastVersion == null) {
            messageServiceImpl.save(messageNewVersion);
            return messageNewVersion;
        } else {
            messageLastVersion.merge(messageNewVersion);
            messageServiceImpl.save(messageLastVersion);
            return messageLastVersion;
        }
    }

    @CrossOrigin
    @ApiOperation(value = "Получить список сообщений по параметрам")
    @RequestMapping(value = "/emails/search", method = RequestMethod.GET)
    public List<Message> getAllMessagesByParams(Integer page,
                                                Integer pageSize,
                                                String id,
                                                String status,
                                                String subject,
                                                String from,
                                                String to,
                                                String sortParam) {
        if(sortParam == null)
            sortParam = "asc";

        if(page == null)
            page = 0;

        if(pageSize == null)
            pageSize = 5;

        return messageServiceImpl.getAllMessagesByParams(id, status, subject, from, to)
                .stream()
                .sorted(new MessageComparatorImpl(sortParam))
                .skip((page) * pageSize).limit(pageSize)
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @ApiOperation(value = "Получить список сообщений по статусу")
    @RequestMapping(value = "/emails/status", method = RequestMethod.GET)
    public List<Message> getAllWithStatus(String messageStatus) {

        if(messageStatus == null || messageStatus.trim().isEmpty())
            return null;

        switch(messageStatus.toUpperCase()) {
            case "NEW", "ERROR", "SUCCESS":
                return messageServiceImpl.getAllWithStatus(MessageStatus.valueOf(messageStatus.toUpperCase()));
            default:
                return null;
        }
    }



}
