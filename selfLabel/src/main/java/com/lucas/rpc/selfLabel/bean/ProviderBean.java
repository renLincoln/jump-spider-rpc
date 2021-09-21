package com.lucas.rpc.selfLabel.bean;

import com.lucas.common.constant.Constant;
import com.lucas.common.util.NetUtil;
import com.lucas.msg.server.SpiderServer;
import com.lucas.rpc.product.regist.ProductRegist;
import com.lucas.rpc.selfLabel.config.MsgServerBasic;
import com.luncas.common.dto.ProductInfoDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: 服务提供方Bean
 * @create: 2021-08-31 23:56
 **/
@Data
@Slf4j
public class ProviderBean implements ApplicationContextAware {
    /**
     * 接口名称
     */
    private String nozzle;
    /**
     * 服务别名分组信息
     */
    private String alias;
    /**
     * 接口实现类
     */
    private String ref;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (!MsgServerBasic.SERVER_RUN) {
            // 判斷msg服务是否已经启动，若未启动，则需要先启动netty通讯服务
            // 并将netty服务的host、port信息进行录入，并更新msg服务状态
//            final Integer noUsePort;
            boolean getUserPortFlag = false;
            for (int port = Constant.INITIAL_PORT; port < Constant.MAX_PORT; port++) {
                if (!NetUtil.isPortUsing(port)) {
                    // 更新端口状态
                    getUserPortFlag = !getUserPortFlag;
                    final int noUsePort = port;
                    // 创建server
                    SpiderServer spiderServer = new SpiderServer();
                    // 由于创建netty服务会发生阻塞，此处使用线程执行服务创建
                    new Thread(() -> {
                        spiderServer.serverRun(noUsePort,applicationContext);
                    }).start();
                    try {
                        // 获取注册服务器channelFuture
                        while (spiderServer.getChannelFuture() == null || !spiderServer.getChannelFuture().channel().isActive()) {
                            Thread.sleep(3 * 1000);
                        }
                        MsgServerBasic.SERVER_HOST = NetUtil.getHost();
                        MsgServerBasic.SERVER_PORT = noUsePort;
                        MsgServerBasic.SERVER_RUN = true;
                        // 自旋等待注册服务器的创建
                        log.info("初始化生产端服务完成 {} {}", MsgServerBasic.SERVER_HOST, MsgServerBasic.SERVER_PORT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            if (!getUserPortFlag) {
                log.error("未能正常获取有效的未开启的端口,创建服务注册中心失败");
                throw new BeanCreationException("未能正常获取有效的未开启的端口,创建服务注册中心失败");
            }
        }
        // 服务信息
        ProductInfoDto productInfoDto = new ProductInfoDto(MsgServerBasic.SERVER_HOST,MsgServerBasic.SERVER_PORT,nozzle);
        // 将服务信息注册到redis注册中心
        ProductRegist.registProduct(alias, productInfoDto);
        log.info("注册生产者：{}，详情：{}", alias, productInfoDto.toString());
    }
}
