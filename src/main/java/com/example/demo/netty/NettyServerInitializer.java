package com.example.demo.netty;

import com.example.demo.util.MsgToByte;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

@Slf4j
@ChannelHandler.Sharable
public class NettyServerInitializer extends ChannelInitializer<NioSocketChannel> {
    int localPort;
    ChannelHandlerContext ctx8888;
    ChannelHandlerContext ctx8889;
    @Override
    protected void initChannel(NioSocketChannel ch) {
        localPort = ch.localAddress().getPort();
        ch.pipeline().addLast(new StringDecoder());//Decode:解码，将收到的ByteBuf解码为String
        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {



            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                super.channelActive(ctx);
                log.info("通道激活");
                log.info(String.valueOf(ctx.channel().localAddress()));
                Channel s=ctx.channel();
                InetSocketAddress inteSocket=(InetSocketAddress) ctx.channel().remoteAddress();
                SocketAddress socketAddress=ctx.channel().localAddress();
                SocketChannel socketChannel= (SocketChannel) ctx.channel();
                String p=socketChannel.localAddress().toString();
                if(p.equals("/192.168.31.142:8888")){
                    ctx8888=ctx;
                }else{
                    ctx8889=ctx;
                }
//                System.out.println("端口 已有客户端连接***"+inteSocket.getPort());
                super.channelActive(ctx);
            }

            @Override
            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                // 打印客户端发送的消息
                String result = MsgToByte.toBinary(msg);
                log.info("客户端发送的数据 -> " + result);//设备号 消息类型
                log.info("当前所连接的端口：" + localPort);

                SocketAddress socketAddress=ctx.channel().localAddress();
                InetSocketAddress inteSocket=(InetSocketAddress) ctx.channel().remoteAddress();



                if(true){
                    if(ctx8889!=null){
                        byte[] bytes = "我是服务端，我在向客户端8889发送数据".getBytes(Charset.forName("utf-8"));
                        ByteBuf buffer = ctx8889.alloc().buffer();
                        buffer.writeBytes(bytes);
                        ctx8889.channel().writeAndFlush(buffer);
                    }else {
//                        if(ctx8888!=null){
//                            byte[] bytes = "我是服务端，我在向客户端8888发送数据".getBytes(Charset.forName("utf-8"));
//                            ByteBuf buffer = ctx8888.alloc().buffer();
//                            buffer.writeBytes(bytes);
//                            ctx8888.channel().writeAndFlush(buffer);
//                        }
                    }
                }else{
                    if(ctx8888!=null){
                        byte[] bytes = "我是服务端，我在向客户端8888发送数据".getBytes(Charset.forName("utf-8"));
                        ByteBuf buffer = ctx8888.alloc().buffer();
                        buffer.writeBytes(bytes);
                        ctx8888.channel().writeAndFlush(buffer);
                    }
                }


            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                super.channelRead(ctx, msg);
            }
        });
    }
}
