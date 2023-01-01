package com.gumse.gui.Basics;

import org.lwjgl.opengl.GL11;

import com.gumse.basics.SmoothFloat;
import com.gumse.gui.GUI;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Mouse;

public class Scroller extends RenderGUI
{
	private Box pScrollBar;
	private Box pScrollIndicator;
	private RenderGUI pContent;
	private RenderGUI pMainChildContainer;

	private SmoothFloat pIndicatorWidthFloat;
	private int iMaxValue;
	private int iStepSize;
	private int iIndicatorPos;
    private boolean bSnapped;
	private boolean bHasOverflow;
	private int iSnapOffset;

	private void moveContent()
	{
		for(int i = 0; i < pMainChildContainer.numChildren(); i++)
		{   
			RenderGUI child = pMainChildContainer.getChild(i);
			float ypos = child.getPosition().y;
			//Hide if outside
			child.hide(ypos + child.getSize().y < vActualPos.y + vOrigin.y ||
						ypos > vActualPos.y + vActualSize.y + vOrigin.y); 
		}
	
		float contentOverlap = pContent.getBoundingBox().size.y - vActualSize.y;
		if(contentOverlap == 0.0f)
		{
			bHasOverflow = false;
			pContent.setPosition(new ivec2(0, 0));
			return;
		}
		bHasOverflow = true;
		iMaxValue = Math.max((int)contentOverlap, 0);
		iIndicatorPos = GumMath.clamp(iIndicatorPos, 0, iMaxValue);

		float scrollPercentage = (float)iIndicatorPos / (float)iMaxValue;
		int indicatorSize = (int)(((float)vActualSize.y / (float)pContent.getBoundingBox().size.y) * pScrollBar.getSize().y);
		float indicatorPos = (pScrollBar.getSize().y - indicatorSize) * scrollPercentage;

		pScrollIndicator.setSize(new ivec2(pScrollIndicator.getSize().x, indicatorSize));
		pScrollIndicator.setPositionY((int)(indicatorPos));
		pContent.setPosition(new ivec2(0, (int)(-(scrollPercentage * contentOverlap))));
	}



	public Scroller(ivec2 position, ivec2 size)
	{
		this.sType = "Scroller";
		this.vSize = size;
		this.vPos = position;
		this.vMargin.x = -20;
		this.bSnapped = false;
		this.iIndicatorPos = 0;
		this.iStepSize = 30;
		this.iMaxValue = 0;
		this.bHasOverflow = false;
		this.pIndicatorWidthFloat = new SmoothFloat(0, 10, 0);

		pContent = new RenderGUI();
		pContent.setSize(new ivec2(100, 100));
		pContent.setSizeInPercent(true, true);
		pContent.shouldKeepTrackOfBoundingbox(true);
		addElement(pContent);
		this.pMainChildContainer = pContent;

		pScrollBar = new Box(new ivec2(100,5), new ivec2(10, 100));
		pScrollBar.setCornerRadius(new vec4(8.0f));
		pScrollBar.setPositionInPercent(true, false);
		pScrollBar.setSizeInPercent(false, true);
		pScrollBar.setOrigin(new ivec2(0, 0));
		pScrollBar.setMargin(new ivec2(0, -10));
		//pScrollBar.setColor(new vec4(0.4f,0.4f,1.0f,1.0f));
		addElement(pScrollBar);

		pScrollIndicator = new Box(new ivec2(5,0), new ivec2(10, 10));
		pScrollIndicator.setCornerRadius(new vec4(8.0f));
		//pScrollIndicator.setColor(new vec4(0.19f, 0.2f, 0.42f, 1.0f));
		pScrollBar.addGUI(pScrollIndicator);

		resize();
		reposition();
	}


	public void cleanup() 
	{
		//Gum::_delete(pScrollBar);
		//Gum::_delete(pScrollIndicator);
	}

	public void update()
	{
		if(!bIsHidden)
		{
			if(!Mouse.isBusy())
			{
				if(isMouseInside())
				{
					if(Window.CurrentlyBoundWindow.getMouse().getMouseWheelState() != 0)
					{
						iIndicatorPos += -Window.CurrentlyBoundWindow.getMouse().getMouseWheelState() * iStepSize;
						moveContent();
					}
				}

				if(pScrollBar.isMouseInside())
				{
					pScrollIndicator.setColor(GUI.getTheme().accentColor);
					pIndicatorWidthFloat.setTarget(12);

					if(Window.CurrentlyBoundWindow.getMouse().hasLeftClick())
					{
						bSnapped = true;
						iSnapOffset = pScrollIndicator.getPosition().y - Window.CurrentlyBoundWindow.getMouse().getPosition().y;
						pScrollIndicator.setColor(GUI.getTheme().accentColor);
						Mouse.setBusiness(true);
					}
				}
				else
				{
					pScrollIndicator.setColor(GUI.getTheme().accentColorShade1);
					pIndicatorWidthFloat.setTarget(6);
				}
			}

			if(bSnapped)
			{
				iIndicatorPos = Window.CurrentlyBoundWindow.getMouse().getPosition().y - pScrollBar.getPosition().y + iSnapOffset;
				moveContent();

				if(!Window.CurrentlyBoundWindow.getMouse().hasLeftClick())
				{
					pScrollIndicator.setColor(GUI.getTheme().accentColorShade1);
					bSnapped = false;
					Mouse.setBusiness(false);
				}
			}
			
			if(pIndicatorWidthFloat.update())
			{
				int width = (int)pIndicatorWidthFloat.get();
				pScrollIndicator.setSize(new ivec2(width, pScrollIndicator.getSize().y));
				pScrollIndicator.setOrigin(new ivec2((int)(width * 0.5f), 0));
				pScrollIndicator.setCornerRadius(new vec4((float)width * 0.8f));
				pScrollIndicator.setPositionX(10);
			}

			//updatechildren();
            pContent.update();
		}
	}


	protected void updateOnAddGUI(RenderGUI gui)
	{
		pContent.addGUI(gui);
		//vChildren.clear();
		moveContent();
	}

    @Override
    public void destroyChildren() 
    {
        super.destroyChildren();
        pContent.clear();
    }

    protected void updateOnPosChange()
	{
		moveContent();
	}

	public void updateContent()
	{
		moveContent();
	}

	public void render()
	{
		if(!bIsHidden)
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(vActualPos.x, Window.CurrentlyBoundWindow.getSize().y - vActualPos.y - vActualSize.y, vActualSize.x, vActualSize.y);
			for(int i = 0; i < pContent.numChildren(); i++)
			{
				pContent.getChild(i).render();
			}
			Window.CurrentlyBoundWindow.resetViewport();
			GL11.glDisable(GL11.GL_SCISSOR_TEST);

			if(bHasOverflow)
				pScrollIndicator.render();
		}
	}

	public void setStepSize(int step)
	{
		this.iStepSize = step;
	}

	public void setMainChildContainer(RenderGUI gui)
	{
		this.pMainChildContainer = gui;
	}


	public int getOffset()
	{
		return this.pContent.getRelativePosition().y;
	}

	public static Scroller createFromXMLNode(XMLNode node)
	{
		return new Scroller(new ivec2(0,0), new ivec2(1,1));
	}
};