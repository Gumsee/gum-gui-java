package com.gumse.system;

import com.gumse.maths.*;
import com.gumse.system.io.Mouse;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class Event {
    public static final int GUM_EVENT_NONE =                  0x000;
    public static final int GUM_EVENT_FOCUS_IN =              0x001;
    public static final int GUM_EVENT_FOCUS_OUT =             0x002;
    public static final int GUM_EVENT_WINDOW_REPOSITION =     0x003;
    public static final int GUM_EVENT_WINDOW_RESIZE =         0x004;
    public static final int GUM_EVENT_WINDOW_CLOSE =          0x005;
    public static final int GUM_EVENT_MOUSE_PRESSED =         0x006;
    public static final int GUM_EVENT_MOUSE_RELEASED =        0x007;
    public static final int GUM_EVENT_MOUSE_MOVED =           0x008;
    public static final int GUM_EVENT_MOUSE_SCROLL =          0x009;
    public static final int GUM_EVENT_MOUSE_ENTERED =         0x00A;
    public static final int GUM_EVENT_MOUSE_LEFT =            0x00B;
    public static final int GUM_EVENT_KEYBOARD_PRESSED =      0x00C;
    public static final int GUM_EVENT_KEYBOARD_RELEASED =     0x00D;
    public static final int GUM_EVENT_KEYBOARD_TEXT_ENTERED = 0x00E;

    public class EventData
    {
        public ivec2 windowpos, windowsize;
        public ivec2 mousepos, mousescroll;
        public int mousebutton;
        public int keyboardkey, keyboardmod;

        EventData()
        {

        }
    };

    public int nativeEvent = 0;
    public int type = GUM_EVENT_NONE;
    public EventData data;

    Event()
    {
        data = new EventData();
    }


    public static void initNative(Window win)
    {
        glfwSetCursorPosCallback(win.getNativeWindow(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) 
            {
                Event evnt = new Event();
                evnt.data.mousepos = new ivec2((int)xpos, (int)ypos);
                evnt.type = Event.GUM_EVENT_MOUSE_MOVED;
                win.getMouse().handleEvent(evnt);
            }
        });

        glfwSetMouseButtonCallback(win.getNativeWindow(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) 
            {
                Event evnt = new Event();
                evnt.type = action == GLFW_PRESS ? Event.GUM_EVENT_MOUSE_PRESSED : Event.GUM_EVENT_MOUSE_RELEASED;
                switch(button)
                {
                    case GLFW_MOUSE_BUTTON_1: evnt.data.mousebutton = Mouse.GUM_MOUSE_BUTTON_LEFT;     break;
                    case GLFW_MOUSE_BUTTON_3: evnt.data.mousebutton = Mouse.GUM_MOUSE_BUTTON_MIDDLE;   break;
                    case GLFW_MOUSE_BUTTON_2: evnt.data.mousebutton = Mouse.GUM_MOUSE_BUTTON_RIGHT;    break;
                    case GLFW_MOUSE_BUTTON_4: evnt.data.mousebutton = Mouse.GUM_MOUSE_BUTTON_PREVIOUS; break;
                    case GLFW_MOUSE_BUTTON_5: evnt.data.mousebutton = Mouse.GUM_MOUSE_BUTTON_NEXT;     break;
                };
                win.getMouse().handleEvent(evnt);
            }
        });

        glfwSetScrollCallback(win.getNativeWindow(), new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) 
            {
                Event evnt = new Event();
                evnt.type = Event.GUM_EVENT_MOUSE_SCROLL;
                evnt.data.mousescroll = new ivec2((int)xoffset, (int)yoffset);

                win.getMouse().handleEvent(evnt);
            }
        });

        glfwSetCursorEnterCallback(win.getNativeWindow(), new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) 
            {
                Event evnt = new Event();
                evnt.type = entered ? Event.GUM_EVENT_MOUSE_ENTERED : Event.GUM_EVENT_MOUSE_LEFT;

                win.getMouse().handleEvent(evnt);
            }
        });

        glfwSetWindowFocusCallback(win.getNativeWindow(), new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focused) 
            {
                Event evnt = new Event();
                evnt.type = focused ? Event.GUM_EVENT_FOCUS_IN : Event.GUM_EVENT_FOCUS_OUT;

                win.handleEvent(evnt);
            }
        });

        glfwSetWindowPosCallback(win.getNativeWindow(), new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xpos, int ypos)
            {
                Event evnt = new Event();
                evnt.type = Event.GUM_EVENT_WINDOW_REPOSITION;
                evnt.data.windowpos = new ivec2(xpos, ypos);

                win.handleEvent(evnt);
            }
        });

        glfwSetWindowSizeCallback(win.getNativeWindow(), new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int xsize, int ysize)
            {
                Event evnt = new Event();
                evnt.type = Event.GUM_EVENT_WINDOW_RESIZE;
                evnt.data.windowsize = new ivec2(xsize, ysize);

                win.handleEvent(evnt);
            }
        });

        glfwSetWindowCloseCallback(win.getNativeWindow(), new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                win.close();
            } 
        });

        glfwSetKeyCallback(win.getNativeWindow(), new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) 
            {
                Event evnt = new Event();
                evnt.type = (action == GLFW_PRESS || action == GLFW_REPEAT) ? Event.GUM_EVENT_KEYBOARD_PRESSED : Event.GUM_EVENT_KEYBOARD_RELEASED;
                evnt.data.keyboardkey = key;
                evnt.data.keyboardmod = mods;

                win.getKeyboard().handleEvent(evnt);
            }
        });

        glfwSetCharCallback(win.getNativeWindow(), new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                Event evnt = new Event();
                evnt.type = Event.GUM_EVENT_KEYBOARD_TEXT_ENTERED;
                evnt.data.keyboardkey = codepoint;

                win.getKeyboard().handleEvent(evnt);
            }
        });
    }
}
