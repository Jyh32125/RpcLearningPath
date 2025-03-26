package com.jyh.SimpleRpcV2.proxy;

import com.jyh.SimpleRpcV2.protocol.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.UUID;

// 客户端代理
public class RpcClientProxy {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass, String host, int port) {
        // 返回代理
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    // 封装请求体
                    RpcRequest request = new RpcRequest(
                            // 请求id为 请求时间 + UUID
                            LocalDateTime.now() + UUID.randomUUID().toString(),
                            method.getDeclaringClass().getName(),
                            method.getName(),
                            method.getParameterTypes(),
                            args
                    );
                    // 在这里将代理逻辑分为两部分，避免代码太复杂，第一部分就是现在这个类，用于封装请求体，RpcClient用于封装请求头并进行通信
                    RpcClient rpcClient = new RpcClient(host, port);
                    // 从RpcResponse中获取响应的数据并返回
                    return rpcClient.sendRequest(request).getData();
                });
    }
}