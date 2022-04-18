package com.kkzz;

import com.alibaba.fastjson.JSON;
import com.kkzz.dto.RpcRequest;
import com.kkzz.rpcInterface.HelloService;
import com.kkzz.service.impl.HelloServiceImpl;
import com.kkzz.to.HelloObject;
import org.junit.Test;

public class SerializerTest {
    @Test
    public void test01(){
        HelloObject object = new HelloObject(11, "你好");
        byte[] bytes = JSON.toJSONBytes(object);

        Object o = JSON.parseObject(bytes, object.getClass());
        System.out.println(o);
    }
}

