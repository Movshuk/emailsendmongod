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
    private Status status;    // new, success, error
    private String subject;
    private String text;
    private String to;
    private String from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    private Set<String> attachments;

    private List<String> copyTo;

    enum Status {
        NEW, ERROR, SUCCESS
    }


    public Message() {
    }

    public Message(String text,
                   LocalDateTime date
    ) {
        this.text = text;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Enum getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<String> attachments) {
        this.attachments = attachments;
    }

    public List<String> getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(List<String> copyTo) {
        this.copyTo = copyTo;
    }
}
