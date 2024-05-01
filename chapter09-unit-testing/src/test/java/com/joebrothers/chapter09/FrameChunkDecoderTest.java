package com.joebrothers.chapter09;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;

class FrameChunkDecoderTest {

    @Test
    void testFramesDecoded() {
        final ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        final ByteBuf input = buf.duplicate();

        final EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));
        assertTrue(channel.writeInbound(input.readBytes(2)));

        try {
            // bigger than max size
            channel.writeInbound(input.readBytes(4));
            fail();
        } catch (TooLongFrameException e) {
            // expected
        }

        assertTrue(channel.writeInbound(input.readBytes(3)));
        assertTrue(channel.finish());

        // Read
        ByteBuf read;
        {
            read = channel.readInbound();
            assertEquals(buf.readSlice(2), read);
            read.release();
        }
        {
            read = channel.readInbound();
            assertEquals(buf.skipBytes(4).readSlice(3), read);
            read.release();
        }

        buf.release();
    }
}
