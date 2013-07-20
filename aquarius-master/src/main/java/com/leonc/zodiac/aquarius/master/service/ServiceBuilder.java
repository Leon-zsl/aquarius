package com.leonc.zodiac.aquarius.master.service;

import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.service.PeerToMasterService;

public final class ServiceBuilder
{
    public static void build(Node n) {
        if(n == null) return;

        n.registerService(new PeerToMasterService(n));
    }
}