# rpc-kkzz



## 项目背景及介绍

​	在之前的学习中，有接触到RPC的概念，对于RPC和OpenFeign的认识较为模糊。于是在学习完《Netty权威指南 第2版》后，打算动手造一个轮子，一方面是巩固Netty的相关知识，另一方面是为了更加深入对RPC的了解。

​	这个项目参考了[guide-rpc-framework](https://github.com/Snailclimb/guide-rpc-framework) 的RPC框架设计，在其设计的基础上，添加了自己对于部分组件的理解，如服务的注册和发现中心，由于此前对Nacos的了解更甚，故采用了Nacos作为服务的注册和发现中心。

## 组织结构

```
rpc-iamkkzz
├── common -- 工具类及通用代码
├── minidubbo -- RPC的服务器客户端以及网络传输部分
├── registry -- 服务注册和发现中心
├── test-api -- 公用API
├── test-client -- 测试服务调用端
└── test-server -- 测试服务提供端
```

## 所用技术

* **服务注册和发现中心**：

  ​	采用Nacos作为服务的注册和发现中心，服务提供端负责将服务信息和服务端开启的RPC-server的地址注册到Nacos中，服务调用端通过Nacos找到服务对应的地址，发起调用。

* **网络传输部分**:

  ​	网络传输部分使用了自定义的协议，使用高性能可靠的网络应用框架Netty来开发RPC的server和client端。

* **序列化部分**：

  ​	使用了fastjson和Protostuff来对需要进行传输的信息进行序列化和反序列化。

* **动态代理**：

  ​	动态代理技术是为了让服务调用端，调用远程服务就如同调用本地服务一样方便。采用JDK动态代理的技术，拦截方法，在方法调用前真正的通过发起网络请求，调用真正的服务。

* **SPI**：

  ​	参考了Dubbo中的SPI机制，让该框架的各个组件可以动态的进行配置。

## 运行项目

##### 导入项目

​	下载项目，使用集成编译器打开项目。

##### 下载并启动启动Nacos

​	该项目采用Nacos-1.4.2版本，以单机命令启动

`startup.cmd	-m	standalone`

##### 分别启动test-server和test-client即可

