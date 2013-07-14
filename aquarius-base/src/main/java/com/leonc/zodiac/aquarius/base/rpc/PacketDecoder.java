package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.buffer.ChannelBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PacketDecoder extends OneToOneEncoder
{
    private static Log logger = LogFactory.getLog(PacketEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if(!(msg instanceof ChannelBuffer)) {
            logger.error("msg is not channel buffer");
            return msg;
        }
        Packet pck = new Packet();
        pck.decode((ChannelBuffer)msg);
        return pck;
    }
}
