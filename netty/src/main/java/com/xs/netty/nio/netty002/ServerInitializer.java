package com.xs.netty.nio.netty002;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-06-16 23:26
 **/
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        //管道加入处理器
        ChannelPipeline pipeline = socketChannel.pipeline();
        //加入一个netty httpServerCode codec =>[code - decoder]
        //httpServerCodec是netty提供的一个http编码解码器

        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //自定义处理器
        pipeline.addLast("http",new HttpHandler());

    }
}
