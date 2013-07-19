package com.leonc.zodiac.aquarius.master.service;

import com.leonc.zodiac.aquarius.base.rpc.Node;

public class ServiceBuilder
{
    public static void build(Node n) {
        if(n == null) return;
        n.registerService(new PeerToMasterService());
    }
}