package com.example.demo.netty.attr;


import com.example.demo.netty.handler.QTCommandHandler;
import io.netty.channel.ChannelPipeline;

/**
 * ChannelPipeline工具类
 */
public class PipelineUtil {

    /**
     * 添加websocket/tcp通用handler
     *
     * @param pipeline
     */
    public static void addHandler(ChannelPipeline pipeline) {
        pipeline.addLast(
                //qt下发命令转发处理
                QTCommandHandler.INSTANCE
        );
    }

}
