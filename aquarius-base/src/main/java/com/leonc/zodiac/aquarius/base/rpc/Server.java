package com.leonc.zodiac.aquarius.base.rpc;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import java.net.InetSocketAddress;

import com.google.protobuf.Message;
import java.lang.reflect.*;
import java.lang.Class;

import java.lang.Thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Server 
{
    private static Log logger = LogFactory.getLog(Server.class);

    private Node owner = null;
    private String id = "";
    private int port = 0;
    private boolean singleThread = false;
    private AtomicBoolean running = new AtomicBoolean();

    private ServerBootstrap bootstrap = null;

    private ConcurrentHashMap<String, Service> services = new ConcurrentHashMap<String, Service>();
    private ConcurrentLinkedQueue<Packet> pckQ = new ConcurrentLinkedQueue<Packet>();

    public Server(Node owner, String id, int port) {
        this.owner = owner;
        this.id = id;
        this.port = port;
    }

    public synchronized void start(boolean singleThread) {
        ServerBootstrap bootstrap = new ServerBootstrap(
            new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ServerPipelineFactory(this));
        bootstrap.bind(new InetSocketAddress(port));

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
        if(this.bootstrap != null) {
            this.bootstrap.releaseExternalResources();
            this.bootstrap = null;
        }
    }

    public String getId() { return this.id; }
    public boolean getSingleThread() { return this.singleThread; }

    public void registerService(Service sv) {
        if(sv == null) return;

        String name = sv.getClass().getSimpleName();
        services.putIfAbsent(name, sv);
    }

    public void putPacket(Packet pck) {
        if(pck == null) return;
        pckQ.add(pck);
    }

    //used in main thread when single thread
    public void dispatch() {
        if(singleThread)
            this.dispatchPacket();
    }

    private void dispatchPacket() {
        while(true) {
            if(!running.get()) break;

            Packet pck = pckQ.poll();
            if(pck == null) break;
            
            handlePacket(pck);
        }
    }

    //this method may be called in multi-thread
    private void handlePacket(Packet pck) {
        if(pck == null) return;
        try {
            Service sv = services.get(pck.getServiceName());
            if(sv == null) {
                logger.info("service not exists:" + pck.getServiceName());
                return;
            }

            Command cmd = new Command();
            cmd.setRemoteId(pck.getSender());
                    
            cmd.setServiceName(pck.getServiceName());
            cmd.setMethodName(pck.getMethodName());

            String clsName = pck.getMessageName();
            Class cls = Class.forName(clsName);
            Method instance = cls.getDeclaredMethod("getDefaultInstance", null);
            Message proto = (Message)instance.invoke(null, null);
            Message msg = proto.newBuilderForType().mergeFrom(pck.getMessageData()).build();
            cmd.setMessage(msg);

            Class clsService = sv.getClass();
            Method handler = clsService.getDeclaredMethod(cmd.getMethodName(), Command.class);
            handler.invoke(sv, cmd);
        } catch(Exception ex) {
            logger.error("java pck handle exception:" + 
                         "\n[service]" + pck.getServiceName() + 
                         "\n[method]" + pck.getMethodName() +
                         "\n[message]" + pck.getMessageName() +
                         "\n[message data]" + pck.getMessageData() +
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

                        Packet pck = pckQ.poll();
                        if(pck == null) break;

                        threalPool.execute(new PacketHandler(pck));
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

        private class PacketHandler implements Runnable {
            private Packet packet;

            public PacketHandler(Packet pck) {
                this.packet = pck;
            }

            public void run() {
                Packet pck = this.packet;
                handlePacket(pck);
            }
        }
    }
}
