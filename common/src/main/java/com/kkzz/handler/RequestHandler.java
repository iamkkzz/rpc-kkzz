package com.kkzz.handler;

import com.kkzz.dto.RpcResponse;
import com.kkzz.dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Object handle(RpcRequest request, Object service) {
        Object result = null;
        try {
            result = invokeTargetMethod(request, service);
            logger.info("服务:{} 成功调用方法:{}",request.getMethodName(),request.getMethodName());
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.error("调用或发送时有错误发生:", e);
        }
        return result;
    }

    private Object invokeTargetMethod(RpcRequest request, Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try {
            method = service.getClass().getMethod(request.getMethodName(),request.getParamTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail();
        }
        return method.invoke(service, request.getParameters());
    }
}
