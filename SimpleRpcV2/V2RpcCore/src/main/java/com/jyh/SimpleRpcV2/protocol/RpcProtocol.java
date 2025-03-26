package com.jyh.SimpleRpcV2.protocol;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class RpcProtocol {
    // 消息头部
    public final static int HEAD_LENGTH = 8;
    private short magic = (short) 0xCAFE; // 魔数校验
    private byte version; // 版本号
    private byte msgType; // 0-request 1-response
    private int bodyLength; // 数据长度
    private byte[] requestBody; // 实际数据

    public RpcProtocol(byte msgType, int bodyLength, byte[] data) {
        this.msgType = msgType;
        this.bodyLength = bodyLength;
        this.requestBody = data;
    }

}
