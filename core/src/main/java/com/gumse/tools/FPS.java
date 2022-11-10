package com.gumse.tools;

import static org.lwjgl.glfw.GLFW.*;

public class FPS {
    
    private static float frametime = 0;
    private static float fCurrentTime = (float)glfwGetTime();
    private static float fPreviousTime = 0;
    private static float fRunTime = 0;

    public static void update()
    {
        fCurrentTime = (float)glfwGetTime();
        frametime = fCurrentTime - fPreviousTime;
        fPreviousTime = fCurrentTime;
        fRunTime += frametime;
    }

    public static float getFPS()
    {
        return 1 / frametime;
    }
    public static float getFrametime()
    {
        return frametime;
    }

    public static float getRuntime()
    {
        return fRunTime;
    }
}
