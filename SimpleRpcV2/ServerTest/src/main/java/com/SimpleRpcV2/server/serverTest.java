package com.SimpleRpcV2.server;

public class serverTest {
    public static void main(String[] args) {
        // 启动服务端
        new Thread(() -> {
            try {
                new localService().start(8888);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}