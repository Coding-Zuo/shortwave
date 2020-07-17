package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
    private static NettyServer nettyServer;

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    private int port = 8888;

    @PostConstruct
    public void init() {
        nettyServer = this;
    }

    private static class SingletionNettyServer {
        static final NettyServer instance = new NettyServer();
    }

    public static NettyServer getInstance() {
        return SingletionNettyServer.instance;
    }

    public NettyServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerInitializer());
    }

    public void start() {
        log.info("通道配置");
        this.channelFuture = serverBootstrap.bind(nettyServer.port);//绑定端口，用作服务端的端口
        log.info("netty server 启动完毕,启动端口为：" + nettyServer.port);
    }
}
