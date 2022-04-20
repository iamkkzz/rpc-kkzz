package com.kkzz;

import com.kkzz.tcp.dto.RpcRequest;

public interface RpcRequestTransport {
    Object sendRequest(RpcRequest request);
}
