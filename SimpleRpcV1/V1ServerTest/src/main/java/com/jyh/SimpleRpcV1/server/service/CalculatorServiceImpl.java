package com.jyh.SimpleRpcV1.server.service;

import com.jyh.SimpleRpcV1.api.CalculatorService;
// 定义在api包里的服务接口实现类
public class CalculatorServiceImpl implements CalculatorService {
    @Override
    public int add(int a, int b) { return a + b; }
    @Override
    public int subtract(int a, int b) { return a - b; }
}
