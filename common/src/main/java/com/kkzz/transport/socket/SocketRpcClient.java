package com.kkzz.transport.socket;

import com.kkzz.dto.RpcRequest;
import com.kkzz.transport.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketRpcClient implements RpcClient{
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
    public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(rpcRequest);
            out.flush();
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用错误:", e);
            return null;
        }
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket("127.0.0.1", 9090)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(rpcRequest);
            out.flush();
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用错误:", e);
            return null;
        }
    }
}
