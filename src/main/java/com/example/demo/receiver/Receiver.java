package com.example.demo.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description redis 客户端
 * @ClassName Receiver
 * @Author 鲸落
 * @date 2020.07.13 18:01
 */
@Slf4j
@Component
public class Receiver {
    /**
     * 接收到消息的方法，message就是指从主题获取的消息，主题配置在RedisMessageListener配置类做配置
     * @param message
     */
    public void receiveMessage(String message) {
        //log.info("Received <" + message + ">");
        System.out.println("收到的消息为：" + message);
    }
}
