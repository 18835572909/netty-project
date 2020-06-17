package com.rhb.netty.client;

import com.rhb.netty.channelhandlers.EchoClientChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author: rhb
 * @date: 2020/6/17 22:49
 * @description: Netty-Client: First Demo
 */
public class EchoClient {
    private final String host;
    private final Integer port;
    public EchoClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        final EchoClientChannelHandler echoClientChannelHandler = new EchoClientChannelHandler();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoClientChannelHandler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int i=0;
        while(i<10){
            new EchoClient("192.168.1.103",10000).start();
            i++;
        }
    }
}
