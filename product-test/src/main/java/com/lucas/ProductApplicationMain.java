package com.lucas;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 启动函数
 * @create: 2021-09-15 23:36
 **/
public class ProductApplicationMain {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("spring-config.xml");
//                new ClassPathXmlApplicationContext("spider-rpc-producter.xml");
    }
}
