package com.example.demo.service;

/**
 * @Description 
 * @ClassName SocketIOService
 * @Author 鲸落
 * @date 2020.07.14 11:18
 */
public interface SocketIOService {
    /**
     * 启动服务
     */
    void start();

    /**
     * 停止服务
     */
    void stop();

    /**
     * 推送信息给指定客户端
     *
     * @param userId:     客户端唯一标识
     * @param msgContent: 消息内容
     */
    void pushMessageToUser(String userId, String msgContent);
}
