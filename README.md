## 首先是RPC的引出

​	之前在一个使用springcloud开发的微服务项目中,发现微服务之间的调用是使用openfeign实现的,存在这么一个场景:

> ​	因为微服务各个模块之间的功能实现是独立的,当order模块下,需要使用到库存数据时,我们就需要调用ware模块中的接口去获取库存信息返回给order模块

order模块中:

```java
@FeignClient("mall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> ids);

    @GetMapping("/ware/wareinfo/fare")
    R getFare(@RequestParam("addrId") Long addrId);

    @PostMapping("/ware/waresku/lock/order")
    R lockStock(@RequestBody WareStockLockVo vo);
}
```

这里的order模块就相当于是客户端,定义了和库存模块中一样的接口名,通过动态代理,生成了http请求,然后将请求发给了库存服务,库存服务收到请求,就会调用相应的处理器去处理,并返回结果

## 初步实现

通过学习,我们发现服务之间的调用需要从以下几步入手

* 服务的发现和服务的暴露
* Java对象的序列化
* 网络中的字节流到Java中可用的字节流(解决半包粘包问题)

大概逻辑是,服务端启动,进入监听状态,如果有客户端接入,就处理客户端接入,并根据请求调用相应的具体的实现

客户端启动,将需要调用的服务的接口传入,通过代理对象构造请求发送

