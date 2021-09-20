package com.senla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListenerEx {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerEx.class);

    @JmsListener(destination = "user")
    public void processMessages(String message) {
        logger.info("user {}", message);
    }
}
