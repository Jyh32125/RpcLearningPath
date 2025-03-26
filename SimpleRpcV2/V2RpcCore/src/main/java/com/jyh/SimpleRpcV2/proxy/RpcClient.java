package com.jyh.SimpleRpcV2.proxy;



import com.jyh.SimpleRpcV2.protocol.RpcProtocol;
import com.jyh.SimpleRpcV2.protocol.RpcRequest;
import com.jyh.SimpleRpcV2.protocol.RpcResponse;
import com.jyh.SimpleRpcV2.serialize.JsonSerializer;
import com.jyh.SimpleRpcV2.serialize.ProtocolSerializer;
import com.jyh.SimpleRpcV2.serialize.Serializer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class RpcClient {
    private final String host;
    private final int port;
    // 之所以是成员变量是在后面判定响应是否合法的函数也会用到
    private RpcProtocol rpcProtocol;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RpcResponse sendRequest(RpcRequest request) {
        // 封装的JSON序列化方法，实现了Serializer接口为了以后方便拓展序列化协议
        Serializer serializer = new JsonSerializer();
        // 将请求体序列化到byte数组中，消息头的序列化在后面
        byte[] requestBody = serializer.serialize(request);
        // 将序列化好之后的请求体封装到protocol里
        rpcProtocol = new RpcProtocol(
                (byte) 0,
                requestBody.length,
                requestBody
        );

        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {

            // 之所以要分开序列化消息头和消息体，是因为在消息头里需要记录序列化好之后的消息体长度，所以要分开序列化，这一块序列化的操作可以更内聚，分开写是为了逻辑更清晰
            byte[] protocolData = ProtocolSerializer.serializeProtocol(rpcProtocol);

            // 发送协议头 + 数据体
            outputStream.write(protocolData);
            outputStream.flush();

            // 根据消息头长度读取响应消息头
            byte[] responseHeader = readFullBytes(inputStream, RpcProtocol.HEAD_LENGTH);
            ByteBuffer headerBuffer = ByteBuffer.wrap(responseHeader);
            // 从消息头读取消息体长度
            int bodyLength = validateResponse(headerBuffer);

            // 读取响应数据体
            byte[] responseBody = readFullBytes(inputStream, bodyLength);
            // 将消息体反序列化到RpcResponse中
            return serializer.deserialize(responseBody, RpcResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new RpcResponse("通信失败");
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

    private int validateResponse(ByteBuffer headerBuffer) {
        short magic = headerBuffer.getShort();
        byte version = headerBuffer.get();
        byte msgType = headerBuffer.get();
        int bodyLength = headerBuffer.getInt();
        if((magic == rpcProtocol.getMagic()) && (version == rpcProtocol.getVersion()) && (msgType == 1)){
            return bodyLength;
        }else {
            return -1;
        }
    }


}