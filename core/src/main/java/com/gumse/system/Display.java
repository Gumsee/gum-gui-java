package com.gumse.system;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;

import com.gumse.maths.*;

public class Display {

    public static ivec2 vScreenSize;

    public static void init()
    {
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        vScreenSize = new ivec2(vidmode.width(), vidmode.height());
    }

    public static void pollEvents()
    {
        glfwPollEvents();
    }
}
