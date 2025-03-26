package com.jyh.SimpleRpcV2.serialize;


import com.jyh.SimpleRpcV2.exception.SerializationException;

public interface Serializer {



    // 序列化对象为字节数组
    <T> byte[] serialize(T object) throws SerializationException;

    // 反序列化指定类型的对象
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException;
}
