package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Description 
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

        System.out.println("Sending message...");
        //第一个参数是，消息推送的主题名称；第二个参数是，要推送的消息信息
        //"chat"->主题
        //"我是一条消息"->要推送的消息
        //stringRedisTemplate.convertAndSend("chat", "陕西省西安市");

//        //向redis里存入数据和设置缓存时间
        stringRedisTemplate.opsForValue().set("test", "100",60*10, TimeUnit.SECONDS);
//
//        //val做-1操作
//        stringRedisTemplate.boundValueOps("test").increment(-1);
//
//        //根据key获取缓存中的val
        String test = stringRedisTemplate.opsForValue().get("test");
        System.out.println(test);
//
//        //val +1
//        stringRedisTemplate.boundValueOps("test").increment(1);
//
//        //根据key获取过期时间
//        stringRedisTemplate.getExpire("test");
//
//        //根据key获取过期时间并换算成指定单位
//        stringRedisTemplate.getExpire("test",TimeUnit.SECONDS);
//
//        //根据key删除缓存
//        stringRedisTemplate.delete("test");
//
//        //检查key是否存在，返回boolean值
//        stringRedisTemplate.hasKey("546545");
//
//        //向指定key中存放set集合
//        stringRedisTemplate.opsForSet().add("red_123", "1","2","3");
//
//        //设置过期时间
//        stringRedisTemplate.expire("red_123",1000 , TimeUnit.MILLISECONDS);
//
//        //根据key查看集合中是否存在指定数据
//        stringRedisTemplate.opsForSet().isMember("red_123", "1");
//
//        //根据key获取set集合
//        stringRedisTemplate.opsForSet().members("red_123");
        return  "Send Success" ;
    }
}
