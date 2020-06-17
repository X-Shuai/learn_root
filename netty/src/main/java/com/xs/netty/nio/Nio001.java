package com.xs.netty.nio;

import java.nio.IntBuffer;

/**
 * @program: learn_root
 * @description: netty学习1
 * @author: xs-shuai.com
 * @create: 2020-06-06 23:07
 **/
public class Nio001 {
    public static void main(String[] args) {

        IntBuffer allocate = IntBuffer.allocate(5);
        for(int i = 0; i < allocate.capacity(); i++) {
            allocate.put( i * 2);
        }
        //如何从 buffer 读取数据
        //将 buffer 转换，读写切换(!!!)
        allocate.flip();

        while (allocate.hasRemaining()) {
            System.out.println(allocate.get());
        }

    }
}
