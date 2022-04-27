package com.kkzz.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.kkzz.serialize.Serializer;

public class FastJsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}
