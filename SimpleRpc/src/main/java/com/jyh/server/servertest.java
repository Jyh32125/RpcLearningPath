package com.jyh.server;

import com.jyh.server.service.RpcServer;

public class servertest {
    public static void main(String[] args) throws Exception {
        // 启动服务端
        new Thread(() -> {
            try {
                new RpcServer().start(8888);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}