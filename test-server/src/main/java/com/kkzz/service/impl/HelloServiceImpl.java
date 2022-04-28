package com.kkzz.service.impl;

import com.kkzz.service.HelloService;
import com.kkzz.to.GreetTo;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(GreetTo greet) {
        return "这是远程调用后返回的结果: { " + greet.getId() + "," + greet.getName() + " }";
    }
}
