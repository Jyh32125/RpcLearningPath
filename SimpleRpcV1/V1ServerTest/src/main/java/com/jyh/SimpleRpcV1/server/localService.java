package com.jyh.SimpleRpcV1.server;



import com.jyh.SimpleRpcV1.api.CalculatorService;
import com.jyh.SimpleRpcV1.server.service.CalculatorServiceImpl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class localService {
    public void start(int port) throws Exception {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("服务端启动，监听端口：" + port);
            while (true) {
                Socket socket = server.accept();
                // 监听到连接就开始处理请求消息
                new Thread(() -> handleRequest(socket)).start();
            }
        }
    }

    private void handleRequest(Socket socket) {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            // 读取方法名、参数类型和值
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
}