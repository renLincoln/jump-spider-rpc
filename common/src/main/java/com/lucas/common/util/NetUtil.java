package com.lucas.common.util;

import com.lucas.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 网络工具类
 * @create: 2021-09-13 07:31
 **/
@Slf4j
public class NetUtil {
    /**
     * @Author: ren
     * @Description: 校验端口是否使用
     * @Param: [port]
     * @return: boolean
     * @Date: 2021/9/13
     */
    public static boolean isPortUsing(int port){
       boolean useFlag = false;
       try{
           Socket socket = new Socket("localhost", port);
           socket.close();
           useFlag = true;
       }catch(Exception e){
           log.warn("端口：{},{}",port,e.toString());
       }
       return useFlag;
    }

    /**
     * @Author: ren
     * @Description: 获取本地ip
     * @Param: []
     * @return: java.lang.String
     * @Date: 2021/9/13
     */
    public static String getHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static void main(String[] args) {
        System.out.println(Constant.MAX_PORT);
    }
}
