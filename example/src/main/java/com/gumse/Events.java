package com.gumse;

import static org.lwjgl.opengl.GL11.*;

import com.gumse.maths.*;
import com.gumse.system.Display;
import com.gumse.system.Window;
import com.gumse.system.Window.*;
import com.gumse.system.io.Mouse;
import com.gumse.system.io.Mouse.*;
import com.gumse.system.io.Keyboard;
import com.gumse.system.io.Keyboard.*;
import com.gumse.tools.Output;
import com.gumse.tools.FPS;


public class Events {
    public static void main(String[] args) 
    {
        Display.init();
        
        //Window Options
        Window pMainWindow = new Window("Event Tests", new ivec2(1920, 1080), Window.GUM_WINDOW_RESIZABLE, null);
        pMainWindow.setClearColor(new vec4(0.5f, 0.0f, 0.0f, 0.0f)); // Set the clear color);

        pMainWindow.onFocused(new WindowFocusCallback() {
            @Override
            public void run(boolean hasfocus) {
                if(hasfocus) Output.info("Window gained Focus");
                else         Output.info("Window lost Focus");
            }
        });

        pMainWindow.onMoved(new WindowResizePosCallback() {
            @Override
            public void run(ivec2 position) {
                Output.info("Window moved: " + position.toString());
            }
        });

        pMainWindow.onResized(new WindowResizePosCallback() {
            @Override
            public void run(ivec2 size) {
                Output.info("Window resized: " + size.toString());
            }
        });

        pMainWindow.getMouse().onMoved(new MouseMovedCallback() {
            @Override
            public void run(ivec2 position) {
                Output.info("Mouse moved: " + position.toString());
            }
        });

        pMainWindow.getMouse().onPress(new MouseButtonCallback() {
            @Override
            public void run(int button, int mod) {
                if     (button == Mouse.GUM_MOUSE_BUTTON_LEFT)     Output.info("Mouse Left Clicked");
                else if(button == Mouse.GUM_MOUSE_BUTTON_RIGHT)    Output.info("Mouse Right Clicked");
                else if(button == Mouse.GUM_MOUSE_BUTTON_MIDDLE)   Output.info("Mouse Middle Clicked");
                else if(button == Mouse.GUM_MOUSE_BUTTON_NEXT)     Output.info("Mouse Next Clicked");
                else if(button == Mouse.GUM_MOUSE_BUTTON_PREVIOUS) Output.info("Mouse Previous Clicked");
            }
        });

        pMainWindow.getMouse().onRelease(new MouseButtonCallback() {
            @Override
            public void run(int button, int mod) {
                if     (button == Mouse.GUM_MOUSE_BUTTON_LEFT)     Output.info("Mouse Left Released");
                else if(button == Mouse.GUM_MOUSE_BUTTON_RIGHT)    Output.info("Mouse Right Released");
                else if(button == Mouse.GUM_MOUSE_BUTTON_MIDDLE)   Output.info("Mouse Middle Released");
                else if(button == Mouse.GUM_MOUSE_BUTTON_NEXT)     Output.info("Mouse Next Released");
                else if(button == Mouse.GUM_MOUSE_BUTTON_PREVIOUS) Output.info("Mouse Previous Released");
            }
        });

        pMainWindow.getMouse().onScroll(new MouseMovedCallback() {
            @Override
            public void run(ivec2 scroll) {
                Output.info("Mouse Scrolled: " + scroll.toString());
            }
        });

        pMainWindow.getMouse().onLeft(new MouseEnteredLeftCallback() {
            @Override
            public void run() {
                Output.info("Mouse Left");
            }
        });

        pMainWindow.getMouse().onEntered(new MouseEnteredLeftCallback() {
            @Override
            public void run() {
                Output.info("Mouse Entered");
            }
        });

        pMainWindow.getKeyboard().onKeyPress(new KeyboardButtonCallback() {
            @Override
            public void run(int key, int mod) {
                Output.info("Keyboard key Pressed: " + Keyboard.key2string(key));
            }
        });

        pMainWindow.getKeyboard().onKeyRelease(new KeyboardButtonCallback() {
            @Override
            public void run(int key, int mod) {
                Output.info("Keyboard key Released: " + Keyboard.key2string(key));
            }
        });

        pMainWindow.getKeyboard().onTextEntered(new KeyboardTextCallback() {
            @Override
            public void run(String str, int codepoint) {
                Output.info("Keyboard text entered: " + str);
            }
        });


        while(pMainWindow.isOpen())
        {            
            pMainWindow.clear(GL_COLOR_BUFFER_BIT);
            pMainWindow.finishRender();
            pMainWindow.getMouse().reset();
            Display.pollEvents();

            //pMainWindow.update();
            FPS.update();
		}
    }
}