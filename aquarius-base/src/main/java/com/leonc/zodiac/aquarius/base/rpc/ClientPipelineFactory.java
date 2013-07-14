package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;

public class ClientPipelineFactory implements ChannelPipelineFactory
{
	public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = Channels.pipeline();
        p.addLast("headerEncoder", new LengthFieldPrepender(4));
        p.addLast("packetEncoder", new PacketEncoder());
        return p;
    }
}
