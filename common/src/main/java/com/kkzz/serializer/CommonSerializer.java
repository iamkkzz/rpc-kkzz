package com.kkzz.serializer;

public interface CommonSerializer {
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 1:
                return new JacksonSerializer();
            case 0:
                return new FastJsonSerializer();
            default:
                return null;
        }
    }
}
