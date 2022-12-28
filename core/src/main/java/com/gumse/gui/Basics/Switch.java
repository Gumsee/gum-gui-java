package com.gumse.gui.Basics;

import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Toolbox;

public class Switch extends RenderGUI
{
    private Box pBackground;
    private Box pTickbox;
    
    public Switch(ivec2 pos, ivec2 size, float radius) 
    {
        this.sType = "Switch";
        this.vPos.set(pos);
        this.vSize.set(size);

        pBackground = new Box(new ivec2(0,0), new ivec2(100, 100));
        pBackground.setCornerRadius(new vec4(radius));
        pBackground.setSizeInPercent(true, true);
        pBackground.setColor(new vec4(0.3f, 0.3f, 0.3f, 1.0f));
        addElement(pBackground);


        pTickbox = new Box(new ivec2(10,10), new ivec2(80, 80));
        pTickbox.setCornerRadius(new vec4(radius * 0.8f));
        pTickbox.setPositionInPercent(true, true);
        pTickbox.setSizeInPercent(true, true);
        pTickbox.setColor(new vec4(0.3f,0.6f,0.3f,1.0f));
        //pTickbox.setColor(vec4(Gum::Maths::HSVToRGB(vec3(rand() % 360, 100, 70)),1.0));
        addElement(pTickbox);

        resize();
        reposition();
    }

    public void cleanup() {};

    public void update()
    {
        if(isMouseInside())
        {
            Mouse.setActiveHovering(true);
            Window.CurrentlyBoundWindow.getMouse().setCursor(Mouse.GUM_CURSOR_HAND);
        }
        if(isClicked())
        {
            pTickbox.hide(!pTickbox.isHidden());
        }


        updatechildren();
    }

    public boolean isTicked()       { return !pTickbox.isHidden(); }

    public void tick(boolean state) { pTickbox.hide(!state); }

    public static Switch createFromXMLNode(XMLNode node)
    {
        float borderRadius = node.getFloatAttribute("border-radius", 0.0f);
        return new Switch(new ivec2(0,0), new ivec2(1,1), borderRadius);
    }
};