package com.xs.netty.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: learn_root
 * @description: bio
 * @author: xs-shuai.com
 * @create: 2020-06-01 00:22
 **/
public class BIOServer {

    public static void main(String[] args) throws Exception {

//思路
//1. 创建一个线程池
//2. 如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)
        ExecutorService newCachedThreadPool;
        newCachedThreadPool = Executors.newCachedThreadPool();
//创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true) {
            System.out.println(" 线 程 信 息 id =" + Thread.currentThread().getId() + " 名 字 =" +
                    Thread.currentThread().getName());
//监听，等待客户端连接
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
//就创建一个线程，与之通讯(单独写一个方法)
            newCachedThreadPool.execute(new Runnable() {
                public void run() { //我们重写
//可以和客户端通讯
                    handler(socket);
                }

            });
        }
    }

    //编写一个 handler 方法，和客户端通讯
    public static void handler(Socket socket) {
        try {
            System.out.println(" 线 程 信 息 id =" + Thread.currentThread().getId() + " 名 字 =" +
                    Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
//通过 socket 获取输入流
            InputStream inputStream = socket.getInputStream();
//循环的读取客户端发送的数据
            while (true) {
                System.out.println(" 线 程 信 息 id =" + Thread.currentThread().getId() + " 名 字 =" +
                        Thread.currentThread().getName());
                System.out.println("read....");
                int read = inputStream.read(bytes);
                if (read != -1) {

                    System.out.println(new String(bytes, 0, read
                    )); //输出客户端发送的数据
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和 client 的连接");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
