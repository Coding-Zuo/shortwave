package com.example.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @Description 
 * @ClassName NettyServerInitializer8889
 * @Author 鲸落
 * @date 2020.07.17 13:24
 */
@Slf4j
public class NettyServerInitializer8889 extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) {
        ch.pipeline().addLast(new StringDecoder());//Decode:解码，将收到的ByteBuf解码为String
        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                // 打印客户端发送的消息
                log.info("客户端发送的数据1 -> " + msg);

                // 回复数据到客户端
                byte[] bytes = "我是服务端，我在向客户端发送数据1".getBytes(Charset.forName("utf-8"));
                ByteBuf buffer = ctx.alloc().buffer();
                buffer.writeBytes(bytes);
                ctx.channel().writeAndFlush(buffer);
            }
        });
        int localPort = ch.localAddress().getPort();
        System.out.println(localPort);
    }
}
