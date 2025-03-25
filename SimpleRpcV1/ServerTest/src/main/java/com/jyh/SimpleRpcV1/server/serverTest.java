package com.jyh.SimpleRpcV1.server;


public class serverTest {
    public static void main(String[] args) throws Exception {
        // 启动服务端
        new Thread(() -> {
            try {
                // 开启一个线程去监听8888端口
                new localService().start(8888);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}