package com.jyh.SimpleRpcV2.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RpcRequest implements Serializable {
    private String requestId; // 请求id
    private String interfaceName; // 接口名称
    private String methodName; // 方法名称
    private Class<?>[] parameterTypes; //数据类型
    private Object[] data; // 实际数据


    @JsonCreator
    public RpcRequest(
            @JsonProperty("requestId") String requestId,
            @JsonProperty("interfaceName") String interfaceName,
            @JsonProperty("methodName") String methodName,
            @JsonProperty("parameterTypes") Class<?>[] parameterTypes,
            @JsonProperty("data") Object[] data
    ) {
        this.requestId = requestId;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.data = data;
    }
//    public RpcRequest(String requestId, String interfaceName, String methodName, Class<?>[] parameterTypes, Object[] data) {
//        this.requestId = requestId;
//        this.interfaceName = interfaceName;
//        this.methodName = methodName;
//        this.parameterTypes = parameterTypes;
//        this.data = data;
//    }

    public RpcRequest() {}
}