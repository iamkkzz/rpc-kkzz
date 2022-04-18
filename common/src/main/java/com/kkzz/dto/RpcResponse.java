package com.kkzz.dto;

import com.kkzz.enums.RpcResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 状态码短语
     */
    private String message;

    private T data;

    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail() {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(RpcResponseCodeEnum.FAIL.getCode());
        response.setMessage(RpcResponseCodeEnum.FAIL.getMessage());
        return response;
    }
}
