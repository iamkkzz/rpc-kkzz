package com.kkzz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RpcErrorMessageEnum {
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端异常"),
    REQUEST_NOT_MATCH("请求与响应不匹配"),
    SERVICE_REGISTER_FAILURE("服务注册失败"),
    SERVICE_CAN_NOT_BE_FOUND("没有找到指定的服务"),
    SERVICE_INVOCATION_FAILURE("服务调用失败");
    private final String message;
}
