package com.example.demo.xt4b;

import ac.nci.xt4b.messageClient.Body;
import ac.nci.xt4b.messageClient.Client;
import ac.nci.xt4b.messageClient.Topic;
import ac.nci.xt4b.messageClient.UserMsg;
import ac.nci.xt4b.messageClient.callback.MessageReceiveCallBack;
import ac.nci.xt4b.messageClient.impl.ClusterMqClient;
import com.example.demo.netty.attr.SessionUtil;
import com.example.demo.netty.handler.QTCommandHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description 转发端
 * @ClassName ForwardTest
 * @Author 鲸落
 * @date 2020.07.27 13:58
 */
@Slf4j
@Component
public class UpForwardListener {

    public final static String broker = "127.0.0.1:9876";


    public void start() throws Exception {
        log.info("SDK上传监听启动");
        QTCommandHandler qtCommandHandler = new QTCommandHandler();
        Channel channel = SessionUtil.getChannel(qtCommandHandler.qtIp);
        Client messageClient = new ClusterMqClient();
        messageClient.connect(broker);

        Topic registerTopic_2010_3000 = new Topic("2010", "3000", "5559");
        Topic sendTopic_2010_3200 = new Topic("2010", "3200", "5560");
        Topic sendTopic_2010_3300 = new Topic("2010", "3300", "5561");

        Topic registerTopic_2010_3010 = new Topic("2010", "3010", "5562");
        Topic sendTopic_2010_3210 = new Topic("2010", "3210", "5563");
        Topic sendTopic_2010_3310 = new Topic("2010", "3310", "5564");

        messageClient.register(registerTopic_2010_3000, true,
                new MessageReceiveCallBack() {
                    @Override
                    public void messageReceive(UserMsg userMsg) {
                        //将msg提取出来以备后续转发使用
                        String msg = new String(userMsg.getBody().getMsgBody());
                        log.info("2010_3000:" + msg.getBytes());
                        ByteBuf byteBuf = Unpooled.directBuffer(msg.length());
                        byteBuf.writeBytes(msg.getBytes());
                        channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
                    }
                });

        messageClient.register(sendTopic_2010_3200, true,
                new MessageReceiveCallBack() {
                    @Override
                    public void messageReceive(UserMsg userMsg) {
                        //将msg提取出来以备后续转发使用
                        String msg = new String(userMsg.getBody().getMsgBody());
                        log.info("2010_3200:" + msg);
                        channel.writeAndFlush(new TextWebSocketFrame(msg));
                    }
                });

        messageClient.register(sendTopic_2010_3300, true,
                new MessageReceiveCallBack() {
                    @Override
                    public void messageReceive(UserMsg userMsg) {
                        //将msg提取出来以备后续转发使用
                        String msg = new String(userMsg.getBody().getMsgBody());
                        log.info("2010_3300:" + msg);
                        channel.writeAndFlush(new TextWebSocketFrame(msg));
                    }
                });

        messageClient.register(registerTopic_2010_3010, true,
                new MessageReceiveCallBack() {
                    @Override
                    public void messageReceive(UserMsg userMsg) {
                        //将msg提取出来以备后续转发使用
                        String msg = new String(userMsg.getBody().getMsgBody());
                        log.info("2010_3010:" + msg.getBytes());
                        ByteBuf byteBuf = Unpooled.directBuffer(msg.length());
                        byteBuf.writeBytes(msg.getBytes());
                        channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
                    }
                });

        messageClient.register(sendTopic_2010_3210, true,
                new MessageReceiveCallBack() {
                    @Override
                    public void messageReceive(UserMsg userMsg) {
                        //将msg提取出来以备后续转发使用
                        String msg = new String(userMsg.getBody().getMsgBody());
                        log.info("2010_3210:" + msg);
                        channel.writeAndFlush(new TextWebSocketFrame(msg));
                    }
                });

        messageClient.register(sendTopic_2010_3310, true,
                new MessageReceiveCallBack() {
                    @Override
                    public void messageReceive(UserMsg userMsg) {
                        //将msg提取出来以备后续转发使用
                        String msg = new String(userMsg.getBody().getMsgBody());
                        log.info("2010_3310:" + msg);
                        channel.writeAndFlush(new TextWebSocketFrame(msg));
                    }
                });

    }
}
