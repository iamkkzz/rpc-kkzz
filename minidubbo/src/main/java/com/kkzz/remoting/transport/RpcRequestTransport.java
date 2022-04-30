package com.kkzz.remoting.transport;


import com.kkzz.extension.Spi;
import com.kkzz.remoting.dto.RpcRequest;

@Spi
public interface RpcRequestTransport {
    Object sendRequest(RpcRequest request);
}
