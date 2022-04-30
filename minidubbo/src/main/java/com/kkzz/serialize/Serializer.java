package com.kkzz.serialize;

import com.kkzz.extension.Spi;

@Spi
public interface Serializer {
    byte[] serialize(Object obj);
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
