package com.gumse.tools;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.gumse.basics.Globals;

public class Debug 
{
    private static String sPrefix = "GumGUI:";
    private static long lStartTime;
    private static NumberFormat formatter;

    public static void init()
    {
        lStartTime = System.currentTimeMillis();
        formatter = new DecimalFormat("#0.00000");
    }

    public static <T> void info(T str)                   { System.out.println(getCurrentTime() + " (Info) "    + sPrefix + " " + str); }
    public static void warn(String str)                  { System.out.println(getCurrentTime() + " (Warning) " + sPrefix + " " + str); }
    public static void error(String str)                 { System.out.println(getCurrentTime() + " (Error) "   + sPrefix + " " + str); }
    public static void fatal(String str, int returncode) { System.out.println(getCurrentTime() + " (Fatal) "   + sPrefix + " " + str); System.exit(returncode); }
    public static void debug(String str)                 { if(Globals.DEBUG_BUILD) {System.out.println(getCurrentTime() + " (Debug) " + sPrefix + " " + str); } }
    private static String getCurrentTime()               { return "[" + formatter.format((System.currentTimeMillis() - lStartTime) / 1000d) + "]"; }
}
