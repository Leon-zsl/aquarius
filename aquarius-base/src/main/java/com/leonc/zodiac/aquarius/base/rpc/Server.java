package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.Channel;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import java.net.InetSocketAddress;
import java.lang.reflect.*;
import java.lang.Class;

import java.lang.Thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Server 
{
    private static Log logger = LogFactory.getLog(Server.class);

    private String listenIp = "";
    private int listenPort = 0;

    private boolean singleThread = false;
    private AtomicBoolean running = new AtomicBoolean();

    private ServerBootstrap bootstrap = null;
    private Channel serverChannel = null;

    private ServerConnListener connListener = null;
    private ConcurrentHashMap<String, Channel> conns = new ConcurrentHashMap<String, Channel>();

    private ConcurrentHashMap<String, Service> services = new ConcurrentHashMap<String, Service>();
    private ConcurrentLinkedQueue<Command> cmdQ = new ConcurrentLinkedQueue<Command>();

    public ServerConnListener getConnListener() { return this.connListener; }
    public Server setConnListener(ServerConnListener l) {
        this.connListener = l;
        return this;
    }

    public Channel[] getConnChannels() {
        return conns.values().toArray(new Channel[0]);
    }

    public Channel getConnChannel(String remoteIp, int remotePort) {
        String addr = remoteIp + ":" + remotePort;
        return conns.get(addr);
    }

    public void closeConn(String remoteIp, int remotePort) {
        Channel ch = getConnChannel(remoteIp, remotePort);
        if(ch == null) return;
        
        InetSocketAddress sock = (InetSocketAddress)ch.getRemoteAddress();
        String addr = sock.getHostString() + ":" + sock.getPort();
        ch.close();
        conns.remove(addr);
    }

    public String getListenIp() { return this.listenIp; }
    public int getListenPort() { return this.listenPort; }
    public Channel getServerChannel() {
        return this.serverChannel;
    }

    public synchronized void start(String ip, int port, 
                                   boolean singleThread) {
        ServerBootstrap bootstrap = new ServerBootstrap(
            new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ServerPipelineFactory(this));
        Channel ch = bootstrap.bind(new InetSocketAddress(ip, port));

        InetSocketAddress addr = (InetSocketAddress)ch.getLocalAddress();
        this.listenIp = addr.getHostString();
        this.listenPort = addr.getPort();
        this.bootstrap = bootstrap;

        this.running.set(true);
        this.singleThread = singleThread;
        if(!singleThread) {
            Thread th = new Thread(new Dispatcher());
            th.start();
        }
    }

    public synchronized void close() {
        this.running.set(false);

        for(Channel ch : conns.values()) {
            ch.close();
        }
        conns.clear();

        if(this.bootstrap != null) {
            this.bootstrap.releaseExternalResources();
            this.bootstrap = null;
        }
    }

    public boolean getSingleThread() { return this.singleThread; }

    public void registerService(Service sv) {
        if(sv == null) return;

        String name = sv.getClass().getSimpleName();
        services.putIfAbsent(name, sv);
    }

    //used internal
    public void putPacket(Packet pck, Channel ch) {
        if(pck == null || ch == null) return;
        try {
            Command cmd = new Command(pck, ch);
            cmdQ.add(cmd);
        } catch(Exception ex) {
            logger.error("create command exception:" + 
                         "\n[service]" + pck.getServiceName() + 
                         "\n[method]" + pck.getMethodName() +
                         "\n[message]" + pck.getMessageName() +
                         "\n[except]" + ex);
        }
    }

    //used internal
    public void nodeConnected(Channel ch) {
        //if(!singleThread) 
        {
            if(this.connListener != null) {
                handleNewconn(ch);
            }
        }
    }

    public void nodeDisconnected(Channel ch) {
        //if(!singleThread) 
        {
            if(this.connListener != null) {
                handleDisconn(ch);
            }
        }
    }

    //used in main thread when single thread
    public void dispatch() {
        if(singleThread) {
            //this.dispatchNewconns();
            //this.dispatchDisconns();
            this.dispatchCommand();
        }
    }

    // private void dispatchNewconns() {
    //     if(connListener == null) return;
    //     while(true) {
    //         Channel ch = newconns.poll();
    //         if(ch == null) break;

    //         handleNewconn(ch);
    //     }
    // }

    // private void dispatchDisconns() {
    //     if(connListener == null) return;
    //     while(true) {
    //         Channel ch = disconns.poll();
    //         if(ch == null) break;

    //         handleDisconn(ch);
    //     }
    // }

    private void dispatchCommand() {
        while(true) {
            if(!running.get()) break;

            Command cmd = cmdQ.poll();
            if(cmd == null) break;
            
            handleCommand(cmd);
        }
    }

    private void handleNewconn(Channel ch) {
        InetSocketAddress addr = (InetSocketAddress)ch.getRemoteAddress();
        this.connListener.nodeConnected(addr.getHostString(), addr.getPort());
        this.conns.putIfAbsent(addr.getHostString() + ":" + addr.getPort(), ch);        
    }

    private void handleDisconn(Channel ch) {
        InetSocketAddress addr = (InetSocketAddress)ch.getRemoteAddress();
        String key = addr.getHostString() + ":" + addr.getPort();
        Channel channel = this.conns.get(key);
        if(channel == null) return;

        this.connListener.nodeDisconnected(addr.getHostString(),
                                           addr.getPort());
        channel.close();
        this.conns.remove(key);
    }

    //this method may be called in multi-thread
    private void handleCommand(Command cmd) {
        try {
            Service sv = services.get(cmd.getServiceName());
            if(sv == null) {
                logger.info("service not exists:" + cmd.getServiceName());
                return;
            }

            Class clsService = sv.getClass();
            Method handler = clsService.getDeclaredMethod(cmd.getMethodName(), 
                                                          Command.class);
            handler.invoke(sv, cmd);
        } catch(Exception ex) {
            logger.error("java cmd handle exception:" + 
                         "\n[service]" + cmd.getServiceName() + 
                         "\n[method]" + cmd.getMethodName() +
                         "\n[message]" + cmd.getMessage().getClass().getName() +
                         "\n[except]" + ex);
        }
    }

    private class Dispatcher implements Runnable {
        public void run() {
            ExecutorService threalPool = Executors.newCachedThreadPool();
            while(true) {
                try {
                    long st = System.currentTimeMillis();

                    while(true) {
                        if(!running.get()) break;

                        Command cmd = cmdQ.poll();
                        if(cmd == null) break;

                        threalPool.execute(new CommandHandler(cmd));
                    }
                    if(!running.get()) break;

                    long elapse = System.currentTimeMillis() - st;
                    Thread.sleep(30 - elapse);
                } catch(Exception e) {
                    logger.error("execute service exception:" + e);
                }
            }
            threalPool.shutdown();
        }

        private class CommandHandler implements Runnable {
            private Command cmd;

            public CommandHandler(Command cmd) {
                this.cmd = cmd;
            }

            public void run() {
                handleCommand(this.cmd);
            }
        }
    }
}
