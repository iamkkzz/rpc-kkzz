package com.kkzz.transport;

public interface RpcServer {
    public void start();

    public <T>  void publishService(T service,String serviceName);
}
