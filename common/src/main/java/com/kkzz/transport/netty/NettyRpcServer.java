package com.kkzz.transport.netty;

import com.kkzz.hook.ShutDownHook;
import com.kkzz.provider.DefaultServiceProvider;
import com.kkzz.provider.ServiceProvider;
import com.kkzz.registry.NacosServiceRegistry;
import com.kkzz.registry.ServiceRegistry;
import com.kkzz.serializer.FastJsonSerializer;
import com.kkzz.transport.RpcServer;
import com.kkzz.transport.netty.codec.CommonDecoder;
import com.kkzz.transport.netty.codec.CommonEncoder;
import com.kkzz.transport.netty.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class NettyRpcServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);
    private String host;
    private Integer port;
    private ServiceProvider serviceProvider;
    private ServiceRegistry serviceRegistry;

    public NettyRpcServer(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.serviceProvider = new DefaultServiceProvider();
        this.serviceRegistry = new NacosServiceRegistry();
    }

    @Override
    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new CommonDecoder());
                            ch.pipeline().addLast(new CommonEncoder(new FastJsonSerializer()));
                            ch.pipeline().addLast(new NettyServerHandler(serviceProvider));
                        }
                    });
            ChannelFuture future = b.bind(port).sync();
            ShutDownHook.getShutDownHook().addClearAllHook();
            future.channel().closeFuture().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
    }
}
