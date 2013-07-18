package com.leonc.zodiac.aquarius.base.rpc;

public interface ClientConnListener
{
    void nodeConnected(String ip, int port);
    void nodeDisconnected(String ip, int port);
}