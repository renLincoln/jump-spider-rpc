package com.lucas.msg.server;

import com.lucas.common.util.ReflectUtil;
import com.lucas.msg.dto.SpiderRequest;
import com.lucas.msg.dto.SpiderResponse;
import com.sun.media.jfxmedia.logging.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: 服务类自定义handler
 * @create: 2021-08-29 23:33
 **/
@Data
@Slf4j
public class SpiderServerHandler extends ChannelInboundHandlerAdapter {

    private ApplicationContext applicationContext;

    public SpiderServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SpiderRequest reqMsg = (SpiderRequest) msg;
        // 从容器中获取bean
        Object bean = applicationContext.getBean(reqMsg.getAlias());
        if(bean==null){
            log.error("未能从容器中获得到名为：{}的bean，远程调用失败",reqMsg.getAlias());
        }
        // 利用反射获取结果
        Object result = ReflectUtil.reflectHandle(bean, reqMsg.getMethodName(), reqMsg.getParamTypes(), reqMsg.getArgs());
        // 封装 response 对象
        SpiderResponse response = SpiderResponse.builder().requestId(reqMsg.getRequestId())
                .result(result).build();
        // 数据写入返回
        ctx.writeAndFlush(response);
        //释放
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


}
