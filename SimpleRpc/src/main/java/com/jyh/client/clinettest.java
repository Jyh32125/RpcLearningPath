package com.jyh.client;

import com.jyh.server.service.CalculatorService;

public class clinettest {
    public static void main(String[] args) throws Exception {

        // 创建客户端代理
        CalculatorService calculator = RpcClient.getProxy(CalculatorService.class, "localhost", 8888);

        // 调用远程方法
        System.out.println("3 + 5 = " + calculator.add(3, 5));  // 输出8
        System.out.println("10 - 4 = " + calculator.subtract(10, 4));  // 输出6
    }
}