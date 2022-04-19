package com.kkzz.transport.netty;

import com.kkzz.dto.RpcRequest;
import com.kkzz.dto.RpcResponse;
import com.kkzz.serializer.FastJsonSerializer;
import com.kkzz.serializer.JacksonSerializer;
import com.kkzz.transport.RpcClient;
import com.kkzz.transport.netty.codec.CommonDecoder;
import com.kkzz.transport.netty.codec.CommonEncoder;
import com.kkzz.transport.netty.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class NettyRpcClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);

    private String host;
    private int port;
    private static final Bootstrap b;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new CommonDecoder());
                        ch.pipeline().addLast(new CommonEncoder(new FastJsonSerializer()));
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture future = b.connect(host, port).sync();
            logger.info("客户端连接到服务器{}:{}", host, port);
            Channel channel = future.channel();
            if (channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        logger.info("客户端发送消息" + rpcRequest.toString());
                    } else {
                        logger.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
