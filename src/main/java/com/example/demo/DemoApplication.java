package com.example.demo;

import com.example.demo.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo.netty","com.example.demo","com.example.demo.redis.receiver"})
@RestController
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private NettyServer nettyServer;

    @Override
    public void run(String... args) throws Exception {
        this.nettyServer.start();
    }


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
