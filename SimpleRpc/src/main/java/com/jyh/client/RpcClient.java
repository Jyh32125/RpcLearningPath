package com.jyh.client;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class RpcClient {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass, String host, int port) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        try (Socket socket = new Socket(host, port);
                             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                            // 发送方法信息 [1,4](@ref)
                            output.writeUTF(method.getName());
                            output.writeObject(method.getParameterTypes());
                            output.writeObject(args);
                            output.flush();

                            return input.readObject();
                        }
                    }
                });
    }
}