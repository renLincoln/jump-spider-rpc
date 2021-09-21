package com.lucas.msg.dto;

import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * http://www.itstack.org
 * create by fuzhengwei on 2019/5/6
 */
@Data
@Builder
@ToString
public class SpiderResponse {

    private transient Channel channel;
    private String requestId;
    private Object result;

}
