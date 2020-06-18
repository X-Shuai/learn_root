package com.xs.netty.nio.netty003group;

import com.sun.prism.shader.AlphaOne_Color_AlphaTest_Loader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @program: learn_root
 * @description: 客户端
 * @author: xs-shuai.com
 * @create: 2020-06-19 00:15
 **/

public class GroupClient {

    private final String host;
    private final int port;

    public GroupClient(String host, int port) {
        this.host = host;
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
            pipeline.addLast("my",new ClientHandler());

        }
    };

    public void run(){

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap =new Bootstrap();


        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(channelInitializer);

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();

            System.out.println("--------------"+channel.localAddress()+"-------------");
            Scanner scanner = new Scanner(System.in);
            while(true){
                String line = scanner.nextLine();
                channel.writeAndFlush(line+"\r\n");

            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {

            eventLoopGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        GroupClient client = new GroupClient("localhost",4869);
        client.run();


    }
}
