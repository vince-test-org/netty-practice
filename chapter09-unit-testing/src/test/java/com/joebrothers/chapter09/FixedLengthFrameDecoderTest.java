package com.joebrothers.chapter09;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() {
        final ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        final ByteBuf input = buf.duplicate();
        final EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // Write bytes
        assertTrue(channel.writeInbound(input.retain()));  // retain!
        assertTrue(channel.finish());

        // Read messages
        ByteBuf read;
        for (int i=0; i<3; i++) {
            read = channel.readInbound();
            assertEquals(buf.readSlice(3), read);
            read.release();
        }

        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testFramesDecoded2() {
        final ByteBuf buf = Unpooled.buffer();
        for (int i=0; i<9; i++) {
            buf.writeByte(i);
        }

        final ByteBuf input = buf.duplicate();
        final EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertFalse(channel.writeInbound(input.readBytes(2)));  // frame is not ready to be read!
        assertTrue(channel.writeInbound(input.readBytes(7)));
        assertTrue(channel.finish());

        ByteBuf read;
        for (int i=0; i<3; i++) {
            read = channel.readInbound();
            assertEquals(buf.readSlice(3), read);
            read.release();
        }

        assertNull(channel.readInbound());
        buf.release();
    }

}
