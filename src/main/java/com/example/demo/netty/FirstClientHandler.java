package com.example.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @Description 
 * @ClassName FirstClientHandler
 * @Author 鲸落
 * @date 2020.07.16 16:49
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    //在客户端连接成功后会调用该方法
    public void channelActive(ChannelHandlerContext ctx) {
        //方法中编写向服务端写数据的逻辑
        System.out.println(new Date() + ": 客户端写出数据");

        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好，鲸落!".getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        // 4. 往服务端写数据
        ctx.channel().writeAndFlush(buffer);
    }

//    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
//
//        return buffer;
//    }
}
