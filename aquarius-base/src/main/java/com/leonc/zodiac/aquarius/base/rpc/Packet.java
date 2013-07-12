package com.leonc.zodiac.aquarius.base.rpc;

public class Packet {
    private int opcode;
    private byte[] data;

    public Packet(int op, byte[] data) {
        this.opcode = op;
        this.data = data;
    }

    public int getOpcode() { return opcode; }
    public byte[] getData() { return data; }
}
