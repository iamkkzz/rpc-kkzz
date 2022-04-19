package com.kkzz.transport.netty;

import com.alibaba.fastjson.JSON;
import com.kkzz.hook.ShutDownHook;
import com.kkzz.provider.DefaultServiceProvider;
import com.kkzz.provider.ServiceProvider;
import com.kkzz.registry.NacosServiceRegistry;
import com.kkzz.registry.ServiceRegistry;
import com.kkzz.serializer.CommonSerializer;
import com.kkzz.serializer.FastJsonSerializer;
import com.kkzz.transport.AbstractRpcServer;
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

public class NettyRpcServer extends AbstractRpcServer {

    private final CommonSerializer serializer;

    public NettyRpcServer(String host, Integer port) {
        this(host, port, 0);
    }

    public NettyRpcServer(String host, Integer port, Integer serializer) {
        this.port = port;
        this.host = host;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new DefaultServiceProvider();
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices();
    }

    @Override
    public void start() {
        ShutDownHook.getShutDownHook().addClearAllHook();
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
                            ch.pipeline().addLast(new CommonEncoder(serializer));
                            ch.pipeline().addLast(new NettyServerHandler(serviceProvider));
                        }
                    });
            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
