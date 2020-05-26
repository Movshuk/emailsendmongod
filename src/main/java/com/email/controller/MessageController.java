package com.email.controller;

import com.email.exception.ResourceNotFoundException;
import com.email.model.Message;
import com.email.model.MessagePattern;
import com.email.model.UploadFileResponse;
import com.email.service.FileStorageService;
import com.email.service.serviceImpl.MessageServiceImpl;
import com.google.common.collect.ImmutableSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@Api(value = "API Description")
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageServiceImpl messageServiceImpl;
    @Autowired
    private FileStorageService fileStorageService;

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

        final Path path = fileStorageService.storeFile(file);

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


}
