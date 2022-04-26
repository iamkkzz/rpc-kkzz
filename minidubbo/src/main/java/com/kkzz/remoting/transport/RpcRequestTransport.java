package com.kkzz.remoting.transport;


import com.kkzz.remoting.dto.RpcRequest;

public interface RpcRequestTransport {
    Object sendRequest(RpcRequest request);
}
