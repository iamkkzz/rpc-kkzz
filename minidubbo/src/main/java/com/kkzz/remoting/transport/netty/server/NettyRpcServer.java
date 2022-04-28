package com.kkzz.remoting.transport.netty.server;

import com.kkzz.config.CustomShutdownHook;
import com.kkzz.config.RpcServiceConfig;
import com.kkzz.factory.SingletonFactory;
import com.kkzz.provider.ServiceProvider;
import com.kkzz.provider.impl.NacosServiceProviderImpl;
import com.kkzz.remoting.transport.netty.codec.RpcMessageDecoder;
import com.kkzz.remoting.transport.netty.codec.RpcMessageEncoder;
import com.kkzz.threadPool.ThreadPoolFactoryUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyRpcServer {
    private final String host;
    private final int port;

    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(NacosServiceProviderImpl.class);

    public NettyRpcServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(new InetSocketAddress(host, port), rpcServiceConfig);
    }

    public void start() {
        CustomShutdownHook.getCustomShutdownHook().clearAll(new InetSocketAddress(host, port));
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                Runtime.getRuntime().availableProcessors() * 2,
                ThreadPoolFactoryUtil.createThreadFactory("server-handler-group", false));
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(serviceHandlerGroup, new NettyRpcServiceHandler());
                        }
                    });
            System.out.println("");
            ChannelFuture future = b.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("服务器启动时发生异常:", e);
        } finally {
            log.info("关闭bossGroup和workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }
    }
}
