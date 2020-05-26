package com.email.service.serviceImpl;

import com.email.exception.ResourceNotFoundException;
import com.email.model.Message;
import com.email.model.MessagePattern;
import com.email.model.MessageStatus;
import com.email.repository.MessageRepository;
import com.email.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    MessageRepository messageRepository;

    public Message addMessage(MessagePattern messagePattern) {

        Message message = new Message();

        message.setFrom(messagePattern.getFrom());
        message.setSubject(messagePattern.getSubject());
        message.setTo(messagePattern.getTo());
        message.setText(messagePattern.getText());
        message.setDate(LocalDateTime.now());
        message.setStatus(MessageStatus.NEW);

        return messageRepository.save(message);
    }

    public Message getMessageById(String id) {
        return messageRepository.findById(id).get();
    }

    /**
     * Return sorted collection of messages.
     * @param sortParam - ignore case, null => "asc", other => "desc"
     * @return List<Messages>
     */
    public List<Message> getAllEmailsSortedByDate(String sortParam) {
        logger.info("The service that returns the collection was called.");

        if(sortParam == null) {
            sortParam = "asc";
        }

        return messageRepository.findAll()
                .stream()
                .sorted(new MessageComparatorImpl(sortParam))
                .collect(Collectors.toList());
    }

    public Map<String, Boolean> deleteMessageById(String id) {

        Map<String, Boolean> response = new HashMap<>();
        Optional<Message> messageToFind = null;

        try {
            messageToFind = messageRepository.findById(id);

            if(messageToFind.isEmpty())
                throw new ResourceNotFoundException("not found");
        } catch(ResourceNotFoundException ex) {
            response.put("Record is not deleted.", Boolean.FALSE);
        } finally {

            if(response.isEmpty()) {
                messageRepository.deleteById(id);
                response.put("Record is deleted.", Boolean.TRUE);
            }
            return response;
        }
    }

    public boolean existAttachment(String attachment) {
        List<Message> messages = messageRepository.withAttachment(attachment);

        return !messages.isEmpty();
    }

    public void save(Message message) {
        messageRepository.save(message);
    }
}
