package com.leonc.zodiac.aquarius.base.rpc;

public interface ServerConnListener
{
    void nodeConnected(String ip, int port);
    void nodeDisconnected(String ip, int port);
}