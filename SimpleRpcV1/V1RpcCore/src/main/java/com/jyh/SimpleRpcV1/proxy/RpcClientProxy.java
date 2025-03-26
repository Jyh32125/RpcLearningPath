package com.jyh.SimpleRpcV1.proxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
// 客户端代理
public class RpcClientProxy {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass, String host, int port) {
        // 返回代理
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    // 定义代理方法，对client隐藏网络通信过程
                    try (Socket socket = new Socket(host, port);
                         // 用Java原生序列方法实现
                         ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                         ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                        // 发送方法信息，包含方法名称，参数类型，参数
                        output.writeUTF(method.getName());
                        output.writeObject(method.getParameterTypes());
                        output.writeObject(args);
                        output.flush();

                        // 返回结果
                        return input.readObject();
                    }
                });
    }
}