package com.xs.netty.nio.netty001;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * @program: learn_root
 * @description: netty服务端
 * @author: xs-shuai.com
 * @create: 2020-06-13 00:12
 **/
public class NettyServer {
    public static void main(String[] args) throws Exception {

        //创建 BossGroup 和 WorkerGroup
        //说明
        //1. 创建两个线程组 bossGroup 和 workerGroup
        //2. bossGroup 只是处理连接请求 , 真正的和客户端业务处理，会交给 workerGroup 完成
        //3. 两个都是无限循环
        //4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
        // 默认实际 cpu 核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象(匿名对象)
            //给 pipeline 设置处理器
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new NettyServerHandler());
            }
        };

        try {
        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        //使用链式编程来进行设置
        
        bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                .channel(NioServerSocketChannel.class) //使用 NioSocketChannel 作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                .childHandler(channelInitializer); // 给我们的 workerGroup 的 EventLoop 对应的管道设置处理器
        System.out.println(".....服务器 is ready...");
        //绑定一个端口并且同步, 生成了一个 ChannelFuture 对象
        //启动服务器(并绑定端口)
        ChannelFuture cf = bootstrap.bind(6668).sync();
        //对关闭通道进行监听
        cf.channel().closeFuture().sync();
    }finally {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    }

}
