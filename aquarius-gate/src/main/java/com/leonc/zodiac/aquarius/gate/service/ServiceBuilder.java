package com.leonc.zodiac.aquarius.gate.service;

import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.service.MasterToPeerService;
import com.leonc.zodiac.aquarius.base.service.PeerToPeerService;
import com.leonc.zodiac.aquarius.gate.Acceptor;
import com.leonc.zodiac.aquarius.gate.App;

public final class ServiceBuilder
{
    public static void build(App app) {
        if(app == null) return;
        Node n = app.getNode();
        
        n.registerService(new MasterToPeerService(n));
        n.registerService(new PeerToPeerService(n));
        
        n.registerService(new RouteService(app.getAcceptor()));
    }
}