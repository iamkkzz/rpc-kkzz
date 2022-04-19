package com.kkzz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializerCode {
    FASTJSON(0),
    JACKSON(1);
    private final int code;
}
