package com.SimpleRpcV2.server.service;

import com.jyh.SimpleRpcV2.api.CalculatorService;

public class CalculatorServiceImpl implements CalculatorService {
        @Override
        public int add(int a, int b) { return a + b; }
        @Override
        public int subtract(int a, int b) { return a - b; }
}