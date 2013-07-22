package com.leonc.zodiac.aquarius.gate;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelLocal;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelHandler.Sharable;

@Sharable
public class PacketRouter extends SimpleChannelUpstreamHandler
{
	private static Log logger = LogFactory.getLog(PacketRouter.class);
	
    private Acceptor owner;
    
    public static final ChannelLocal<String> SID = new ChannelLocal<String>() {
    	protected String initialValue(Channel ch) {
    		return "";
    	}
    };

    public PacketRouter(Acceptor owner) {
        this.owner = owner;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        Packet pck = (Packet)e.getMessage();
        String sid = SID.get(ctx.getChannel());
        this.owner.recvPacket(sid, pck);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Channel ch = ctx.getChannel();
        String sid = UUID.randomUUID().toString();
        SID.set(ch, sid);
        this.owner.addChannel(sid, ch);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
    	Channel ch = ctx.getChannel();
        String sid = SID.get(ch);
        this.owner.delChannel(sid);
        ch.close();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    	Channel ch = ctx.getChannel();
        String sid = SID.get(ch);
        this.owner.delChannel(sid);
        ch.close();
        
        logger.error("channel exception: " + e.getCause());
    }
}