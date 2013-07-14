package com.leonc.zodiac.aquarius.master;

import com.leonc.zodiac.aquarius.base.rpc.Node;

public class App 
{
    private static App instance = new App();
    public static App getInstance() { return instance; }

    private Node node;

    public void startup() {
        System.out.println("this is master");
    }

    public void shutdown() {
    }
}
