package com.gumse.gui.Basics;

import com.gumse.gui.GUI;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.system.Window;

public class Speechbubble extends RenderGUI
{
    public enum Side
    {
        ABOVE,
        BELOW,
        LEFT,
        RIGHT
    }

    private Box pBackground;
    private Box pIndicator;
    private Side iSide;

    public Speechbubble(ivec2 pos, ivec2 size, Side side)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.iSide = side;


        pIndicator = new Box(new ivec2(0, 0), new ivec2(20, 20));
        pIndicator.setColor(GUI.getTheme().primaryColor);
        pIndicator.setRotation(45.0f);
        addElement(pIndicator);

        pBackground = new Box(new ivec2(0, 0), new ivec2(100, 100));
        pBackground.setSizeInPercent(true, true);
        pBackground.setCornerRadius(new vec4(7));
        pBackground.setColor(GUI.getTheme().primaryColor);
        addElement(pBackground);

        switch(iSide)
        {
            case ABOVE:
                pIndicator.setPosition(new ivec2(-10, -20));
                pIndicator.setCornerRadius(new vec4(0, 0, 0, 10));

                pBackground.setPosition(new ivec2(0, -10));
                pBackground.setOrigin(new ivec2(50, 100));
                pBackground.setOriginInPercent(true, true);
                break;

            case BELOW:
                pIndicator.setPosition(new ivec2(-10, 5));
                pIndicator.setCornerRadius(new vec4(0, 10, 0, 0));

                pBackground.setPosition(new ivec2(0, 15));
                pBackground.setOrigin(new ivec2(50, 0));
                pBackground.setOriginInPercent(true, false);
                break;

            case LEFT:
                pIndicator.setPosition(new ivec2(-20, -10));
                pIndicator.setCornerRadius(new vec4(0, 0, 10, 0));

                pBackground.setPosition(new ivec2(-10, 0));
                pBackground.setOrigin(new ivec2(100, 50));
                pBackground.setOriginInPercent(true, true);
                break;

            case RIGHT:
                pIndicator.setPosition(new ivec2(5, -10));
                pIndicator.setCornerRadius(new vec4(10, 0, 0, 0));

                pBackground.setPosition(new ivec2(15, 0));
                pBackground.setOrigin(new ivec2(0, 50));
                pBackground.setOriginInPercent(false, true);
                break;
            
        }

        resize();
        reposition();
    }

    @Override
    public void updateextra() 
    {
        if(Window.CurrentlyBoundWindow.getMouse().hasLeftClickStart() && !pBackground.isMouseInside())
        {
            this.hide(true);
        }
    }

    @Override
    protected void updateOnColorChange() 
    {
        pBackground.setColor(getColor(GUI.getTheme().primaryColor));
        pIndicator.setColor(getColor(GUI.getTheme().primaryColor));
    }

    @Override
    protected void updateOnThemeChange() 
    {
        pBackground.setColor(getColor(GUI.getTheme().primaryColor));
        pIndicator.setColor(getColor(GUI.getTheme().primaryColor));
    }

    @Override
    protected void updateOnAddGUI(RenderGUI gui) 
    {
        removeChild(gui);
        pBackground.addGUI(gui);
    }
    @Override public void onClick(GUICallback callback) { pBackground.onClick(callback); }
    @Override public void destroyChildren()             { pBackground.destroyChildren(); }
    @Override public void removeChild(RenderGUI child)  { pBackground.removeChild(child); }
    @Override public void removeChild(int index)        { pBackground.removeChild(index); }

    public void show()
    {
        this.hide(false);
    }
}