package com.lucas;

import org.springframework.stereotype.Service;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description:
 * @create: 2021-09-16 07:54
 **/
@Service("callService")
public class CallServiceImpl implements CallService {
    @Override
    public String callMyName(String name) {
        System.out.print("my name is " + name);
        return "my name is " + name;
    }
}
