package com.cristisima.sheepclient;

import net.minecraft.client.network.ServerAddress;

import java.util.HashSet;
import java.util.Set;

public class Variables {
    public  static class PositionPrecision {
        public static int DEFAULT = 2;
        public static int PRECISION = DEFAULT;
        public static boolean active = true;
    }
    public static class InitialHeightOffset
    {
        public static boolean active=false;
        public static int DEFAULT = 0;
        public static int offset=DEFAULT;
        public static boolean initialized=false;
        public static int DefaultInitializedCount =2;
        public static int initializedCount =DefaultInitializedCount;
    }

    public static class AutoReconnect
    {
        public static ServerAddress lastServer;
        public static float reconnectAfter=10;

        public static boolean reconnectOnMultiplayerMenu=false;
//        public static
    }

    public static class xRay
    {
        public static boolean active=false;
    }

    public static class CatEyes
    {
        public static boolean active=true;
        public static final int MAX=15;
        public static int forced_min=10;
    }

    public static int injectedYVelocity=0;
    public static boolean noPositionPacket=false;
    public static boolean noDemo=true;
    public static boolean noCreative=false;
    public static boolean fixPositionActive=false;
    public static boolean FlyActive=true;
    public static boolean NoFall=true;

    public static Set<String> packets_seen=new HashSet();

}
