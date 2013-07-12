package com.leonc.zodiac.aquarius.base.event;

public abstract class Event
{
    protected  String cmd = "";
    protected boolean handled = false;

    public String getCmd() { return cmd; }
    public boolean IsHandled() { return handled; }
    public void SetHandled(boolean val) { handled = val; }
}
