package com.example.demo.netty.handler;

import com.example.demo.netty.WebSocketMessageEntity;
import com.example.demo.netty.WebSocketUsers;
import com.example.demo.netty.attr.Session;
import com.example.demo.netty.attr.SessionUtil;
import com.example.demo.util.MsgToByte;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@ChannelHandler.Sharable
public class QTCommandHandler extends SimpleChannelInboundHandler<Object> {

    public final String qtIp = "/192.168.31.166";
    public static final QTCommandHandler INSTANCE = new QTCommandHandler();
    Map<String, String> shebeiMap = new HashMap<String, String>() {{
        put("设备1", "/192.168.31.189");
        put("设备2", "/192.168.31.184");
    }};
    Set<String> shebeiSet = new HashSet<String>() {{
        add("test1");
        add("test2");
    }};
    private WebSocketServerHandshaker socketServerHandShaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        InetSocketAddress inteSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String remoteIp = inteSocket.toString().split(":")[0];
        Session session = new Session(remoteIp, remoteIp);
        SessionUtil.bindSession(session, ctx.channel());
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        log.info("地址" + remoteIp + "加入");
        channelGroup.add(ctx.channel());
//        SessionUtil.bindChannelGroup("1",channelGroup);
        if (!qtIp.equals(remoteIp)) { //判断是否是arm
            shebeiMap.put("设备" + shebeiMap.size() + 1, remoteIp);
            shebeiSet.add(remoteIp);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ChannelGroup channelGroup = SessionUtil.getChannelGroup("1");
//        for (Channel channel : channelGroup) {
//            System.out.println(channel.toString());
//        }

//        if (arr[1].equals(0)) {
//        Channel channel1 = SessionUtil.getChannel(shebei);
//        Channel channel2 = ctx.channel();
//        byte[] bytes = "我是服务端，我在向客户端发送数据".getBytes(Charset.forName("utf-8"));
//        ByteBuf buffer = channel2.alloc().buffer();
//        buffer.writeBytes(bytes);
        WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
        String remoteIp = ctx.channel().remoteAddress().toString().split(":")[0];
        if (remoteIp.equals(qtIp)) {
            handlerWebSocketFrame(ctx, webSocketFrame, true, remoteIp);
        } else {
            handlerWebSocketFrame(ctx, webSocketFrame, false, remoteIp);
        }
//        channel2.writeAndFlush(new TextWebSocketFrame("我是服务器,我收到你的消息为:" + content));
    }

    /**
     * webSocket处理逻辑
     *
     * @param channelHandlerContext channelHandlerContext
     * @param frame                 webSocketFrame
     */
    private void handlerWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame, boolean isQt, String remoteIp) throws Exception {
//        Channel channel = channelHandlerContext.channel();
//        Channel channel=SessionUtil.getChannelGroup("/192.168.31.166").;
        // region 判断是否是关闭链路的指令
//        if (frame instanceof CloseWebSocketFrame) {
//            log.info("├ 关闭与客户端[{}]链接", channel.remoteAddress());
//            socketServerHandShaker.close(channel, (CloseWebSocketFrame) frame.retain());
//            return;
//        }
        // endregion
        // region 判断是否是ping消息
//        if (frame instanceof PingWebSocketFrame) {
//            log.info("├ [Ping消息]");
//            channel.write(new PongWebSocketFrame(frame.content().retain()));
//            return;
//        }
        // endregion
        // region 纯文本消息
        if (frame instanceof TextWebSocketFrame) {
            Channel channel = SessionUtil.getChannel(qtIp);
            String text = ((TextWebSocketFrame) frame).text();
            log.info("├ [{} 接收到客户端的消息]: {}", new Date(), text);
            channel.writeAndFlush(new TextWebSocketFrame(text));
        }
        // endregion
        // region 二进制消息 此处使用了MessagePack编解码方式
        if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            ByteBuf content = binaryWebSocketFrame.content();
            byte[] bt3 = new byte[content.capacity()];
            int toArmFlag = 0;
            for (int i = 0; i < content.capacity(); i++) {
                byte b = content.getByte(i);
                if (i == 0 && b == 1 && isQt) {
                    toArmFlag = 1;
                } else if (i == 0 && b == 2 && isQt) {
                    toArmFlag = 2;
                }
                bt3[i] = b;
            }
            Channel channel;
            if (toArmFlag == 0) {//arm 传给 qt
                channel = SessionUtil.getChannel(qtIp);
            } else if (toArmFlag == 1) { //qt给arm1
                String armIp = shebeiMap.get("设备1");
                channel = SessionUtil.getChannel(armIp);
            } else {
                String armIp = shebeiMap.get("设备2");
                channel = SessionUtil.getChannel(armIp);
            }
            content.markReaderIndex();
//            int flag = content.readInt();
//            log.info("标志位:[{}]", flag);
            content.resetReaderIndex();

            ByteBuf byteBuf = Unpooled.directBuffer(binaryWebSocketFrame.content().capacity());
            byteBuf.writeBytes(binaryWebSocketFrame.content());

            channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));

//            log.info("├ [二进制数据]:{}", content);
//            final int length = content.readableBytes();
//            final byte[] array = new byte[length];
//            content.getBytes(content.readerIndex(), array, 0, length);
//            MessagePack messagePack = new MessagePack();
//            WebSocketMessageEntity webSocketMessageEntity = messagePack.read(array, WebSocketMessageEntity.class);
//            log.info("├ [解码数据]: {}", webSocketMessageEntity);
//            WebSocketUsers.sendMessageToUser(webSocketMessageEntity.getAcceptName(), webSocketMessageEntity.getContent());
        }
        // endregion
    }

    /**
     * 第一次握手
     *
     * @param channelHandlerContext channelHandlerContext
     * @param req                   请求
     */
    private void handlerHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest req) {
        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory
                = new WebSocketServerHandshakerFactory("/dunabo", null, false);
        // region 从连接路径中截取连接用户名
        String uri = req.uri();
        int i = uri.lastIndexOf("/");
        String userName = uri.substring(i + 1, uri.length());
        // endregion
        Channel connectChannel = channelHandlerContext.channel();
        // 加入在线用户
        WebSocketUsers.put(userName, connectChannel);
        socketServerHandShaker = wsFactory.newHandshaker(req);
        if (socketServerHandShaker == null) {
            // 发送版本错误
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(connectChannel);
        } else {
            // 握手响应
            socketServerHandShaker.handshake(connectChannel, req);
        }
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
//        String s=String.valueOf(msg.toString());
//        String arr[] = MsgToByte.toBinary(s).split(" ");
//        String shebei = arr[1];
//
//        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
//        for (Channel channel : channelGroup) {
//            System.out.println(channel.toString());
//        }
//
////        if (arr[1].equals(0)) {
//        Channel channel1 = SessionUtil.getChannel(shebei);
//        Channel channel2 = ctx.channel();
//        byte[] bytes = "我是服务端，我在向客户端发送数据".getBytes(Charset.forName("utf-8"));
//        ByteBuf buffer = channel2.alloc().buffer();
//        buffer.writeBytes(bytes);
//        channel2.writeAndFlush(buffer);
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
//        String arr[] = MsgToByte.toBinary(s).split(" ");
//        String shebei = arr[1];
//
//        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
//        for (Channel channel : channelGroup) {
//            System.out.println(channel.toString());
//        }
//
////        if (arr[1].equals(0)) {
//            Channel channel1 = SessionUtil.getChannel(shebei);
//            Channel channel2 = ctx.channel();
//            byte[] bytes = "我是服务端，我在向客户端发送数据".getBytes(Charset.forName("utf-8"));
//            ByteBuf buffer = channel2.alloc().buffer();
//            buffer.writeBytes(bytes);
//            channel1.writeAndFlush(buffer);
////        }
//
//
//    }
}
