package com.jyh.server.service;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class RpcServer {
    public void start(int port) throws Exception {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("服务端启动，监听端口：" + port);
            while (true) {
                Socket socket = server.accept();
                new Thread(() -> handleRequest(socket)).start();
            }
        }
    }

    private void handleRequest(Socket socket) {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            // 读取方法名、参数类型和值 [1,4](@ref)
            String methodName = input.readUTF();
            Class<?>[] paramTypes = (Class<?>[]) input.readObject();
            Object[] args = (Object[]) input.readObject();

            // 反射调用方法
            CalculatorService service = new CalculatorServiceImpl();
            Method method = service.getClass().getMethod(methodName, paramTypes);
            Object result = method.invoke(service, args);

            // 返回结果
            output.writeObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 服务接口实现类
    static class CalculatorServiceImpl implements CalculatorService {
        @Override
        public int add(int a, int b) { return a + b; }
        @Override
        public int subtract(int a, int b) { return a - b; }
    }
}