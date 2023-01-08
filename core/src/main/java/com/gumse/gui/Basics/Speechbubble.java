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


        pBackground = new Box(new ivec2(0, 15), new ivec2(100, 100));
        pBackground.setMargin(new ivec2(0, 5));
        pBackground.setOrigin(new ivec2(0, 0));
        pBackground.setOriginInPercent(true, false);
        pBackground.setSizeInPercent(true, true);
        pBackground.setColor(GUI.getTheme().accentColor);
        addElement(pBackground);

        pIndicator = new Box(new ivec2(-10, 10), new ivec2(20, 20));
        pIndicator.setCornerRadius(new vec4(10, 0, 0, 0));
        pIndicator.setColor(new vec4(1,0,0,1));
        pIndicator.setRotation(-45.0f);
        addElement(pIndicator);

        resize();
        reposition();
    }

    @Override
    public void updateextra() 
    {
        if(Window.CurrentlyBoundWindow.getMouse().hasLeftClick() && !isMouseInside())
        {
            //this.hide(true);
        }
    }

    public void show()
    {
        this.hide(false);
    }
}