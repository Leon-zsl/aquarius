package com.leonc.zodiac.aquarius.gate;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.channel.Channels;

public class GatePipelineFactory implements ChannelPipelineFactory
{
    private Acceptor owner = null;

    public GatePipelineFactory(Acceptor owner) {
        this.owner = owner;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = Channels.pipeline();
        p.addLast("headerDecoder", new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
        p.addLast("packetDecoder", new PacketDecoder());
        p.addLast("packetRouter", new PacketRouter(this.owner));

        p.addLast("headEncoder", new LengthFieldPrepender(4));
        p.addLast("packetEncoder", new PacketEncoder());
        return p;
    }
}