package com.xs.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: learn_root
 * @description: 文件拷贝
 * @author: xs-shuai.com
 * @create: 2020-06-07 20:45
 **/

public class NioChannel003 {
    public static void main(String[] args) throws Exception {
        //获得文件及通道
        FileInputStream fileInputStream = new FileInputStream("d:\\file01.txt");
        FileChannel fileChannelRead = fileInputStream.getChannel();
        //文件输出及通道
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file02.txt");
        FileChannel fileChannelWrite = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        //循环读取
        while (true) {

            byteBuffer.clear(); //清空 buffer
            int read = fileChannelRead.read(byteBuffer);
            System.out.println("read =" + read);
            //表示读完
            if (read == -1) {
                break;
            }
            //将 buffer 中的数据写入到 fileChannelWrite -- 2.txt
            byteBuffer.flip();
            fileChannelWrite.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();

    }
}
