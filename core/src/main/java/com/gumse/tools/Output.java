package com.gumse.tools;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.gumse.basics.Globals;

public class Output 
{
    public interface OutputCallback
    {
        public void onInfo(String msg);
        public void onWarn(String msg);
        public void onError(String msg);
        public void onFatal(String msg, int ret);
        public void onDebug(String msg);
    }

    private static String sPrefix = "GumGUI:";
    private static long lStartTime;
    private static NumberFormat formatter;
    private static OutputCallback pCallback;

    public static void init()
    {
        lStartTime = System.currentTimeMillis();
        formatter = new DecimalFormat("#0.00000");
        pCallback = null;
    }

    
    public static <T> void info(T str)                   
    { 
        if(pCallback == null) 
            System.out.println(getCurrentTime() + " (Info) "    + sPrefix + " " + str); 
    }
    
    public static void info(String str)                  
    { 
        if(pCallback == null) 
            System.out.println(getCurrentTime() + " (Info) "    + sPrefix + " " + str); 
        else 
            pCallback.onInfo(str); 
    }
    
    public static void warn(String str)                  
    { 
        if(pCallback == null) 
            System.out.println(getCurrentTime() + " (Warning) " + sPrefix + " " + str); 
        else 
            pCallback.onWarn(str); 
    }
    
    public static void error(String str)                 
    { 
        if(pCallback == null) 
            System.out.println(getCurrentTime() + " (Error) "   + sPrefix + " " + str); 
        else 
            pCallback.onError(str); 
    }
    
    public static void fatal(String str, int returncode) 
    { 
        if(pCallback == null) 
        {
            System.out.println(getCurrentTime() + " (Fatal) "   + sPrefix + " " + str); 
            System.exit(returncode); 
        }
        else
        {
            pCallback.onFatal(str, returncode);
        }
    }
    
    public static void debug(String str)                 
    { 
        if(Globals.DEBUG_BUILD) 
        {
            if(pCallback == null)
                System.out.println(getCurrentTime() + " (Debug) " + sPrefix + " " + str); 
            else
                pCallback.onDebug(str);
        }
    }

    public static void setCallback(OutputCallback callback) { pCallback = callback; }

    private static String getCurrentTime()               { return "[" + formatter.format((System.currentTimeMillis() - lStartTime) / 1000d) + "]"; }
}
