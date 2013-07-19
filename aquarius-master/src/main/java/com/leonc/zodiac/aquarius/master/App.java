package com.leonc.zodiac.aquarius.master;

import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.AquariusException;
import com.leonc.zodiac.aquarius.base.rpc.ClientConnListener;
import com.leonc.zodiac.aquarius.base.rpc.ServerConnListener;
import com.leonc.zodiac.aquarius.master.service.ServiceBuilder;

import java.lang.Thread;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class App 
{
    public class ClientConnListenerImpl implements ClientConnListener {
        public void nodeConnected(String ip, int port) {
            logger.info("connect finished:" + ip + ":" + port);
        }

        public void nodeDisconnected(String ip, int port) {
            logger.info("disconnect finished:" + ip + ":" + port);
        }
    }

    public class ServerConnListenerImpl implements ServerConnListener {
        public void nodeConnected(String ip, int port) {
            logger.info("new node connected:" + ip + ":" + port);
        }

        public void nodeDisconnected(String ip, int port) {
            logger.info("node disconnected: " + ip + ":" + port);
        }
    }

    private static App instance = new App();
    public static App getInstance() { return instance; }

    private volatile boolean running = false;
    private Node node = new Node();
    private Properties sysConf = new Properties();

    private static Log logger = LogFactory.getLog(App.class);

    public Properties getSysConf() { return this.sysConf; }
    public Node getNode() { return this.node; }

    public void startup() {
        this.start();
        this.mainLoop();
        this.close();
    }

    public void shutdown() {
        this.running = false;
    }

    private void start() {
        System.out.println("master starting...");

        Properties conf = this.initSysConf();
        if(conf == null) {
            logger.info("init sys conf failed");
            return;
        }

        Node n = this.startNode(conf);
        if(n == null) {
            logger.error("start node module failed");
            return;
        }
        
        this.createService(n);
        this.running = true;

        System.out.println("master started");
    }

    private void close() {
        System.out.println("master closing...");
        this.running = false;

        if(this.node != null) {
            this.node.close();
            this.node = null;
        }
        System.out.println("master closed");
    }

    private void mainLoop() {
        while(this.running) {
            try {
                long st = System.currentTimeMillis();
                //do nothing now
                //this.peerMgr.update();
                long dt = System.currentTimeMillis() - st;
                Thread.sleep(Global.FRAME_TIME - dt);
            } catch(Exception ex) {
                logger.error("exception caught:" + ex);
                break;
            }
        }
    }

    private Properties initSysConf() {
        try {
            InputStream in = this.getClass().getResourceAsStream(Global.SYS_CONFIG_FILE);
            this.sysConf.load(in);
            in.close();

            return this.sysConf;
        } catch(IOException e) {
            logger.error("open sys conf file failed:" + e);
        }

        this.sysConf = new Properties();
        return null;
    }

    private Node startNode(Properties conf) {
        String nodeid = conf.getProperty("node_id");
        String nodetype = conf.getProperty("node_type");
        int port = Integer.parseInt(conf.getProperty("listen_port"));

        this.node.setNodeId(nodeid).setNodeType(nodetype);
        this.node.setServerConnListener(new ServerConnListenerImpl());
        this.node.setClientConnListener(new ClientConnListenerImpl());
        this.node.start(port);

        return this.node;
    }

    private void createService(Node n) {
        ServiceBuilder.build(n);
    }
}
