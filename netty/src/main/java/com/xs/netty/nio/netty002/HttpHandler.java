package com.xs.netty.nio.netty002;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @program: learn_root
 * @description: handler
 * @author: xs-shuai.com
 * @create: 2020-06-16 23:26
 **/
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {

    //SimpleChannelInboundHandler  extends  ChannelInboundHandlerAdapter
    //HttpObject 是客户端和服务器端的通讯的数据封装成httpObject

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if(msg instanceof HttpRequest){

            System.out.println("msg类型:"+msg.getClass());
            System.out.println("客户端地址:"+ctx.channel().remoteAddress());


            //获取到
            HttpRequest httpRequest = (HttpRequest) msg;
        //获取 uri, 过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico, 不做响应");
                return;
            }
            //回复信息给浏览器 [http 协议]

            //返回数据
            ByteBuf byteBuf = Unpooled.copiedBuffer("this is server", CharsetUtil.UTF_8);

            //返回对象
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

            //设置头
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());

//            返回
            ctx.writeAndFlush(fullHttpResponse);


        }

    }
}
