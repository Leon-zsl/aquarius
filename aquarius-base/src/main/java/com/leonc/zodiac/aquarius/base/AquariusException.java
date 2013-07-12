package com.leonc.zodiac.aquarius.base;

import java.lang.Exception;

public class AquariusException extends Exception 
{
    private int errCode;
    private String errMsg;

    public AquariusException(int errCode, String errMsg) {
        super("[code]" + errCode + "\n[msg]" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
