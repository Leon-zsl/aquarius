package com.leonc.zodiac.aquarius.gate;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.jboss.netty.buffer.ChannelBuffer;

import com.leonc.zodiac.aquarius.base.packet.Packet;

public class PacketDecoder extends OneToOneDecoder
{
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel ch, Object msg) throws Exception {
        ChannelBuffer buf = (ChannelBuffer)msg;
        int op = buf.readInt();
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        return new Packet(op, data);
    }
}