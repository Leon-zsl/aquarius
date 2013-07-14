package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

//import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Client
{
    private static Log logger = LogFactory.getLog(Client.class);

    private Node owner = null;

    private ConcurrentHashMap<String, Channel> conns = new ConcurrentHashMap<String, Channel>();
    private ClientBootstrap bootstrap = null;

    public Client(Node owner) {
        this.owner = owner;
    }

    public synchronized void start() {
        logger.info("start client...");
        ClientBootstrap bootstrap = new ClientBootstrap(
            new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool()));
        this.bootstrap = bootstrap;
    }

    public synchronized void close() {
        logger.info("close client...");

        for(Channel conn : this.conns.values()) {
            conn.close();
        }
        this.conns.clear();

        if(this.bootstrap != null) {
            this.bootstrap.releaseExternalResources();
            this.bootstrap = null;
        }
    }

    public synchronized void connect(String id, String ip, int port) {
        if(id == null || id.equals("") || ip == null || ip.equals("") || port == 0)
            return;

        if(conns.containsKey(id)) {
            logger.info("node exists:" + id);
            return;
        }

        logger.info("connect new node:[id]" + id + "[ip]" + ip + "[port]" + port);

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        this.conns.putIfAbsent(id, channel);
    }

    public synchronized void disconnect(String id) {
        if(id == null || id.equals(""))
            return;
        Channel ch = this.conns.get(id);
        if(ch != null) {
            ch.close();
            this.conns.remove(id);
        }
    }

    public void sendPacket(String id, String service, byte[] args) {
        Packet pck = new Packet();
        pck.setSender(owner.getServer().getId());
        pck.setReceiver(id);
        pck.setService(service);
        pck.setArgs(args);
        Channel ch = conns.get(id);
        if(ch != null) {
            ch.write(pck);
        }
    }
}
