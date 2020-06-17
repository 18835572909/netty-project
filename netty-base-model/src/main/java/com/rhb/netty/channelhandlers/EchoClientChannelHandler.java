package com.rhb.netty.channelhandlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: rhb
 * @date: 2020/6/17 22:40
 * @description:
 */
@ChannelHandler.Sharable
public class EchoClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /*
        Channel接受数据时调用。 function: 控制台输出接受信息
        注意：TCP协议只保证传输字节的顺序是发送时的顺讯。并不保证一次发送，接受的时候会全部接受？
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println(current+" Server CallBack："+byteBuf.toString(CharsetUtil.UTF_8));
    }

    /*
        Channel链接时被调用。function: Channel活跃时，发送信息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",CharsetUtil.UTF_8));
    }

    /*
        捕获异常，关闭Channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
