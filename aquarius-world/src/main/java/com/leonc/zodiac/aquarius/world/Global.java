package com.leonc.zodiac.aquarius.world;

public final class Global
{
    public static final long FRAME_TIME = 60;//ms

    public static final String SYS_CONFIG_FILE = "/sysconf.properties";
    
    public static final int GUILD_OP_MIN = (1 << 18) + 1;
    public static final int GUILD_OP_MAX = (1 << 18) + 65535;
    
    public static final int TRADE_OP_MIN = (1 << 18) + 1;
    public static final int TRADE_OP_MAX = (1 << 18) + 65535;
    
    public static final int REPOSITORY_OP_MIN = (1 << 18) + 1;
    public static final int REPOSITORY_OP_MAX = (1 << 18) + 65535;
}