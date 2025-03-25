package com.jyh.SimpleRpcV1.client;

import com.jyh.SimpleRpcV1.api.CalculatorService;
import com.jyh.SimpleRpcV1.proxy.RpcClientProxy;
// 启动客户端
public class clientTest {
    public static void main(String[] args) throws Exception {

        // 创建客户端代理
        CalculatorService calculator = RpcClientProxy.getProxy(CalculatorService.class, "localhost", 8888);

        // 调用远程方法
        System.out.println("3 + 5 = " + calculator.add(3, 5));  // 输出8
        System.out.println("10 - 4 = " + calculator.subtract(10, 4));  // 输出6
    }
}