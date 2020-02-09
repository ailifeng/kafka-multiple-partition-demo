package com.alf.kafkamultiplepartitiondemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Random;

@EnableBinding(PersonStream.class)
public class SinkSender {
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final Logger logger = LoggerFactory.getLogger(SinkSender.class);
    private static final String[] USER_NAME= new String[] {
            "ai", "tom", "alma",
    };

    @InboundChannelAdapter(channel = PersonStream.OUTPUT, poller = @Poller(fixedRate = "1000"))
    public Message<?> timerPersonMessage() {
        String curName = USER_NAME[RANDOM.nextInt(USER_NAME.length)];
        String value = "{\"name\":\"" + curName + "\", \"age\":\"39\"}";
        logger.info("sending " + value);
        return MessageBuilder.withPayload(value)
                .setHeader("partitionKey", curName)
                .build();
    }
}
