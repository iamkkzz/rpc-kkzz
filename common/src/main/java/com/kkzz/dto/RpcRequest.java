package com.kkzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
