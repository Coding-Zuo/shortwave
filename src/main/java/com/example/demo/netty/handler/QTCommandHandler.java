package com.example.demo.netty.handler;

import ac.nci.xt4b.messageClient.Body;
import ac.nci.xt4b.messageClient.Client;
import ac.nci.xt4b.messageClient.Topic;
import ac.nci.xt4b.messageClient.UserMsg;
import ac.nci.xt4b.messageClient.impl.ClusterMqClient;
import com.example.demo.netty.attr.Session;
import com.example.demo.netty.attr.SessionUtil;
import com.example.demo.xt4b.UpForwardListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
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
        WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
        String remoteIp = ctx.channel().remoteAddress().toString().split(":")[0];
        if (remoteIp.equals(qtIp)) {
            handlerWebSocketFrame(ctx, webSocketFrame, true, remoteIp);
        } else {
            handlerWebSocketFrame(ctx, webSocketFrame, false, remoteIp);
        }
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

        if (frame instanceof BinaryWebSocketFrame) {//frame如果是二进制的会进入下面
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;//对frame强转
            ByteBuf content = binaryWebSocketFrame.content();//返回byteBuf数据
            byte[] bt3 = new byte[content.capacity()];//content.capacity()：返回此缓冲区包含的的字节数，new一个byte数组，长度就是缓冲区的字节数
            String toArmFlag = "";//用来存储获取到的协议
            for (int i = 0; i < content.capacity(); i++) {
                byte b = content.getByte(i);//返回当前索引的字节
                /**
                 * 判断第一个字节（设备号）
                 * 判断完第一个字节后，判断第二个字节（消息类型）
                 * 根据第二个字节的判断结果，判断第三个字节，如果第二个字节是全景数据，则判断第三个字节，如果第二个字节非全景数据，则不再判断第三个字节的内容
                 *
                 * 模拟new一个byte数组，长度是3，
                 * 设置一个数组，模拟取出当前索引的数组   
                 */
                if (i == 0 && b == 1) {//当字节是1的时候，定义toArmFlag为1
                    toArmFlag = "1";
                    //第一个字段是1（设备1）
                } else if (i == 0 && b == 2) {
                    toArmFlag = "2";
                    //第一个字段是2（设备2）
                } else if (i == 1 && b == 1){
                    //第二个字段是1
                    toArmFlag = toArmFlag + "1";
                } else if(toArmFlag.length() == 2 && toArmFlag.substring(1,2).equals("1")){
                    //第二个字段必须是1，并且字符串的长度必须是2
                    toArmFlag = toArmFlag + "2";
                    if (i == 2 && b == 0){
                        //第三个字段是0
                        toArmFlag = toArmFlag + "0";
                    } else if (i == 2 && b == 1){
                        //第三个字段是1
                        toArmFlag = toArmFlag + "1";
                    }
                }
                bt3[i] = b;
            }
            log.info("获取到的协议bt3：" + bt3.toString());
            // 连接消息服务器
            //final String broker = "192.168.1.226:9876";
            Client messageClient = new ClusterMqClient();
            messageClient.connect(UpForwardListener.broker);

            String msgType = "";

            if (toArmFlag.substring(0, 1).equals("1")){
                //设备1
                log.info("设备1");
                if (toArmFlag.substring(1, 2).equals("0")){
                    //停止
                    //设备1停止：2000
                    msgType = "2000";
                    log.info("配置：2000");
                } else if (toArmFlag.substring(1, 2).equals("1")){
                    //全景数据（启动）
                    if (toArmFlag.substring(2, 3).equals("0")){
                        //如果是全景数据，还需要判断第三位是信号（0）还是噪声（1）
                        //设备1启动信号：2100
                        msgType = "2100";
                        log.info("配置：2100");
                    } else if(toArmFlag.substring(2, 3).equals("1")){
                        //如果是全景数据，还需要判断第三位是信号（0）还是噪声（1）
                        //设备1启动噪声：2101
                        msgType = "2101";
                        log.info("配置：2101");
                    }
                } else if(toArmFlag.substring(1, 2).equals("2")){
                    //自动侦测
                    //设备1自动侦测：2200
                    msgType = "2200";
                    log.info("配置：2200");
                } else if(toArmFlag.substring(1, 2).equals("3")){
                    //定位信息
                    //设备1定位信息：2300
                    msgType = "2300";
                    log.info("配置：2300");
                }
            } else if (toArmFlag.substring(0, 1).equals("2")){
                //设备2
                log.info("设备2");
                if (toArmFlag.substring(1, 2).equals("0")){
                    //停止
                    //设备2停止：2010
                    msgType = "2010";
                    log.info("配置：2010");
                } else if (toArmFlag.substring(1, 2).equals("1")){
                    //全景数据（启动）
                    if (toArmFlag.substring(2, 3).equals("0")){
                        //如果是全景数据，还需要判断第三位是信号（0）还是噪声（1）
                        //设备2启动信号：2110
                        msgType = "2110";
                        log.info("配置：2110");
                    } else if(toArmFlag.substring(2, 3).equals("1")){
                        //如果是全景数据，还需要判断第三位是信号（0）还是噪声（1）
                        //设备2启动噪声：2111
                        msgType = "2111";
                        log.info("配置：2111");
                    }
                } else if(toArmFlag.substring(1, 2).equals("2")){
                    //自动侦测
                    //设备2自动侦测：2210
                    msgType = "2210";
                    log.info("配置：2210");
                } else if(toArmFlag.substring(1, 2).equals("3")){
                    //定位信息
                    //设备2定位信息：2310
                    msgType = "2310";
                    log.info("配置：2310");
                }
            }

            // 发送消息，消息大类为2010，消息小类通过指定协议动态指定小类编码，消息群发，消息体为指定协议
            Topic sendTopic_2010_msgType = new Topic("2010", msgType);
            log.info("msgType：" + msgType);
            UserMsg msg_2010 = new UserMsg();
            msg_2010.setTopic(sendTopic_2010_msgType);
            msg_2010.setBody(new Body(bt3));
            messageClient.sendMessage(msg_2010);
            log.info("发送数据");



            Channel channel = SessionUtil.getChannel(qtIp);
//            if (toArmFlag == 0) {//arm 传给 qt
//                channel = SessionUtil.getChannel(qtIp);
//            } else if (toArmFlag == 1) { //qt给arm1
//                String armIp = shebeiMap.get("设备1");
//                channel = SessionUtil.getChannel(armIp);
//            } else {
//                String armIp = shebeiMap.get("设备2");
//                channel = SessionUtil.getChannel(armIp);
//            }
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
//    private void handlerHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest req) {
//        // 构造握手响应返回，本机测试
//        WebSocketServerHandshakerFactory wsFactory
//                = new WebSocketServerHandshakerFactory("/dunabo", null, false);
//        // region 从连接路径中截取连接用户名
//        String uri = req.uri();
//        int i = uri.lastIndexOf("/");
//        String userName = uri.substring(i + 1, uri.length());
//        // endregion
//        Channel connectChannel = channelHandlerContext.channel();
//        // 加入在线用户
//        WebSocketUsers.put(userName, connectChannel);
//        socketServerHandShaker = wsFactory.newHandshaker(req);
//        if (socketServerHandShaker == null) {
//            // 发送版本错误
//            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(connectChannel);
//        } else {
//            // 握手响应
//            socketServerHandShaker.handshake(connectChannel, req);
//        }
//    }

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
