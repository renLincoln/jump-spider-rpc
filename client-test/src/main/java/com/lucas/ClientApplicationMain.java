package com.lucas;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 启动函数
 * @create: 2021-09-15 23:37
 **/
public class ClientApplicationMain {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("spider-rpc-consumer.xml");
        CallService consumer = (CallService) classPathXmlApplicationContext.getBean("consumer");
        String spider = consumer.callMyName("spider");
        System.out.println(spider);
    }
}
