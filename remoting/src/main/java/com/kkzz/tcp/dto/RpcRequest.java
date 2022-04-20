package com.kkzz.tcp.dto;

public class RpcRequest {
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
}
