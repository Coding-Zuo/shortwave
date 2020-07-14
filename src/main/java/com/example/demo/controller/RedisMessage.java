package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Description redis 服务端
 * @ClassName SendRedisMessage
 * @Author 鲸落
 * @date 2020.07.13 17:54
 */
@RestController
@RequestMapping("/test")
public class RedisMessage {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/sendRedisMessageTest")
    public String SendRedisMessage() {
        //第一个参数是，消息推送的主题名称；第二个参数是，要推送的消息信息
        //"chat" -> 主题
        //"陕西省西安市" -> 要推送的消息
        stringRedisTemplate.convertAndSend("aszh", "陕西省西安市");
        stringRedisTemplate.convertAndSend("as", "陕西省西安市长安区");
        return  "Send Success" ;
    }
}
