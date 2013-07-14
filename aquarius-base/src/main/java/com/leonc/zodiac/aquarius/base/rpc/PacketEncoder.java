package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PacketEncoder extends OneToOneEncoder
{
    private static Log logger = LogFactory.getLog(PacketEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel ch, Object msg) throws Exception {
        if(!(msg instanceof Packet)) {
            logger.error("encode data is not packet");
            return msg;
        }
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
        ((Packet)msg).encode(buf);
        return buf;
    }
}
