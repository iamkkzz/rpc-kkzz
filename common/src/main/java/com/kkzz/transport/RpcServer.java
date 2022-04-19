package com.kkzz.transport;

public interface RpcServer {
    public void start();

    public <T>  void publishService(Object service,Class<T> serviceClass);
}
