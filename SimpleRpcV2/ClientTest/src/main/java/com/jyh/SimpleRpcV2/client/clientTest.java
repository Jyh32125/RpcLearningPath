package com.jyh.SimpleRpcV2.client;

import com.jyh.SimpleRpcV2.api.CalculatorService;
import com.jyh.SimpleRpcV2.proxy.RpcClientProxy;

public class clientTest {
    public static void main(String[] args){

        // 创建客户端代理
        CalculatorService calculator = RpcClientProxy.getProxy(CalculatorService.class, "localhost", 8888);

        // 调用远程方法
        System.out.println("3 + 5 = " + calculator.add(3, 5));  // 输出8
        System.out.println("10 - 4 = " + calculator.subtract(10, 4));  // 输出6
    }
}