package com.example.demo.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * <p>
 */
@Message
@Data
@NoArgsConstructor
//@AllArgsConstructor
public class WebSocketMessageEntity implements Serializable {
    /**
     * 序列化字段
     */
    private static final long serialVersionUID = 1579258906605843062L;

    public WebSocketMessageEntity(String acceptName, String content, String method) {
        this.acceptName = acceptName;
        this.content = content;
        this.method = method;
    }

    /**
     * 接收人
     */
    @Index(0)
    private String acceptName;

    /**
     * 内容
     */
    @Index(1)
    private String content;

    /**
     * 方式
     * client -> webClient
     * client -> client
     */
    @Index(2)
    private String method;
}
