package com.xs.netty.nio.netty003group;

import com.xs.netty.nio.netty001.NettyServerHandler;
import com.xs.netty.nio.netty002.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @program: learn_root
 * @description: 群聊服务器
 * @author: xs-shuai.com
 * @create: 2020-06-17 21:55
 **/
public class GroupServer {
    /**监听端口
     *
     */
    private int port;

    public GroupServer(int port) {
        this.port = port;
    }

    ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
        //给 pipeline 设置处理器
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            //编码器
            pipeline.addLast("decoder",new StringDecoder());
            //解码器
            pipeline.addLast("encoder",new StringEncoder());
            //自己的业务类
            pipeline.addLast(null);

        }
    };


    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {



            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioSctpServerChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(channelInitializer);

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }



    public static void main(String[] args) throws InterruptedException {

        GroupServer groupServer = new GroupServer(4869);
        groupServer.run();
    }
}
