package com.example.demo.netty.handler;

import com.example.demo.netty.attr.Session;
import com.example.demo.netty.attr.SessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class QTCommandHandler extends SimpleChannelInboundHandler<String> {

    public static final QTCommandHandler INSTANCE = new QTCommandHandler();
    List<String> shebeiList = new ArrayList<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        InetSocketAddress inteSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        SocketAddress socketAddress=ctx.channel().localAddress();
        String port=socketAddress.toString().split(":")[1];
        String id = inteSocket.toString();
        Session session;
        if(port.equals("8888")){
            session = new Session(id, "qt");
        }else{
            session = new Session(id, id);
        }
        SessionUtil.bindSession(session, ctx.channel());
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        log.info("地址" + id + "加入");
        channelGroup.add(ctx.channel());
        shebeiList.add(id);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        String arr[] = s.split(" ");
        String shebei = arr[1];

        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        for (Channel channel : channelGroup) {
            System.out.println(channel.toString());
        }

        if (arr[1].equals(0)) {
            Channel channel1 = SessionUtil.getChannel(shebei);
            byte[] bytes = "我是服务端，我在向客户端发送数据".getBytes(Charset.forName("utf-8"));
            ByteBuf buffer = channel1.alloc().buffer();
            buffer.writeBytes(bytes);
            channel1.writeAndFlush(buffer);
        }


    }
}
