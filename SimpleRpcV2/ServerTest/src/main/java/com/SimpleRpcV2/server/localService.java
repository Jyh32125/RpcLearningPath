package com.SimpleRpcV2.server;



import com.SimpleRpcV2.server.service.CalculatorServiceImpl;
import com.jyh.SimpleRpcV2.api.CalculatorService;
import com.jyh.SimpleRpcV2.protocol.RpcProtocol;
import com.jyh.SimpleRpcV2.protocol.RpcRequest;
import com.jyh.SimpleRpcV2.protocol.RpcResponse;
import com.jyh.SimpleRpcV2.serialize.JsonSerializer;
import com.jyh.SimpleRpcV2.serialize.ProtocolSerializer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
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
        try (
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream()
        ) {
            // 读取请求协议头
            byte[] headerBytes = readFullBytes(inputStream, RpcProtocol.HEAD_LENGTH);
            ByteBuffer headerBuffer = ByteBuffer.wrap(headerBytes);
            // 读取消息头参数
            short magic = headerBuffer.getShort();
            byte version = headerBuffer.get();
            byte msgtype = headerBuffer.get();
            int bodyLength = headerBuffer.getInt();

            // 校验魔数和版本
            if (magic != (short) 0xCAFE || version > 1 || msgtype != 0) {
                throw new IllegalArgumentException("非法协议");
            }

            // 读取请求数据体
            byte[] bodyBytes = readFullBytes(inputStream, bodyLength);
            // 反序列化请求体
            RpcRequest request = new JsonSerializer().deserialize(bodyBytes, RpcRequest.class);
            String methodName = request.getMethodName();
            Class<?>[] paramTypes = request.getParameterTypes();
            Object[] args = request.getData();

            // 反射调用方法并生成响应
            CalculatorService service = new CalculatorServiceImpl();
            Method method = service.getClass().getMethod(methodName, paramTypes);
            Object result = method.invoke(service, args);
            RpcResponse rpcResponse = new RpcResponse(request.getRequestId(), result, null);

            // 发送响应
            byte[] responseBody = new JsonSerializer().serialize(rpcResponse);
            // 封装消息头
            RpcProtocol responseProtocol = new RpcProtocol(
                    (byte) 1,
                    responseBody.length,
                    responseBody
            );
            // 序列化消息头并发送
            byte[] protocolData = ProtocolSerializer.serializeProtocol(responseProtocol);
            outputStream.write(protocolData);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] readFullBytes(InputStream input, int length) throws IOException {
        byte[] data = new byte[length];
        int bytesRead = 0;
        while (bytesRead < length) {
            int read = input.read(data, bytesRead, length - bytesRead);
            if (read == -1) {
                throw new EOFException("未读取到足够数据");
            }
            bytesRead += read;
        }
        return data;
    }

}