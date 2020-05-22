package com.email.config;

import com.email.model.Message;
import com.email.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PreRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PreRunner.class);

    @Autowired
    MessageRepository messageRepository;

    @Override
    public void run(String... args) throws Exception {
            logger.info("The PreRunner is working.");
            Message message = new Message(
                    "TEXT",
                    LocalDateTime.now()
            );

            if(messageRepository.count() != 0) {
                logger.info("Collection is not empty.");
            } else {
                messageRepository.save(message);
                logger.info("Collection is empty. Object was saved.");
            }
            logger.info("The PreRunner is finished.");
    };

}
