package com.example.demo.redis.receiver;

import com.example.demo.netty.attr.SessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Date;

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
        System.out.println("收到的消息为：" + message);
        //TODO 处理message区分设备
        Channel channel= SessionUtil.getChannel("/192.168.31.184");
        channel.writeAndFlush(new TextWebSocketFrame(new Date() + " 服务器将你发的消息原样返回："+message));
//        byte[] bytes = "我是服务端，我在向客户端发送数据".getBytes(Charset.forName("utf-8"));
//        ByteBuf buffer = channel.alloc().buffer();
//        buffer.writeBytes(bytes);
//        channel.writeAndFlush(buffer);
    }
}
