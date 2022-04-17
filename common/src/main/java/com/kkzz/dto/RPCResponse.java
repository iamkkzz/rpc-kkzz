package com.kkzz.dto;

import com.kkzz.RpcResponseCodeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class RPCResponse<T> implements Serializable {
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 状态码短语
     */
    private String message;

    private T data;

    public static <T> RPCResponse<T> success(T data) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setStatusCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> RPCResponse<T> fail() {
        RPCResponse<T> response = new RPCResponse<>();
        response.setStatusCode(RpcResponseCodeEnum.FAIL.getCode());
        response.setMessage(RpcResponseCodeEnum.FAIL.getMessage());
        return response;
    }
}
