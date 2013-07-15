package com.leonc.zodiac.aquarius.master;

import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.AquariusException;
import com.leonc.zodiac.aquarius.master.service.ServiceBuilder;

import java.lang.Thread;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class App 
{
    private static App instance = new App();
    public static App getInstance() { return instance; }

    private volatile boolean running = false;
    private Node node = null;
    private NodeManager nodeMgr = null;
    private Properties sysConf = new Properties();

    private static Log logger = LogFactory.getLog(App.class);

    public Properties getSysConf() { return this.sysConf; }
    public Node getNode() { return this.node; }
    public NodeManager getNodeMgr() { return this.nodeMgr; }

    public void startup() {
        System.out.println("master starting...");
        Properties conf = this.initSysConf();
        if(conf == null) return;

        Node n = this.startNode(conf);
        if(n == null) return;

        this.createService(n);
        this.running = true;

        this.nodeMgr = new NodeManager();
        this.nodeMgr.start();
        System.out.println("master started");

        this.mainLoop();

        System.out.println("master closing...");
        this.running = false;
        if(this.nodeMgr != null) {
            this.nodeMgr.close();
            this.nodeMgr = null;
        }
        if(this.node != null) {
            this.node.close();
            this.node = null;
        }
        System.out.println("master closed");
    }

    public void shutdown() {
        this.running = false;
    }

    private void mainLoop() {
        while(this.running) {
            try {
                long st = System.currentTimeMillis();
                this.nodeMgr.update();
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
            Properties conf = new Properties();
            InputStream in = this.getClass().getResourceAsStream(Global.SYS_CONFIG_FILE);
            conf.load(in);
            in.close();

            this.sysConf = conf;
            return this.sysConf;
        } catch(IOException e) {
            logger.error("open sys conf file failed:" + e);
        }

        return null;
    }

    private Node startNode(Properties conf) {
        String nodeid = conf.getProperty("node_id");
        String nodetype = conf.getProperty("node_type");
        int port = Integer.parseInt(conf.getProperty("listen_port"));

        Node node = new Node(nodetype);
        node.start(nodeid, port);

        this.node = node;
        return this.node;
    }

    private void createService(Node n) {
        ServiceBuilder.build(n);
    }
}
