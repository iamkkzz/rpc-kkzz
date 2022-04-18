package com.kkzz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RpcErrorMessageEnum {
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_BE_FOUND("没有找到指定的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务没有实现任何接口"),
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误！请求和返回的相应不匹配"),
    UNKNOWN_PACKAGE_TYPE("无法识别该数据包"),
    UNKNOWN_SERIALIZER("无法识别该序列化器"),
    UNKNOWN_PROTOCOL("不支持该协议包");
    private final String message;
}
