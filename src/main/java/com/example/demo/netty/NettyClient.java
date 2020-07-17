package com.example.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Description Netty 客户端
 * @ClassName NettyClient
 * @Author 鲸落
 * @date 2020.07.15 15:46
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(group)//客户端指定线程模型
                .channel(NioSocketChannel.class)//指定IO模型为Nio
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new FirstClientHandler());//使用自定义的往服务端写数据的方法
                    }
                });

        Channel channel = bootstrap.connect("127.0.0.1", 8888).channel();
//
//        while (true) {
//            channel.writeAndFlush(new Date() + ": hello world!");
//            Thread.sleep(5000);
//        }
    }
}
