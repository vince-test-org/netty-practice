package com.joebrothers.chapter09;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

public class FrameChunkDecoder extends ByteToMessageDecoder {

    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) {
        if (maxFrameSize <= 0) {
            throw new IllegalArgumentException("maxFrameSize must be a positive number: " + maxFrameSize);
        }

        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws Exception {
        final int readableBytes = in.readableBytes();
        if (readableBytes > maxFrameSize) {
            // discard
            in.clear();
            throw new TooLongFrameException();
        }
        final ByteBuf buf = in.readBytes(readableBytes);
        out.add(buf);
    }
}
