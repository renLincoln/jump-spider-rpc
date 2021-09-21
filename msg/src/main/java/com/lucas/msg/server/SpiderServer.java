package com.lucas.msg.server;

import com.lucas.msg.codec.FixHeadAndAssignLengthDecoder;
import com.lucas.msg.codec.FixHeadAndAssignLengthEncoder;
import com.lucas.msg.dto.SpiderRequest;
import com.lucas.msg.dto.SpiderResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import org.springframework.context.ApplicationContext;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: 服务器类
 * @create: 2021-08-29 23:33
 **/
@Data
public class SpiderServer {
    /**
     * 向外暴露 channelFuture 以便判断服务器正常启动
     */
    private ChannelFuture channelFuture;

    /**
     * @Author: ren
     * @Description: 创建Netty服务
     * @Param: [noUsePort]
     * @return: void
     * @Date: 2021/9/13
     */
    public void serverRun(int noUsePort,ApplicationContext applicationContext) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new FixHeadAndAssignLengthDecoder(SpiderRequest.class),
                                    new FixHeadAndAssignLengthEncoder(SpiderResponse.class),
                                    new SpiderServerHandler(applicationContext));
                        }
                    });
            channelFuture = b.bind(noUsePort).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
