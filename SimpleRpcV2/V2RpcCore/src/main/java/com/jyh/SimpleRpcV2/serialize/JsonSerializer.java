package com.jyh.SimpleRpcV2.serialize;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jyh.SimpleRpcV2.exception.SerializationException;

import java.io.IOException;

public class JsonSerializer implements Serializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // 禁用无关特性，提升性能
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
    }

    @Override
    public byte getProtocolId() {
        return 1; // JSON协议标识
    }

    @Override
    public <T> byte[] serialize(T object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new SerializationException("JSON序列化失败");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new SerializationException("JSON反序列化失败");
        }
    }
}
