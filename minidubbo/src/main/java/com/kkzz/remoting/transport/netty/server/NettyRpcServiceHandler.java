package com.kkzz.remoting.transport.netty.server;

import com.kkzz.enums.CompressTypeEnum;
import com.kkzz.enums.RpcResponseCodeEnum;
import com.kkzz.enums.SerializerTypeEnum;
import com.kkzz.factory.SingletonFactory;
import com.kkzz.remoting.constants.RpcConstants;
import com.kkzz.remoting.dto.RpcMessage;
import com.kkzz.remoting.dto.RpcRequest;
import com.kkzz.remoting.dto.RpcResponse;
import com.kkzz.remoting.handler.RpcRequestHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRpcServiceHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private final RpcRequestHandler rpcRequestHandler;

    public NettyRpcServiceHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        log.info("服务器收到消息:[{}]", msg);
        byte messageType = msg.getMessageType();
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setCodec(SerializerTypeEnum.HESSIAN.getCode());
        rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            //收到心跳包,返回响应包
            rpcMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
            rpcMessage.setData(RpcConstants.PONG);
        } else {
            RpcRequest rpcRequest = (RpcRequest) msg.getData();
            Object result = rpcRequestHandler.handle(rpcRequest);
            log.info(String.format("服务器调用真正服务得到结果:%s", result.toString()));
            rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
            if (ctx.channel().isActive()&&ctx.channel().isWritable()){
                RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                rpcMessage.setData(rpcResponse);
            }else {
                RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCodeEnum.FAIL);
                rpcMessage.setData(rpcResponse);
                log.error("现在无法写入消息,丢弃消息");
            }
        }
        ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    //todo 新建心跳检测处理器
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("长时间未收到心跳包,关闭连接");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务器处理器捕获异常");
        cause.printStackTrace();
        ctx.close();
    }
}
