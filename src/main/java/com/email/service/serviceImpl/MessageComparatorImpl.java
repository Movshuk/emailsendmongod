package com.email.service.serviceImpl;

import com.email.model.Message;

import java.time.LocalDateTime;
import java.util.Comparator;

public class MessageComparatorImpl implements Comparator<Message> {

    private String sortParam;

    public MessageComparatorImpl(String sortParam) {
        this.sortParam = sortParam;
    }

    public int compare(Message m1, Message m2) {

        LocalDateTime date1 = m1.getDate();
        LocalDateTime date2 = m2.getDate();

        if (date1 == null && date2 != null) {
            return 1;
        } else if (date1 != null && date2 == null) {
            return -1;
        } else if (date1 == null && date2 == null) {
            return 0;
        } else {
            if(sortParam.equalsIgnoreCase("desc")) {
                return date2.compareTo(date1);
            } else {
                return date1.compareTo(date2);
            }
        }
    }
}
