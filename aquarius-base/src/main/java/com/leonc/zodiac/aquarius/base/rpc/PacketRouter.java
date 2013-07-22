package com.leonc.zodiac.aquarius.base.rpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelStateEvent;

public class PacketRouter extends SimpleChannelUpstreamHandler
{
	private static Log logger = LogFactory.getLog(ClientHandler.class);
	
    private Server owner;

    public PacketRouter(Server owner) {
        this.owner = owner;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if(owner != null)
            owner.putPacket((Packet)e.getMessage(), ctx.getChannel());
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        if(owner == null) return;
        owner.nodeConnected(ctx.getChannel());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        if(owner == null) return;
        owner.nodeDisconnected(ctx.getChannel());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    	logger.error("unexpected exception: " + e.getCause());
    	if(owner != null)
    		owner.nodeDisconnected(e.getChannel());
    }
}
