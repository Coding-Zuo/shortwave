package com.example.demo.xt4b;

import ac.nci.xt4b.messageClient.Client;
import ac.nci.xt4b.messageClient.Topic;
import ac.nci.xt4b.messageClient.impl.ClusterMqClient;

public class ReceiveTest2 {

    public static void main(String[] args) throws Exception {

        // 连接消息服务器
        //final String broker = "192.168.1.226:9876";
        final String broker = "192.168.199.128:9876";
        Client messageClient = new ClusterMqClient();
        messageClient.connect(broker);

        // 订阅消息，消息大类为2005，消息小类为2009，消息客户端ID为5554
        Topic registerTopic_2005_2009 = new Topic("2006", "2008", "5557");
        try {
            messageClient.register(registerTopic_2005_2009, false, userMsg -> System.out.println(new String(userMsg.getBody().getMsgBody())));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
