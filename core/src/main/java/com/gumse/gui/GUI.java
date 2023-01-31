package com.gumse.gui;

import org.lwjgl.opengl.GL30;

import com.gumse.gui.Basics.Button;
import com.gumse.gui.Basics.Dropdown;
import com.gumse.gui.Basics.Group;
import com.gumse.gui.Basics.Scroller;
import com.gumse.gui.Basics.Slider;
import com.gumse.gui.Basics.Switch;
import com.gumse.gui.Basics.Tabs;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Basics.TextField;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.gui.TagList.TagList;
import com.gumse.gui.XML.XMLGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;

public class GUI
{
	private RenderGUI WindowCanvas;
    private static Theme pCurrentTheme, pDefaultTheme = null;

	
	public GUI(Window contextwindow)  
	{
		GUIShader.initShaderProgram();
		//Gum::GUI::initFonts();

		WindowCanvas = new RenderGUI();
		WindowCanvas.setPosition(new ivec2(0,0));
		WindowCanvas.setSize(contextwindow.getSize());
		WindowCanvas.setType("MainCanvas");

        if(pDefaultTheme == null)
        {
            pDefaultTheme = new Theme();
            pDefaultTheme.backgroundColor     = new vec4(0.09f, 0.10f, 0.11f, 1.0f);
            pDefaultTheme.primaryColor        = new vec4(0.20f, 0.20f, 0.20f, 1.0f);
            pDefaultTheme.primaryColorShade   = new vec4(0.14f, 0.14f, 0.14f, 1.0f);
            pDefaultTheme.secondaryColor      = new vec4(0.26f, 0.26f, 0.31f, 1.0f);
            pDefaultTheme.textColor           = new vec4(0.90f, 0.90f, 0.90f, 1.0f);
            pDefaultTheme.accentColor         = new vec4(0.61f, 0.53f, 1.00f, 1.0f);
            pDefaultTheme.accentColorShade1   = new vec4(0.73f, 0.74f, 0.96f, 1.0f);
            pDefaultTheme.cornerRadius        = new vec4(7.00f, 7.00f, 7.00f, 7.0f);
            pCurrentTheme = pDefaultTheme;

			
			XMLGUI.addGUIType("box",       Box.createFromXMLNode());
			XMLGUI.addGUIType("button",    Button.createFromXMLNode());
			XMLGUI.addGUIType("tabs",      Tabs.createFromXMLNode());
			XMLGUI.addGUIType("group",     Group.createFromXMLNode());
			XMLGUI.addGUIType("dropdown",  Dropdown.createFromXMLNode());
			XMLGUI.addGUIType("scroller",  Scroller.createFromXMLNode());
			XMLGUI.addGUIType("slider",    Slider.createFromXMLNode());
			XMLGUI.addGUIType("switch",    Switch.createFromXMLNode());
			XMLGUI.addGUIType("text",      Text.createFromXMLNode());
			XMLGUI.addGUIType("textfield", TextField.createFromXMLNode());
			XMLGUI.addGUIType("textbox",   TextBox.createFromXMLNode());
			XMLGUI.addGUIType("tag-list",  TagList.createFromXMLNode());
        }

		/*pToolTipBox = new TextBox("", Gum::GUI::Fonts.getDefaultFont(), ivec2(0,0), ivec2(200, 200));
		pToolTipBox.setTextSize(12);
		fToolTipHideTimer = 0.0f;
		fToolTipShowTimer = 0.0f;*/
	}

	public void cleanup()
	{
		//Gum::_delete(pToolTipBox);
		WindowCanvas.destroyChildren();
	}

	public void addGUI(RenderGUI gui)
	{
		WindowCanvas.addGUI(gui);
	}

	/*void addWindow(GUIWindow* window)
	{
		window.getRenderWindow().bind();
		window.setParent(&WindowCanvas);
		window.resize();
		window.reposition();
		windows.push_back(window);
		window.getRenderWindow().unbind();
	}*/

	public void render()
	{
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glDisable(GL30.GL_DEPTH_TEST);
		WindowCanvas.render();
		//pToolTipBox.render();
		GL30.glEnable(GL30.GL_DEPTH_TEST);
		GL30.glDisable(GL30.GL_BLEND);
	}

	public void update()
	{
		//Debug.debug("Updating GUI");
		WindowCanvas.update();
		//WindowCanvas.updateToolTip();
		/*if(!pToolTipBox.isHidden())
		{
			fToolTipHideTimer += FPS.get();
			if(fToolTipHideTimer > 0.1f) //Hide after 2 seconds
			{
				fToolTipShowTimer = 0.0f;
				pToolTipBox.hide(true);
			}
		}*/
		//pColorMenu.update();
		RenderGUI.clickedSomething(false);
	}

    public void updateCanvas()
    {
        WindowCanvas.resize();
        WindowCanvas.reposition();
    }

    public static Theme getTheme()
    {
        return pCurrentTheme;
    }

    public static Theme getDefaultTheme()
    {
        return pDefaultTheme;
    }


	public ivec2 getSize()
	{
		return WindowCanvas.getSize();
	}

	public ivec2 getPosition()
	{
		return WindowCanvas.getPosition();
	}

	public void setSize(ivec2 size)
	{
		this.WindowCanvas.setSize(size);
		WindowCanvas.reposition();
		WindowCanvas.resize();
	}

    public static void setTheme(Theme theme)
    {
        pCurrentTheme = theme;
    }


	public static void cleanupAllGUIs()
    {
        //GUI.cleanupFonts();
        //Box.cleanup();
        //cleanupShaders();
    }
};