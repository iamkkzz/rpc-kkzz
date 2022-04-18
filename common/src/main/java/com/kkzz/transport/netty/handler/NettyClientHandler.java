package com.kkzz.transport.netty.handler;

import com.kkzz.dto.RpcResponse;
import com.kkzz.transport.netty.NettyRpcClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        try {
            logger.info("客户端收到消息" + msg);
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            ctx.channel().attr(key).set(msg);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("过程调用时有错误发生:", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
