package com.kkzz.transport.socket;

import com.kkzz.dto.RpcResponse;
import com.kkzz.dto.RpcRequest;
import com.kkzz.handler.RequestHandler;
import com.kkzz.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestHandlerRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerRunnable.class);
    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;
    public RequestHandlerRunnable(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest request = (RpcRequest) in.readObject();
            String interfaceName = request.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result=requestHandler.handle(request,service);
            out.writeObject(RpcResponse.success(result));
            out.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用或发送时有错误发生:", e);
        }
    }
}
