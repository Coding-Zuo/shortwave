package com.example.demo.xt4b;

import ac.nci.xt4b.messageClient.Body;
import ac.nci.xt4b.messageClient.Client;
import ac.nci.xt4b.messageClient.Topic;
import ac.nci.xt4b.messageClient.UserMsg;
import ac.nci.xt4b.messageClient.impl.ClusterMqClient;

public class SendTest {

    public static void main(String[] args) throws Exception {

        // 连接消息服务器
        //final String broker = "192.168.1.226:9876";
        final String broker = "192.168.199.128:9876";
        Client messageClient = new ClusterMqClient();
        messageClient.connect(broker);

        // 发送消息，消息大类为2005，消息小类为2009，目标消息客户端为5554，消息体为“messagebody_p2p”
        Topic sendTopic_2005_2009 = new Topic("2006", "2009", "5558");
        UserMsg msg_2005_2009 = new UserMsg();
        msg_2005_2009.setTopic(sendTopic_2005_2009);
        msg_2005_2009.setBody(new Body("messagebody_p2p".getBytes()));
        messageClient.sendMessage(msg_2005_2009);

        // 发送消息，消息大类为2005，消息小类为2008，消息群发，消息体为“messagebody_broadcast”
        /*Topic sendTopic_2005_2008 = new Topic("2006", "2008");
        UserMsg msg_2005_2008 = new UserMsg();
        msg_2005_2008.setTopic(sendTopic_2005_2008);
        msg_2005_2008.setBody(new Body("messagebody_broadcast".getBytes()));
        messageClient.sendMessage(msg_2005_2008);*/
    }
}
