package com.leonc.zodiac.aquarius.master.service;

import com.leonc.zodiac.aquarius.base.rpc.Node;
import com.leonc.zodiac.aquarius.base.rpc.Service;

public class ServiceBuilder
{
    public static void build(Node n) {
        Service sv = new NodeService();
        n.registerService(sv);
    }
}
