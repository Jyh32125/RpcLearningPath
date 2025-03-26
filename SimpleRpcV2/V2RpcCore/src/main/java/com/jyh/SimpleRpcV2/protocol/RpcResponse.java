package com.jyh.SimpleRpcV2.protocol;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RpcResponse implements Serializable {
    // 请求唯一标识
    private String requestId;

    // 响应数据
    private Object data;

    // 错误信息
    private String error;

    public RpcResponse(
            @JsonProperty("requestId") String requestId,
            @JsonProperty("data") Object data,
            @JsonProperty("error") String error) {
        this.requestId = requestId;
        this.data = data;
        this.error = error;
    }

    public RpcResponse(String error){
        this.error = error;
    }

}
