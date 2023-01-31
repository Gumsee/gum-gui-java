package com.gumse.gui.Basics;

import org.lwjgl.opengl.GL11;

import com.gumse.basics.SmoothFloat;
import com.gumse.gui.GUI;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
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
	private int iStepSize;
	private int iIndicatorPos, iLastIndicatorPos;
    private boolean bSnapped;
	private boolean bHasOverflow;
    private boolean bHasHitBottom, bHasHitTop;
	private int iSnapOffset;
    private GUICallback pOnTopHitCallback, pOnBottomHitCallback;

	private void moveContent()
	{	
		float contentOverlap = pContent.getBoundingBox().size.y - vActualSize.y;
        int upperlimit = 0;
		if(contentOverlap == 0.0f)
		{
			bHasOverflow = false;
			pContent.setPosition(new ivec2(0, 0));
		}
        else
        {
            bHasOverflow = true;
            int indicatorSize = (int)(((float)vActualSize.y / (float)pContent.getBoundingBox().size.y) * pScrollBar.getSize().y);
            upperlimit = vActualSize.y - indicatorSize - 5;
            iIndicatorPos = GumMath.clamp(iIndicatorPos, 0, upperlimit);

            pScrollIndicator.setPositionY((int)(iIndicatorPos));
            pScrollIndicator.setSize(new ivec2(pScrollIndicator.getSize().x, indicatorSize));

            float scrollPercentage = (float)iIndicatorPos / (float)upperlimit;
            pContent.setPosition(new ivec2(0, (int)(-(scrollPercentage * contentOverlap))));
        }
        
		for(int i = 0; i < pMainChildContainer.numChildren(); i++)
		{   
			RenderGUI child = pMainChildContainer.getChild(i);
			float ypos = child.getPosition().y;
			//Hide if outside
			child.hide(ypos + child.getSize().y < vActualPos.y + vOrigin.y ||
						ypos > vActualPos.y + vActualSize.y + vOrigin.y); 
		}

        if(iIndicatorPos == iLastIndicatorPos)
            return;
        
        if(bHasHitBottom && iIndicatorPos < iLastIndicatorPos) bHasHitBottom = false;
        if(bHasHitTop && iIndicatorPos > iLastIndicatorPos)    bHasHitTop = false;
        
        if(pOnTopHitCallback != null && !bHasHitTop && iIndicatorPos == 0)
        {
            pOnTopHitCallback.run(this);
            bHasHitBottom = true;
        }
        else if(pOnBottomHitCallback != null && !bHasHitBottom && iIndicatorPos == upperlimit)
        {
            pOnBottomHitCallback.run(this);
            bHasHitTop = true;
        }
        iLastIndicatorPos = iIndicatorPos;
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
		this.bHasOverflow = false;
		this.pIndicatorWidthFloat = new SmoothFloat(0, 10, 0);
        this.hideChildren(true);

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

	public void updateextra()
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
                    iSnapOffset = pScrollIndicator.getRelativeMousePosition().y;
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
            pScrollIndicator.setCornerRadius(new vec4((float)width * 0.75f));
            pScrollIndicator.setPositionX(10);
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
        pContent.destroyChildren();
    }

    @Override
    public void removeChild(RenderGUI child) 
    {
        super.removeChild(child);
        pContent.removeChild(child);
    }

    @Override
    public void removeChild(int index) 
    {
        super.removeChild(index);
        pContent.removeChild(index);
    }

    protected void updateOnPosChange()
	{
		moveContent();
	}

	public void updateContent()
	{
		moveContent();
	}

	public void renderextra()
	{
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(vActualPos.x, Window.CurrentlyBoundWindow.getSize().y - vActualPos.y - vActualSize.y, vActualSize.x, vActualSize.y);
        for(int i = pContent.numChildren(); i --> 0;) { pContent.getChild(i).render();  }
        //for(int i = 0; i < pContent.numChildren(); i++) { pContent.getChild(i).render(); }
        Window.CurrentlyBoundWindow.resetViewport();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if(bHasOverflow)
            pScrollIndicator.render();
	}

    //
    // Setter
    //
	public void setStepSize(int step)                { this.iStepSize = step; }
	public void setMainChildContainer(RenderGUI gui) { this.pMainChildContainer = gui; }
    public void onTopHit(GUICallback callback)       { this.pOnTopHitCallback = callback; }
    public void onBottomHit(GUICallback callback)    { this.pOnBottomHitCallback = callback; }


    //
    // Getter
    //
	public RenderGUI getMainChildContainer()         { return this.pMainChildContainer; }
	public int getOffset()                           { return this.pContent.getRelativePosition().y; }


    //
    // Static
    //
    public static XMLGUICreator createFromXMLNode() 
    {
        return (XMLNode node) -> { 
			return new Scroller(new ivec2(0,0), new ivec2(1,1));
        };
    };
};