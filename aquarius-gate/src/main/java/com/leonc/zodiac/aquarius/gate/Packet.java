package com.leonc.zodiac.aquarius.gate;

public class Packet
{
    private int opcode = 0;
    private byte[] data = null;

    public Packet(int opcode, byte[] data) {
        this.opcode = opcode;
        this.data = data;
    }

    public int getOpcode() { return this.opcode; }
    public byte[] getData() { return this.data; }
}