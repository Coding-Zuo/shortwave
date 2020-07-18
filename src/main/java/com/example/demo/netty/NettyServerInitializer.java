package com.example.demo.netty;

import com.example.demo.netty.attr.PipelineUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new StringDecoder());//Decode:解码，将收到的ByteBuf解码为String
        // 添加tcp/websocket通用handler
        PipelineUtil.addHandler(pipeline);

    }
}
