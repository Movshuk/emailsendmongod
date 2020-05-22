package com.email.service.serviceImpl;

import com.email.config.PreRunner;
import com.email.model.Message;
import com.email.repository.MessageRepository;
import com.email.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    MessageRepository messageRepository;

    public Message addEmail(Message message) {

//        LocalDateTime date = LocalDateTime.now();
        return messageRepository.save(message);
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
}
