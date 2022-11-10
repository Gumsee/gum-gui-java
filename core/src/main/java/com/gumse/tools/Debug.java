package com.gumse.tools;

import com.gumse.basics.Globals;

public class Debug 
{
    private static String sPrefix = "Game1:";
    private static long lStartTime;

    public static void init()
    {
        lStartTime = System.nanoTime();
    }

    public static void info(String str)  { System.out.println(getCurrentTime() + " (Info) "    + sPrefix + " " + str); }
    public static void warn(String str)  { System.out.println(getCurrentTime() + " (Warning) " + sPrefix + " " + str); }
    public static void error(String str) { System.out.println(getCurrentTime() + " (Error) "   + sPrefix + " " + str); System.exit(1); }
    public static void debug(String str) { if(Globals.DEBUG_BUILD) {System.out.println(getCurrentTime() + " (Debug) " + sPrefix + " " + str); } }
    private static String getCurrentTime() { return "[" + Float.toString(System.nanoTime() - lStartTime) + "]"; }
}
