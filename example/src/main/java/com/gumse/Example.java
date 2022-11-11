package com.gumse;

import static org.lwjgl.opengl.GL11.*;

import com.gumse.basics.Globals;
import com.gumse.gui.GUI;
import com.gumse.gui.AltMenu.AltMenu;
import com.gumse.gui.AltMenu.AltMenuEntry;
import com.gumse.gui.AltMenu.AltMenuEntry.AltMenuEntryCallback;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.Text;
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
        Globals.DEBUG_BUILD = false;
        Display.init();
        
        //Window Options
        Window pMainWindow = new Window("Example App", new ivec2(500, 500), Window.GUM_WINDOW_RESIZABLE, null);
        pMainWindow.setClearColor(new vec4(0.5f, 0.0f, 0.0f, 0.0f)); // Set the clear color);

        GUI testGUI = new GUI(pMainWindow);

        Box testBox = new Box(new ivec2(30, 30), new ivec2(50, 50));
        testBox.setSizeInPercent(true, true);
        testBox.setColor(new vec4(1.0f,1.0f,0.0f,1.0f));
        /*Texture hehe = new Texture();
        hehe.load("hehe.jpg");
        testBox.setTexture(hehe);*/
        testGUI.addGUI(testBox);

        String testStr = "Test Text with unicode characters: öäüß \n";
        for(int i = 64; i < 128; i++)
            testStr += Character.toString((char)i);

        FontManager fonts = FontManager.getInstance();

        AltMenu altMenu = new AltMenu(new ivec2(0,0), new ivec2(100, 100));
        altMenu.setSizeInPercent(true, true);
        testGUI.addGUI(altMenu);

        AltMenuEntry fileEntry = new AltMenuEntry("File", null);
        altMenu.addEntry(fileEntry);
        fileEntry.addEntry(new AltMenuEntry("Open", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("File should be opened"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Save", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("File should be saved"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Save As", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("File should be saved as"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Exit", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("Exit.."); }
        }));

        AltMenuEntry editEntry = new AltMenuEntry("Edit", null);
        altMenu.addEntry(editEntry);
        editEntry.addEntry(new AltMenuEntry("Undo", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("Undo"); }
        }));
        editEntry.addEntry(new AltMenuEntry("Redo", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("Redo"); }
        }));
        editEntry.addEntry(new AltMenuEntry("Settings", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("Settings"); }
        }));

        
		pMainWindow.onResized(new WindowResizePosCallback() {
            @Override
            public void run(ivec2 val) {
                testGUI.setSize(val);
            }
        });

        TextBox textBox = new TextBox("Some test text", fonts.getDefaultFont(), new ivec2(100, 100), new ivec2(200, 40));
        altMenu.addGUI(textBox);
        

        while(pMainWindow.isOpen())
        {
            Mouse.update();
            Display.pollEvents();
            pMainWindow.clear(GL_COLOR_BUFFER_BIT);
            testGUI.render();
            testGUI.update();

            pMainWindow.finishRender();
            pMainWindow.getMouse().reset();

            //pMainWindow.update();
            //FPS.update();
		}
    }
}