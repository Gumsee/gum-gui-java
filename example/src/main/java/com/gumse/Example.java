package com.gumse;

import static org.lwjgl.opengl.GL11.*;

import com.gumse.basics.Globals;
import com.gumse.gui.GUI;
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
import com.gumse.system.io.Mouse;
import com.gumse.tools.Debug;
import com.gumse.tools.FPS;


public class Example {
    public static void main(String[] args) 
    {
        Globals.DEBUG_BUILD = true;
        Display.init();
        
        //Window Options
        Window pMainWindow = new Window("Example App", new ivec2(500, 500), Window.GUM_WINDOW_RESIZABLE, null);
        pMainWindow.setClearColor(new vec4(0.09f, 0.1f, 0.11f, 1.0f)); // Set the clear color);
        
        GUI testGUI = new GUI(pMainWindow);
		pMainWindow.onResized(new WindowResizePosCallback() {
            @Override public void run(ivec2 val) {
                testGUI.setSize(val);
            }
        });


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
            @Override public void run() { Debug.info("File should be opened"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Save", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("File should be saved"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Save As", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("File should be saved as"); }
        }));
        fileEntry.addEntry(new AltMenuEntry("Exit", new AltMenuEntryCallback() {
            @Override public void run() { Debug.info("Exit.."); pMainWindow.close(); }
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

        altMenu.resize();
        altMenu.reposition();

        while(pMainWindow.isOpen())
        {
            Display.pollEvents();
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
    }
}