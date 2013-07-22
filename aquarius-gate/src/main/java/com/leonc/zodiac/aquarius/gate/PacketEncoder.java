package com.leonc.zodiac.aquarius.gate;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.leonc.zodiac.aquarius.base.packet.Packet;

public class PacketEncoder extends OneToOneEncoder
{
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel ch, Object msg) throws Exception {
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
        Packet pck = (Packet)msg;
        buf.writeInt(pck.getOpcode());
        buf.writeBytes(pck.getData());
        return buf;
    }
}
