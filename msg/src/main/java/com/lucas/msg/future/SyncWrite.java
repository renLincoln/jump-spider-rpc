package com.lucas.msg.future;


import com.lucas.msg.dto.SpiderRequest;
import com.lucas.msg.dto.SpiderResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 通讯同步写入逻辑
 * @create: 2021-09-14 22:13
 **/
public class SyncWrite {
    /**
     * 存放请求的 promise，用于等待远程请求响应
     * key requestId 唯一id
     * value 控制的阻塞对象
     */
    public static Map<String,Promise> promiseMap = new ConcurrentHashMap();
    /**
     * @Author: ren
     * @Description: 同步调用远程服务
     * @Param: [channel（通讯通道）, spiderRequest（请求封装对象）, timeOut（同步等待超时时间）]
     * @return: com.lucas.msg.dto.SpiderResponse
     * @Date: 2021/9/14
     */
    public static Object writeAndSync(final Channel channel, final SpiderRequest spiderRequest, final long timeOut) throws InterruptedException, ExecutionException {
        // 设置requestId
        String requestId = UUID.randomUUID().toString();
        spiderRequest.setRequestId(requestId);
        // 获取发送对象的 future
        ChannelFuture future = channel.writeAndFlush(spiderRequest);
        // 可以添加 future 监听 addListener 来获取相关的信息
        // ..................

        // 尝试进行线程阻塞，等待相应数据由服务方返回，需要设置相应的等待超时时间
        // 创建 DefaultPromise 实例
        Promise promise = new DefaultPromise(new DefaultEventExecutor());
        try {
            promiseMap.put(requestId,promise);
            // 阻塞等待数据返回
            promise.await(timeOut, TimeUnit.SECONDS);
            Object response = promise.get();
            return ((SpiderResponse) response).getResult();
        }finally {
            // 移除不再监控的 promise
            promiseMap.remove(requestId);
        }
    }
}
