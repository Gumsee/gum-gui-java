package com.gumse;

import static org.lwjgl.opengl.GL11.*;

import com.gumse.gui.GUI;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.maths.*;
import com.gumse.system.Display;
import com.gumse.system.Window;
import com.gumse.system.Window.*;
import com.gumse.system.io.Mouse;
import com.gumse.textures.Texture;
import com.gumse.tools.Debug;


public class Example {
    public static void main(String[] args) 
    {
        Display.init();
        
        //Window Options
        Window pMainWindow = new Window("Example App", new ivec2(1920, 1080), Window.GUM_WINDOW_RESIZABLE, null);
        pMainWindow.setClearColor(new vec4(0.5f, 0.0f, 0.0f, 0.0f)); // Set the clear color);

        GUI testGUI = new GUI(pMainWindow);

        Box testBox = new Box(new ivec2(30, 30), new ivec2(50, 50));
        testBox.setSizeInPercent(true, true);
        testBox.setColor(new vec4(1.0f,1.0f,0.0f,1.0f));
        /*Texture hehe = new Texture();
        hehe.load("hehe.jpg");
        testBox.setTexture(hehe);*/
        testGUI.addGUI(testBox);
        
		pMainWindow.onResized(new WindowResizePosCallback() {
            @Override
            public void run(ivec2 val) {
                testGUI.setSize(val);
            }
        });

        FontManager fonts = FontManager.getInstance();

        int index = 0;

        while(pMainWindow.isOpen())
        {
            Mouse.update();
            Display.pollEvents();
            pMainWindow.clear(GL_COLOR_BUFFER_BIT);

            testBox.setTexture(fonts.getDefaultFont().getCharacter(20 + index % 100).texture);

            testGUI.render();
            testGUI.update();

            pMainWindow.finishRender();
            pMainWindow.getMouse().reset();

            //pMainWindow.update();
            //FPS.update();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            index++;
		}
    }
}