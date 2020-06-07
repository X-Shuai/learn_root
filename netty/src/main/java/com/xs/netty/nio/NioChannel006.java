package com.xs.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @program: learn_root
 * @description: 多个buffer
 * @author: xs-shuai.com
 * @create: 2020-06-08 00:12
 **/
public class NioChannel006 {
    public static void main(String[] args) throws Exception {


        //使用 ServerSocketChannel 和 SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到 socket ，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建 buffer 数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        //等客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8; //假定从客户端接收 8 个字节
        //循环的读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                //累计读取的字节数
                byteRead += l;
                System.out.println("byteRead=" + byteRead);
                //使用流打印, 看看当前的这个 buffer 的 position 和 limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "postion=" + buffer.position() + ", limit=" +
                        buffer.limit()).forEach(System.out::println);
            }
            //将所有的 buffer 进行 flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            //将数据读出显示到客户端
            long byteWirte = 0;
            while (byteWirte < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWirte += l;

                //将所有的 buffer 进行 clear
                Arrays.asList(byteBuffers).forEach(buffer -> {
                    buffer.clear();
                });
                System.out.println("byteRead:=" + byteRead + " byteWrite=" + byteWirte + ", messagelength" +
                        messageLength);
            }
        }

    }
}
