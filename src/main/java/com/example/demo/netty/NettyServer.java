package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import javax.annotation.PostConstruct;

/**
 * @Description Netty 服务端
 * @ClassName NettyServer
 * @Author 鲸落
 * @date 2020.07.15 14:47
 */
public class NettyServer {
    @PostConstruct
    public void start() {
        //接收连接
        NioEventLoopGroup boos = new NioEventLoopGroup();
        //处理连接
        NioEventLoopGroup worker = new NioEventLoopGroup();

        //创建引导类（引导服务端的启动工作）
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap
                    .group(boos, worker)//配置线程组
                    .channel(NioServerSocketChannel.class)//指定服务的IO模型为Nio
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {//处理新连接数据的读写处理逻辑，服务端用childHandler，客户端用handler
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(new StringDecoder());//Decode:解码，将收到的ByteBuf解码为String
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                    System.out.println(msg);
                                }
                            });
                        }
                    })
                    .bind(8888)
                    .sync();//绑定端口
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
