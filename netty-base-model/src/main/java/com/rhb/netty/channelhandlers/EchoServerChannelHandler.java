package com.rhb.netty.channelhandlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * @author: rhb
 * @date: 2020/6/17 21:34
 * @description:
 */

/*
    @Sharable: 标识可以被多个Channel安全的共享ChannelHandler
 */
@ChannelHandler.Sharable
public class EchoServerChannelHandler extends ChannelInboundHandlerAdapter {

    /*
        每个传入的信息都会调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接受到的信息写给发送者，但不刷新
        ByteBuf in = (ByteBuf) msg;
        System.out.println(in.toString(Charset.defaultCharset()));
        ctx.write(in);
    }

    /*
        通知ChannelInboundHandler当前是这次批次中最后一次调用channelRead().即当前批量读取的结束标识
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将ChannelOutboundBuffer中的信息刷新到远程节点，并关闭Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /*
        过程中异常抛出后的调用：可以用于服务降级
        如果没有实现异常捕获，异常将会传递到下个ChannelHandler，即将记录在ChannelPipeLine的尾端记录
        （每个ChannelHandler都有一个关联的ChannelPipeLine）
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //关闭Channel
        ctx.close();
    }
}
