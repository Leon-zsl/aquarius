package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelStateEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientHandler extends SimpleChannelUpstreamHandler
{
    private static Log logger = LogFactory.getLog(ClientHandler.class);

    private Client owner;

    public ClientHandler(Client owner) {
        this.owner = owner;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        logger.warn("client channel received msg:" + e.getMessage());
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
}