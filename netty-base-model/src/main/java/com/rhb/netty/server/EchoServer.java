package com.rhb.netty.server;

import com.rhb.netty.channelhandlers.EchoServerChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author: rhb
 * @date: 2020/6/17 21:59
 * @description: netty实现第一个server -.-
 */
public class EchoServer {

    private final static int ECHO_PORT=10000;

    public static void start() throws InterruptedException {
        final EchoServerChannelHandler echoChannelHandler = new EchoServerChannelHandler();
        //NIO传输
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            //NIO-Server的配置
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(ECHO_PORT)
                    //绑定ChannelHandler，监听入站信息。（将ChannelHandler实例添加到ChannelHandler的ChannelPipeline中）
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //EchoChannelHandler标识为@Sharable，所以可以总是使用同一实例
                            socketChannel.pipeline().addLast(echoChannelHandler);
                        }
                    });
            //异步绑定，同步阻塞直至绑定完成。
            ChannelFuture channelFuture = bootstrap.bind().sync();
            //阻塞式获取CloseFuture
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放EventLoopGroup资源
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        start();
    }

}
