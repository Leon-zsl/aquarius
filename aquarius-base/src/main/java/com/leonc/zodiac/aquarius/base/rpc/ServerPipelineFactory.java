package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.channel.Channels;

public class ServerPipelineFactory implements ChannelPipelineFactory
{
    private Server owner;

    public ServerPipelineFactory(Server owner) { 
        this.owner = owner; 
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = Channels.pipeline();
        p.addLast("headerDecoder", new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
        p.addLast("packetDecoder", new PacketDecoder());
        p.addLast("packetRouter", new PacketRouter(this.owner));
        return p;
    }
}
