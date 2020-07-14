package com.example.demo.service.impl;

import com.example.demo.service.ServerHandlerBs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Description 
 * @ClassName ServerHandlerImpl
 * @Author 鲸落
 * @date 2020.07.14 17:03
 */
public class ServerHandlerImpl implements ServerHandlerBs {
    private int bufferSize = 1024;
    private String localCharset = "UTF-8";

    public ServerHandlerImpl() {
    }

    public ServerHandlerImpl(int bufferSize) {
        this(bufferSize, null);
    }

    public ServerHandlerImpl(String localCharset) {
        this(-1, localCharset);
    }

    public ServerHandlerImpl(int bufferSize, String localCharset) {
        this.bufferSize = bufferSize > 0 ? bufferSize : this.bufferSize;
        this.localCharset = localCharset == null ? this.localCharset : localCharset;
    }

    @Override
    public void handleAccept(SelectionKey selectionKey) throws IOException {
        //获取channel
        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        //非阻塞
        socketChannel.configureBlocking(false);
        //注册selector
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));

        System.out.println("建立请求......");
    }

    @Override
    public String handleRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

        String receivedStr = "";

        if (socketChannel.read(buffer) == -1) {
            //没读到内容关闭
            socketChannel.shutdownOutput();
            socketChannel.shutdownInput();
            socketChannel.close();
            System.out.println("连接断开......");
        } else {
            //将channel改为读取状态
            buffer.flip();
            //按照编码读取数据
            receivedStr = Charset.forName(localCharset).newDecoder().decode(buffer).toString();
            buffer.clear();

            //返回数据给客户端
            buffer = buffer.put(("received string : " + receivedStr).getBytes(localCharset));
            //读取模式
            buffer.flip();
            socketChannel.write(buffer);
            //注册selector 继续读取数据
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
        }
        return receivedStr;
    }
}
