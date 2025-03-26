package com.jyh.SimpleRpcV2.serialize;



import com.jyh.SimpleRpcV2.protocol.RpcProtocol;

import java.nio.ByteBuffer;

public class ProtocolSerializer {
    public static byte[] serializeProtocol(RpcProtocol protocol) {
        byte[] headerBytes = serializeHeader(protocol);
        byte[] bodyBytes = protocol.getRequestBody(); // 已序列化的RpcRequest
        return ByteBuffer.allocate(headerBytes.length + bodyBytes.length)
                .put(headerBytes)
                .put(bodyBytes)
                .array();
    }
    public static byte[] serializeHeader(RpcProtocol protocol) {
        ByteBuffer buffer = ByteBuffer.allocate(RpcProtocol.HEAD_LENGTH);
        buffer.putShort(protocol.getMagic());
        buffer.put(protocol.getVersion());
        buffer.put(protocol.getMsgType());
        buffer.putInt(protocol.getBodyLength());
        return buffer.array();
    }
}
