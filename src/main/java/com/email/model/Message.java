package com.email.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private Enum status;    // new, success, error
    private String subject;
    private String text;
    private String to;
    private String from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    private Set<String> attachments;

    private List<String> copyTo;

    public Message() {
    }

    public Message(String text,
                   LocalDateTime date
    ) {
        this.text = text;
        this.date = date;
    }
}
