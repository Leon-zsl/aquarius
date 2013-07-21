package com.leonc.zodiac.aquarius.stage.service;

import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.service.MasterToPeerService;
import com.leonc.zodiac.aquarius.base.service.PeerToPeerService;

public final class ServiceBuilder
{
    public static void build(Node n) {
        if(n == null) return;
        n.registerService(new MasterToPeerService(n));
        n.registerService(new PeerToPeerService(n));
    }
}