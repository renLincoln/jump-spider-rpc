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
public class SpiderRequest {

    private transient Channel channel;

    private String requestId;
    private String methodName;  //方法
    private Class[] paramTypes; //属性
    private Object[] args;      //入参
    private String nozzle; //接口
    private String ref;    //实现类
    private String alias;  //别名
}
