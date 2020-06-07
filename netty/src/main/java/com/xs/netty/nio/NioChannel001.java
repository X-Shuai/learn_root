package com.xs.netty.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: learn_root
 * @description: channel学习 写入数据
 * @author: xs-shuai.com
 * @create: 2020-06-07 11:56
 **/
public class NioChannel001 {
    public static void main(String[] args) throws Exception {


        String str="hello,xs-shuai";
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");
        //获得通道
        FileChannel channel = fileOutputStream.getChannel();
        //创建缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        //装入数据
        allocate.put(str.getBytes());

        allocate.flip();
//        将 byteBuffer 数据写入到 fileChannel
        channel.write(allocate);
        fileOutputStream.close();
    }
}
