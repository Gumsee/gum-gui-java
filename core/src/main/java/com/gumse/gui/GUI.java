package com.gumse.gui;

import org.lwjgl.opengl.GL30;

import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;

public class GUI
{
	private RenderGUI WindowCanvas;
	//private TextBox pToolTipBox;
	private float fToolTipHideTimer, fToolTipShowTimer;
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
            pDefaultTheme.backgroundColor   = new vec4(0.09f, 0.10f, 0.11f, 1.0f);
            pDefaultTheme.primaryColor      = new vec4(0.20f, 0.20f, 0.20f, 1.0f);
            pDefaultTheme.secondaryColor    = new vec4(0.24f, 0.24f, 0.24f, 1.0f);
            pDefaultTheme.textColor         = new vec4(0.90f, 0.90f, 0.90f, 1.0f);
            pDefaultTheme.accentColor       = new vec4(0.60f, 0.58f, 0.85f, 1.0f);
            pDefaultTheme.accentColorShade1 = new vec4(0.73f, 0.74f, 0.96f, 1.0f);

            pCurrentTheme = pDefaultTheme;
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

    public void setTheme(Theme theme)
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