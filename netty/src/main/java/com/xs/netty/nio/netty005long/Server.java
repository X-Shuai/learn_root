package com.xs.netty.nio.netty005long;

import com.xs.netty.nio.netty004heart.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @program: learn_root
 * @description: 服务器
 * @author: xs-shuai.com
 * @create: 2020-06-21 15:07
 **/
public class Server {
    public static void main(String[] args) {


        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                //基于http协议 使用http的编码器和解码器
                pipeline.addLast(new HttpServerCodec());
                //以块的方式写入, 添加ChunkedWriteHandler 处理器
                pipeline.addLast(new ChunkedWriteHandler());


                /***
                 * http数据传输过程中是分段 HttpObjectAggregator 可以将多个分段聚合
                 *
                 * 就是多个浏览器传输大数据时,会发起多个请求
                 */
                pipeline.addLast(new HttpObjectAggregator(8192));


                /***
                 * websocket的数据以帧的方式进行请求
                 * 可以看到websocketFrame
                 */
                pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                pipeline.addLast(new MyHandler());

            }
        };
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(channelInitializer);
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
