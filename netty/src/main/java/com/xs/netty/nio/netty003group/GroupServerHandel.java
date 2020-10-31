package com.xs.netty.nio.netty003group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @program: learn_root
 * @description: 服务器 handler
 * @author: xs-shuai.com
 * @create: 2020-06-17 22:09
 **/
public class GroupServerHandel extends SimpleChannelInboundHandler<String> {



    /***
     * 定义一个channel组,管理所有的channel
     * GlobalEventExecutor.INSTANCE全局的是事假执行器 单利
     */
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /***
     * 建立连接 当建立连接时第一个被执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        //推送到其他客户端

        //遍历所有的channelGroup里的所有channel 并发送消息
        channels.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入到群聊");
    }

    /**
     * channel在活动状态   上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


        System.out.println(ctx.channel().remoteAddress()+"上线");
    }

    /***
     * 下线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线");
    }

    /***
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了");
        //channels 会自动移除


    }

    /***
     * 读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
//        遍历group 不同的用户回送不同的消息
        channels.forEach(x->{
            if(x != channel){
                x.writeAndFlush("[客户]"+channel.remoteAddress()+"发送的消息"+msg+"\n");
            }else {
                x.writeAndFlush("[我]发送的消息"+msg+"\n");
            }
        });

    }

    /***
     * 异常处理 关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
