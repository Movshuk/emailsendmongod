package com.email;

import com.email.controller.MessageController;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

@SpringBootTest
class EmailApplicationTests {

    private Logger logger = LoggerFactory.getLogger(EmailApplicationTests.class);

    @Autowired
    private MessageController controller;

    @Test
    void contextLoads() {
        logger.info("Run contextLoads test.");
        Assertions.assertNotEquals(controller, null);
    }

}
