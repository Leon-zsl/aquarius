package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import java.lang.Thread;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Server 
{
    private static Log logger = LogFactory.getLog(Server.class);

    private Node owner = null;
    private String id = "";
    private int port = 0;
    private AtomicBoolean running = new AtomicBoolean();

    private ServerBootstrap bootstrap = null;

    private ConcurrentHashMap<String, Service> services = new ConcurrentHashMap<String, Service>();
    private ConcurrentLinkedQueue<Packet> pckQ = new ConcurrentLinkedQueue<Packet>();

    public Server(Node owner, String id, int port) {
        this.owner = owner;
        this.id = id;
        this.port = port;
    }

    public synchronized void start() {
        ServerBootstrap bootstrap = new ServerBootstrap(
            new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ServerPipelineFactory(this));
        bootstrap.bind(new InetSocketAddress(port));

        this.bootstrap = bootstrap;
        this.running.set(true);
        Thread th = new Thread(new Dispatcher());
        th.start();
    }

    public synchronized void close() {
        this.running.set(false);
        if(this.bootstrap != null) {
            this.bootstrap.releaseExternalResources();
            this.bootstrap = null;
        }
    }

    public String getId() { return this.id; }

    public void registerService(String name, Service sv) {
        services.putIfAbsent(name, sv);
    }

    public void unregisterService(String name) {
        services.remove(name);
    }

    public void putPacket(Packet pck) {
        if(pck == null) return;
        pckQ.add(pck);
    }

    private class Dispatcher implements Runnable {
        public void run() {
            while(true) {
                try {
                    long st = System.currentTimeMillis();
                    while(true) {
                        Packet pck = pckQ.poll();
                        if(pck == null) break;

                        Service sv = services.get(pck.getService());
                        if(!running.get()) break;

                        if(sv != null) {
                            Command cmd = new Command(pck.getSender(), 
                                                      pck.getService(), 
                                                      pck.getArgs());
                            sv.handleCommand(cmd);
                        }
                    }
                    if(!running.get())
                        break;
                    long elapse = System.currentTimeMillis() - st;
                    Thread.sleep(30 - elapse);
                } catch(Exception e) {
                    logger.error("execute service exception:" + e);
                }
            }
        }
    }
}
