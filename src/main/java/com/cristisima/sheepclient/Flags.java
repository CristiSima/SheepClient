package com.cristisima.sheepclient;

public class Flags {
    public static boolean ignoreForcedRotation()
    {
        if(Variables.uneventfulMove)
            return true;

        return false;
    }

    public static boolean positionPrecision()
    {
        if(Variables.fixPositionActive)
            return false;
        if(Variables.uneventfulMove)
            return false;

        return Variables.PositionPrecision.active;
    }

    public static boolean xRay()
    {
        return Variables.xRay.active;
    }

    public static boolean catEyes()
    {
        return Variables.CatEyes.active;
    }

    public static boolean noPositionPacket()
    {
        if(Variables.fixPositionActive)
            return true;
        if(Variables.uneventfulMove)
            return true;

        return Variables.noPositionPacket;
    }

    public static boolean uneventfulMove() {
        return Variables.uneventfulMove;
    }
}
