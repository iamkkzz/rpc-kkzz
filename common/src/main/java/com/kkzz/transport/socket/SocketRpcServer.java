package com.kkzz.transport.socket;

import com.kkzz.handler.RequestHandler;
import com.kkzz.registry.ServiceRegistry;
import com.kkzz.transport.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketRpcServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcServer.class);
    private  static final int CORE_POOL_SIZE=5;
    private  static final int MAXIMUM_POOL_SIZE=50;
    private  static final int KEEP_ALIVE_TIME=60;
    private  static final int BLOCKING_QUEUE_CAPACITY=100;
    private final ExecutorService threadPool;
    private RequestHandler requestHandler=new RequestHandler();
    private final ServiceRegistry serviceRegistry;
    public SocketRpcServer(ServiceRegistry registry) {
        this.serviceRegistry=registry;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, blockingQueue, Executors.defaultThreadFactory());
    }

    @Override
    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器启动中...");
            Socket  socket;
            while ((socket=serverSocket.accept())!=null){
                logger.info("客户端连接成功,ip为:"+socket.getInetAddress());
                threadPool.execute(new RequestHandlerRunnable(socket,requestHandler,serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("连接错误:", e);
        }
    }
}
