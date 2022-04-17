package com.kkzz.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable {
    /**
     * 接口名
     */
    private String interfaceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法中的参数
     */
    private Object[] parameters;
    /**
     * 方法中的参数的类型
     */
    private Class<?>[] paramTypes;
}
