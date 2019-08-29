package com.redis._04subpub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageListener implements MessageListener {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();

        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String msgBody = (String) stringSerializer.deserialize(body);
        log.info("msgBody = {}", msgBody);

        String msgChannel = (String) stringSerializer.deserialize(channel);
        log.info("msgChannel = {}",msgChannel);
        String patternStr = new String(pattern);
        log.info("pattern = {}.", patternStr);
    }
}
