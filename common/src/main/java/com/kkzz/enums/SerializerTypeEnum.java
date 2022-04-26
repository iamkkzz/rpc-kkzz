package com.kkzz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializerTypeEnum {
    FASTJSON((byte) 0x01, "fastjson"),
    PROTOBUF((byte) 0x02, "protobuf"),
    HESSIAN((byte) 0x03, "hessian");
    private final byte code;
    private final String name;
}
