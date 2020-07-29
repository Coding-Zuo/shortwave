package com.example.demo.xt4b;

import ac.nci.xt4b.messageClient.Body;
import ac.nci.xt4b.messageClient.Client;
import ac.nci.xt4b.messageClient.Topic;
import ac.nci.xt4b.messageClient.UserMsg;
import ac.nci.xt4b.messageClient.callback.MessageReceiveCallBack;
import ac.nci.xt4b.messageClient.impl.ClusterMqClient;

/**
 * @Description 转发端
 * @ClassName ForwardTest
 * @Author 鲸落
 * @date 2020.07.27 13:58
 */
public class ForwardTest {
    public static void main(String[] args) throws Exception {
        final String broker = "127.0.0.1:9876";
        Client messageClient = new ClusterMqClient();
        messageClient.connect(broker);

        // 配置接收端接收来自sendTest的消息 消息大类为2006，消息小类为2009，消息客户端ID为5558
        Topic registerTopic_2006_2009_5558 = new Topic("2006", "2009", "5558");
        // 配置发送端发送来自sendTest的消息 消息大类为2006，消息小类为2009，消息客户端ID为5556
        Topic sendTopic_2006_2009_5556 = new Topic("2006", "2009", "5556");
        try {
            messageClient.register(registerTopic_2006_2009_5558, true,
                    new MessageReceiveCallBack() {
                        @Override
                        public void messageReceive(UserMsg userMsg) {
                            //将msg提取出来以备后续转发使用
                            String msg = new String(userMsg.getBody().getMsgBody());

                            try {
                                final String broker = "192.168.199.128:9876";
                                Client messageClient = new ClusterMqClient();
                                messageClient.connect(broker);
                                UserMsg forwardMsg = new UserMsg();
                                forwardMsg.setTopic(sendTopic_2006_2009_5556);
                                forwardMsg.setBody(new Body(msg.getBytes()));
                                messageClient.sendMessage(forwardMsg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
