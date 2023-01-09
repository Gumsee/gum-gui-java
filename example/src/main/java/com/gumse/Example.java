package com.gumse;

import static org.lwjgl.opengl.GL11.*;

import com.gumse.basics.Globals;
import com.gumse.gui.GUI;
import com.gumse.gui.Theme;
import com.gumse.gui.AltMenu.AltMenu;
import com.gumse.gui.AltMenu.AltMenuEntry;
import com.gumse.gui.AltMenu.AltMenuEntry.AltMenuEntryCallback;
import com.gumse.maths.*;
import com.gumse.pages.ListsPage;
import com.gumse.pages.LoginPage;
import com.gumse.pages.MainPage;
import com.gumse.system.Display;
import com.gumse.system.Window;
import com.gumse.system.Window.*;
import com.gumse.tools.Output;
import com.gumse.tools.FPS;


public class Example
{
    public static void main(String[] args) 
    {
        Globals.DEBUG_BUILD = true;
        System.setProperty("java.awt.headless", "true"); //for iCrap support

        Output.init();
        Display.init();
        
        //Window Options
        Window pMainWindow = new Window("Example App", new ivec2(500, 500), Window.GUM_WINDOW_RESIZABLE, null);
        
        GUI testGUI = new GUI(pMainWindow);
		pMainWindow.onResized(new WindowResizePosCallback() {
            @Override public void run(ivec2 val) {
                testGUI.setSize(val);
            }
        });

        /*Theme lightTheme = new Theme();
        lightTheme.backgroundColor   = vec4.div(Color.HEXToRGBA("#FFFFFF"), 255.0f);
        lightTheme.primaryColor      = vec4.div(Color.HEXToRGBA("#FFFFFF"), 255.0f);
        lightTheme.secondaryColor    = vec4.div(Color.HEXToRGBA("#A6C7E5"), 255.0f);
        lightTheme.accentColor       = vec4.div(Color.HEXToRGBA("#0F79D9"), 255.0f);
        lightTheme.accentColorShade1 = vec4.div(Color.HEXToRGBA("#A6C7E5"), 255.0f);
        lightTheme.textColor         = vec4.div(Color.HEXToRGBA("#000000"), 255.0f);
        lightTheme.borderThickness   = 1;
        testGUI.setTheme(lightTheme);*/

        MainPage mainPage = new MainPage();
        LoginPage loginPage = new LoginPage();
        ListsPage listsPage = new ListsPage();
        mainPage.hide(false);
        loginPage.hide(true);
        listsPage.hide(true);

        //
        // ALWAYS ADD ENTRIES BEFORE ANYTHING ELSE
        //
        AltMenu altMenu = new AltMenu(new ivec2(0,0), new ivec2(100, 100));
        altMenu.setSizeInPercent(true, true);
        testGUI.addGUI(altMenu);

        AltMenuEntry fileEntry = new AltMenuEntry("File", null);
        altMenu.addEntry(fileEntry);
        fileEntry.addEntry(new AltMenuEntry("Open", new AltMenuEntryCallback() {
            @Override public void run() { Output.info("File should be opened"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Save", new AltMenuEntryCallback() {
            @Override public void run() { Output.info("File should be saved"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Save As", new AltMenuEntryCallback() {
            @Override public void run() { Output.info("File should be saved as"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Exit", new AltMenuEntryCallback() {
            @Override public void run() { Output.info("Exit.."); pMainWindow.close(); }
        }));

        AltMenuEntry editEntry = new AltMenuEntry("Edit", null);
        altMenu.addEntry(editEntry);
        editEntry.addEntry(new AltMenuEntry("Undo", new AltMenuEntryCallback() {
            @Override public void run() { Output.info("Undo"); }
        }));
        editEntry.addEntry(new AltMenuEntry("Redo", new AltMenuEntryCallback() {
            @Override public void run() { Output.info("Redo"); }
        }));
        editEntry.addEntry(new AltMenuEntry("Settings", new AltMenuEntryCallback() {
            @Override public void run() { Output.info("Settings"); }
        }));

        AltMenuEntry viewEntry = new AltMenuEntry("View", null);
        altMenu.addEntry(viewEntry);
        viewEntry.addEntry(new AltMenuEntry("Login", new AltMenuEntryCallback() {
            @Override public void run() { 
                loginPage.hide(false);
                mainPage.hide(true);
                listsPage.hide(true);
            }
        }));
        viewEntry.addEntry(new AltMenuEntry("Main", new AltMenuEntryCallback() {
            @Override public void run() 
            { 
                loginPage.hide(true);
                mainPage.hide(false);
                listsPage.hide(true);
            }
        }));
        viewEntry.addEntry(new AltMenuEntry("Lists", new AltMenuEntryCallback() {
            @Override public void run() 
            { 
                loginPage.hide(true);
                mainPage.hide(true);
                listsPage.hide(false);
            }
        }));

        
        
        altMenu.addGUI(mainPage);
        altMenu.addGUI(loginPage);
        altMenu.addGUI(listsPage);

        //RenderGUI xmlTest = XMLGUI.loadFile("guis/examplegui.xml");
        //mainPage.addGUI(xmlTest);

        altMenu.resize();
        altMenu.reposition();

        while(pMainWindow.isOpen())
        {
            Display.pollEvents();
            pMainWindow.setClearColor(GUI.getTheme().backgroundColor);
            pMainWindow.clear(GL_COLOR_BUFFER_BIT);
            testGUI.render();
            testGUI.update();

            //System.out.println("Mouse is: " + (Mouse.isBusy() ? "Busy" : "Available"));

            pMainWindow.finishRender();
            pMainWindow.getMouse().reset();
            pMainWindow.getKeyboard().reset();

            //pMainWindow.update();
            FPS.update();
		}

        pMainWindow.cleanup();
    }
}