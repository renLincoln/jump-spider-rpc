package com.lucas.msg.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lucas.msg.dto.SpiderResponse;
import com.lucas.msg.future.SyncWrite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;


/**
 * http://www.itstack.org
 * create by fuzhengwei on 2019/5/6
 */
@Slf4j
public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        SpiderResponse msg = (SpiderResponse) obj;
        String requestId = msg.getRequestId();
        // 尝试通过 requestId 获取到 Promise
        if (SyncWrite.promiseMap.containsKey(requestId)) {
            // 设置 Promise 为成功，并返回成功数据
            SyncWrite.promiseMap.get(requestId).setSuccess(msg);
        } else {
            log.warn("当前响应数据已失效：" + JSON.toJSONString(msg));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
