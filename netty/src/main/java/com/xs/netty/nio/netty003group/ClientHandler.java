package com.xs.netty.nio.netty003group;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @program: learn_root
 * @description: 客户端
 * @author: xs-shuai.com
 * @create: 2020-06-19 00:27
 **/
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg.trim());

    }
}
