package com.xs.netty.nio.netty004heart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-06-19 00:55
 **/
public class Server {

    /***
     * 1.编写一个 Netty 心跳检测机制案例, 当服务器超过 3 秒没有读时，就提示读空闲
     * 2.当服务器超过 5 秒没有写操作时，就提示写空闲
     * 3.实现当服务器超过 7 秒没有读或者写操作时，就提示读写空闲
     */

    public static void main(String[] args) {


        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
//加入一个 netty 提供 IdleStateHandler
/*
说明
1. IdleStateHandler 是 netty 提供的处理空闲状态的处理器
2. long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接
3. long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
4. long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接
5. 文档说明


                    triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                            * read, write, or both operation for a while.
* 6. 当 IdleStateEvent 触发后 , 就会传递给管道 的下一个 handler 去处理
                            * 通过调用(触发)下一个 handler 的 userEventTiggered , 在该方法中去处理 IdleStateEvent(读
                            空闲，写空闲，读写空闲)
                            */
                    pipeline.addLast(new IdleStateHandler(2, 3, 5, TimeUnit.SECONDS));
                    //加入一个对空闲检测进一步处理的 handler(自定义)
                    pipeline.addLast(new ServerHandler());

                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
            channelFuture.channel().closeFuture().sync();
        }
        catch (Exception e){
            e.printStackTrace();
        }

      finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }


}
