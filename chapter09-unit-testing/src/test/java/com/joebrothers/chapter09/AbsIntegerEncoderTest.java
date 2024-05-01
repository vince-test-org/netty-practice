package com.joebrothers.chapter09;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

class AbsIntegerEncoderTest {

    @Test
    void testEncode() {
        final ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }

        final EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());

        // Write
        assertTrue(channel.writeOutbound(buf));
        assertTrue(channel.finish());

        // Read
        for (int i = 1; i < 10; i++) {
            assertEquals(i, (int) channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }
}
