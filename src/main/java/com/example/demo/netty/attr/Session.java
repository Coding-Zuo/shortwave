package com.example.demo.netty.attr;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Session会话（设备信息）
 */
@Data
@NoArgsConstructor
public class Session {

    /**
     * 设备唯一标识
     */
    private String id;
    private String name;

    public Session(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
