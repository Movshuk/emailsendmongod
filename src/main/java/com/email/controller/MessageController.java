package com.email.controller;

import com.email.model.Message;
import com.email.service.serviceImpl.MessageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "API Description")
public class MessageController {

    @Autowired
    MessageServiceImpl messageServiceImpl;

    @CrossOrigin
    @ApiOperation(value = "Получить список всех писем с сортировкой (параметрически)",
            response = Message.class, responseContainer = "List")
    @RequestMapping(value = "/emails", method = RequestMethod.GET)
    public List<Message> getAllEmailsSortedByDate(String sortParam) {

        return messageServiceImpl.getAllEmailsSortedByDate(sortParam);
    }

}
