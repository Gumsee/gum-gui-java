package com.gumse.system;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;

import com.gumse.maths.*;

public class Display {

    public static ivec2 vScreenSize;
    public static final int GUM_OS_UNKNOWN  = 0b0000000;
    public static final int GUM_OS_DOSLIKE  = 0b0000001;
    public static final int GUM_OS_UNIXLIKE = 0b0000010;
    public static final int GUM_OS_WINDOWS  = 0b0000101;
    public static final int GUM_OS_LINUX    = 0b0001010;
    public static final int GUM_OS_BSD      = 0b0010010;
    public static final int GUM_OS_MAC      = 0b0110010;
    public static final int GUM_OS_SOLARIS  = 0b1010010;

    private static int iOSType = GUM_OS_UNKNOWN;

    public static void init()
    {
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        String osstr = System.getProperty("os.name").toLowerCase();
        if     (osstr.indexOf("sunos") >= 0) { iOSType = GUM_OS_SOLARIS; }
        else if(osstr.indexOf("mac") >= 0)   { iOSType = GUM_OS_MAC; }
        else if(osstr.indexOf("bsd") >= 0)   { iOSType = GUM_OS_BSD; }
        else if(osstr.indexOf("nux") >= 0)   { iOSType = GUM_OS_LINUX; }
        else if(osstr.indexOf("win") >= 0)   { iOSType = GUM_OS_WINDOWS; }
        else if(osstr.indexOf("aix") >= 0)   { iOSType = GUM_OS_UNIXLIKE; }
        else if(osstr.indexOf("nix") >= 0)   { iOSType = GUM_OS_UNIXLIKE; }
        
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        vScreenSize = new ivec2(vidmode.width(), vidmode.height());
    }

    public static void pollEvents()
    {
        glfwPollEvents();
    }

    public static int getOSType()
    {
        return iOSType;
    }
}
