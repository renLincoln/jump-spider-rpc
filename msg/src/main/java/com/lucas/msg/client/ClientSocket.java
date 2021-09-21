package com.lucas.msg.client;

import com.lucas.msg.codec.FixHeadAndAssignLengthDecoder;
import com.lucas.msg.codec.FixHeadAndAssignLengthEncoder;
import com.lucas.msg.dto.SpiderRequest;
import com.lucas.msg.dto.SpiderResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;

@Data
public class ClientSocket implements Runnable {

    private ChannelFuture future;

    private String host;

    private int port;

    public ClientSocket(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new FixHeadAndAssignLengthDecoder(SpiderResponse.class),
                            new FixHeadAndAssignLengthEncoder(SpiderRequest.class),
                            new MyClientHandler());
                }
            });
            ChannelFuture f = b.connect(this.host, this.port).sync();
            this.future = f;
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
