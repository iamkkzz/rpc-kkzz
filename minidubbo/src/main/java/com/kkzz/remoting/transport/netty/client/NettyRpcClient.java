package com.kkzz.remoting.transport.netty.client;

import com.kkzz.ServiceDiscovery;
import com.kkzz.ServiceRegistry;
import com.kkzz.enums.CompressTypeEnum;
import com.kkzz.enums.SerializerTypeEnum;
import com.kkzz.extension.ExtensionLoader;
import com.kkzz.factory.SingletonFactory;
import com.kkzz.remoting.constants.RpcConstants;
import com.kkzz.remoting.dto.RpcMessage;
import com.kkzz.remoting.dto.RpcRequest;
import com.kkzz.remoting.dto.RpcResponse;
import com.kkzz.remoting.transport.RpcRequestTransport;
import com.kkzz.remoting.transport.netty.codec.RpcMessageDecoder;
import com.kkzz.remoting.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyRpcClient implements RpcRequestTransport {
    private final UnprocessedRequests unprocessedRequests;
    private final ServiceDiscovery serviceDiscovery;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final ChannelProvider channelProvider;

    public NettyRpcClient() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        pipeline.addLast(new RpcMessageEncoder());
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });

        //todo
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("nacos");
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param request
     * @return
     */
    @Override
    public Object sendRequest(RpcRequest request) {
        CompletableFuture<RpcResponse<Object>> resFuture = new CompletableFuture<>();
        //??????RpcServiceName???????????????ip??????
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(request.getRpcServiceName());
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            unprocessedRequests.put(request.getRequestId(), resFuture);
            RpcMessage rpcMessage = RpcMessage.builder().data(request)
                    .codec(SerializerTypeEnum.HESSIAN.getCode())
                    .compress(CompressTypeEnum.GZIP.getCode())
                    .messageType(RpcConstants.REQUEST_TYPE).build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("???????????????????????????:[{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resFuture.completeExceptionally(future.cause());
                    log.error("???????????????????????????", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    /**
     * ??????????????????,??????????????????,??????????????????
     *
     * @param inetSocketAddress
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @SneakyThrows
    private Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("????????????????????????????????? [{}]", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }
}
