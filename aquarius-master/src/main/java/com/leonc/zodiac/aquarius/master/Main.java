package com.leonc.zodiac.aquarius.master;

import java.lang.Class;
import java.lang.reflect.*;
import com.google.protobuf.Message;
import com.leonc.zodiac.aquarius.base.proto.ProtoMaster;

public class Main
{
    public static void main(String[] args) {
        Message msg = genMsg();

        String name = msg.getClass().getName();
        byte[] data = msg.toByteArray();

        Message pmsg = parseMsg(name, data);
        
        App.getInstance().startup();
    }

    private static Message genMsg() {
        ProtoMaster.RegisterNode.Builder builder = ProtoMaster.RegisterNode.newBuilder();
        builder.setId("master_main").setType("master");
        ProtoMaster.RegisterNode msg = builder.build();

        System.out.println("[id]" + msg.getId() + "[type]" + msg.getType() + "[name]" + msg.getClass().getSimpleName());
        return msg;
    }

    private static Message parseMsg(String name, byte[] data) {
        System.out.println("[name]" + name + "[data]" + data.length);

        try {
            Class cls = Class.forName(name);
            Method mtd = cls.getDeclaredMethod("getDefaultInstance", null);
            Message proto = (Message)mtd.invoke(null, null);
            Message msg = proto.newBuilderForType().mergeFrom(data).build();
            ProtoMaster.RegisterNode pm = (ProtoMaster.RegisterNode)msg;
            System.out.println("[id]" + pm.getId() + "[type]" + pm.getType());
        } catch(Exception ex) {
            System.out.println("java reflection error:" + "[msg]" + name + "[except]" + ex);
        }

        return null;
    }
}
