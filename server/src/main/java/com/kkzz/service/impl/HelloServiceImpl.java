package com.kkzz.service.impl;

import com.kkzz.rpcInterface.HelloService;
import com.kkzz.to.HelloObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到了:{}", object.getMessage());
        return "调用返回Id为" + object.getId();
    }
}
