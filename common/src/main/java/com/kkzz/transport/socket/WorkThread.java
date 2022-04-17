package com.kkzz.transport.socket;

import com.kkzz.dto.RPCResponse;
import com.kkzz.dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class WorkThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(WorkThread.class);
    private Socket socket;
    private Object service;

    public WorkThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest request = (RpcRequest) in.readObject();
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            Object returnObj = method.invoke(service, request.getParameters());
            out.writeObject(RPCResponse.success(returnObj));
            out.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error("调用或发送时有错误发生:", e);
        }
    }
}
