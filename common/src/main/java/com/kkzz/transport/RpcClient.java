package com.kkzz.transport;

import com.kkzz.dto.RpcRequest;

public interface RpcClient {
    public Object sendRequest(RpcRequest rpcRequest);
}
