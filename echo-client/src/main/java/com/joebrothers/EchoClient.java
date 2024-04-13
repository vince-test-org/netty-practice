package com.joebrothers;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public final class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(final String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }

    public void start() throws Exception {
        final EchoClientHandler clientHandler = new EchoClientHandler();
        final EventLoopGroup group = new NioEventLoopGroup();

        try {
            final Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .remoteAddress(new InetSocketAddress(host, port))
             .handler(new ChannelInitializer<>() {
                 @Override
                 protected void initChannel(Channel ch) throws Exception {
                     ch.pipeline().addLast(clientHandler);
                 }
             });

            final ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
