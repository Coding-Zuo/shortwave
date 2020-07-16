//package com.example.demo.service.impl;
//
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.example.demo.service.ServerHandlerBs;
//import com.example.demo.service.SocketIOService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.ServerSocketChannel;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @Description
// * @ClassName SocketIOServiceImpl
// * @Author 鲸落
// * @date 2020.07.14 11:19
// */
//@Slf4j
//@Service(value = "socketIOService")
//public class SocketIOServiceImpl implements SocketIOService {
//    /**
//     * 存放已连接的客户端
//     */
//    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
//
//    @Autowired
//    private SocketIOServer socketIOServer;
//
//    /**
//     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
//     */
//    @PostConstruct
//    private void autoStartup() {
//        start();
//    }
//
//    /**
//     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
//     */
//    @PreDestroy
//    private void autoStop() {
//        stop();
//    }
//
//    private volatile byte flag = 1;
//
//    public void setFlag(byte flag) {
//        this.flag = flag;
//    }
//
//    @Override
//    public void start() {
//        //创建serverSocketChannel，监听8888端口
//        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
//            serverSocketChannel.socket().bind(new InetSocketAddress(8888));
//            //设置为非阻塞模式
//            serverSocketChannel.configureBlocking(false);
//            //为serverChannel注册selector
//            Selector selector = Selector.open();
//            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//
//            System.out.println("服务端开始工作：");
//
//            //创建消息处理器
//            ServerHandlerBs handler = new ServerHandlerImpl(1024);
//
//            while (flag == 1) {
//                selector.select();
//                System.out.println("开始处理请求 ： ");
//                //获取selectionKeys并处理
//                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
//                while (keyIterator.hasNext()) {
//                    SelectionKey key = keyIterator.next();
//                    try {
//                        //连接请求
//                        if (key.isAcceptable()) {
//                            handler.handleAccept(key);
//                        }
//                        //读请求
//                        if (key.isReadable()) {
//                            System.out.println(handler.handleRead(key));
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //处理完后移除当前使用的key
//                    keyIterator.remove();
//                }
//                System.out.println("完成请求处理。");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void stop() {
//        if (socketIOServer != null) {
//            socketIOServer.stop();
//            socketIOServer = null;
//        }
//    }
//
//    public static void main(String[] args) {
//        SocketIOServiceImpl server = new SocketIOServiceImpl();
//        new Thread(() -> {
//            try {
//                Thread.sleep(10*60*1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }finally {
//                server.setFlag((byte) 0);
//            }
//        }).start();
//        server.start();
//    }
//}
