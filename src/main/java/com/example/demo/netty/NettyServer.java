package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description Netty 服务端
 * @ClassName NettyServer
 * @Author 鲸落
 * @date 2020.07.15 14:47
 */
@Component
@Slf4j
public class NettyServer {
//    @PostConstruct
//    public void start() {
//        //接收连接
//        NioEventLoopGroup boos = new NioEventLoopGroup();
//        //处理连接
//        NioEventLoopGroup worker = new NioEventLoopGroup();
//
//        //创建引导类（引导服务端的启动工作）
//        ServerBootstrap serverBootstrap = new ServerBootstrap();
//        try {
//            serverBootstrap
//                    .group(boos, worker)//配置线程组
//                    .channel(NioServerSocketChannel.class)//指定服务的IO模型为Nio
//                    .childHandler(new ChannelInitializer<NioSocketChannel>() {//处理新连接数据的读写处理逻辑，服务端用childHandler，客户端用handler
//                        @Override
//                        protected void initChannel(NioSocketChannel ch) {
//                            ch.pipeline().addLast(new StringDecoder());//Decode:解码，将收到的ByteBuf解码为String
//                            ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//                                @Override
//                                protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//                                    System.out.println(msg);
//                                }
//                            });
//                        }
//                    })
//                    .bind(8888)
//                    .sync();//绑定端口
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private static NettyServer nettyServer;


    @PostConstruct
    public void init() {
        nettyServer = this;
    }

    private int port = 8888;

    private static class SingletionNettyServer {
        static final NettyServer instance = new NettyServer();
    }

    public static NettyServer getInstance() {
        return SingletionNettyServer.instance;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;


    public NettyServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerInitializer());
//                .childHandler(new ChannelInitializer<NioSocketChannel>() {//处理新连接数据的读写处理逻辑，服务端用childHandler，客户端用handler
//                    @Override
//                    protected void initChannel(NioSocketChannel ch) {
//                        ch.pipeline().addLast(new StringDecoder());//Decode:解码，将收到的ByteBuf解码为String
//                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//                                System.out.println(msg);
//                            }
//                        });
//                    }
//                });
    }


    public void start() {
        log.info("通道配置");
        this.future = server.bind(nettyServer.port);
        log.info("netty server 启动完毕,启动端口为：" + nettyServer.port);
    }
}
