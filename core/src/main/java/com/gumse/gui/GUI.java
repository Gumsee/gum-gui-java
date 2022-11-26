package com.gumse.gui;

import org.lwjgl.opengl.GL30;

import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.tools.Debug;

public class GUI
{
	private RenderGUI WindowCanvas;
	//private TextBox pToolTipBox;
	private float fToolTipHideTimer, fToolTipShowTimer;
	private mat4 m4Projection;

	
	public GUI(Window contextwindow)  
	{
		GUIShader.initShaderProgram();
		//Gum::GUI::initFonts();

		WindowCanvas = new RenderGUI();

		WindowCanvas.setPosition(new ivec2(0,0));
		WindowCanvas.setSize(contextwindow.getSize());
		WindowCanvas.setType("MainCanvas");

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


	/*public void showToolTip(String tooltip)
	{
		pToolTipBox.setString(tooltip);
		pToolTipBox.setPosition(Window.CurrentlyBoundWindow.getMouse().getPosition());
		fToolTipShowTimer += FPS.get();
		if(fToolTipShowTimer > 2.0f) //Show after 2 seconds
		{
			fToolTipHideTimer = 0.0f;
			pToolTipBox.hide(false);
		}
	}*/


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



	public static void cleanupAllGUIs()
    {
        //GUI.cleanupFonts();
        //Box.cleanup();
        //cleanupShaders();
    }
};