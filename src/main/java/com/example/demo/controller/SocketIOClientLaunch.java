package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description 
 * @ClassName SocketIOClientLaunch
 * @Author 鲸落
 * @date 2020.07.14 14:39
 */
@Slf4j
public class SocketIOClientLaunch {
    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            //连接服务端socket
            SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);
            socketChannel.connect(socketAddress);

            int sendCount = 0;

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //这里最好使用selector处理
            while (sendCount < 10) {
                buffer.clear();
                //向服务端发送消息
                buffer.put(("current time : " + System.currentTimeMillis()).getBytes());
                //读取模式
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();

                //从服务端读取消息
                int readLenth = socketChannel.read(buffer);
                //读取模式
                buffer.flip();
                byte[] bytes = new byte[readLenth];
                buffer.get(bytes);
                System.out.println(new String(bytes, "UTF-8"));
                buffer.clear();


                sendCount++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SocketIOClientLaunch().start();
    }
}
