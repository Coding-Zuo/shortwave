package com.example.demo.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description 
 * @ClassName Receiver
 * @Author 鲸落
 * @date 2020.07.13 18:01
 */
@Component
public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    /**
     * 接收到消息的方法，message就是指从主题获取的消息，主题配置在RedisMessageListener配置类做配置
     * @param message
     */
    public void receiveMessage(String message) {
        //TestService testService=new TestService();
        //testService.getData();
        LOGGER.info("Received <" + message + ">");

    }
}
