package com.xs.netty.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: learn_root
 * @description: MappedByteBuffer
 * @author: xs-shuai.com
 * @create: 2020-06-07 23:57
 **/
public class NioChannel005 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\file01.txt", "rw");
//获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();
/**
 * 参数 1: FileChannel.MapMode.READ_WRITE 使用的读写模式
 * 参数 2： 0 ： 可以直接修改的起始位置
 * 参数 3: 5: 是映射到内存的大小(不是索引位置) ,即将 1.txt 的多少个字节映射到内存
 * 可以直接修改的范围就是 0-5
 * 实际类型 DirectByteBuffer
 */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
//        mappedByteBuffer.put(5, (byte) 'Y');//IndexOutOfBoundsException
        randomAccessFile.close();
        System.out.println("修改成功~~");

    }
}
