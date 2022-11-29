package com.gumse.gui.Basics;

import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Toolbox;

public class Button extends RenderGUI
{
    private TextBox backgroundBox;

    public interface ButtonCallback {
        void run();
    }

    private ButtonCallback pCallback = null;

    public Button(ivec2 pos, ivec2 size, String title, Font font)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.sType = "button";
        this.v4Color = new vec4(0.2f, 0.2f, 0.2f, 1.0f);

        backgroundBox = new TextBox(title, font, new ivec2(0), new ivec2(100, 100));
        backgroundBox.setSizeInPercent(true, true);
        backgroundBox.setAlignment(TextBox.Alignment.CENTER);
        addGUI(backgroundBox);

        resize();
        reposition();

        backgroundBox.setTextSize((int)(getSize().y * 0.9f));
    }

    public void cleanup() 
    {
        //Gum::_delete(backgroundBox);
    }

    public void update()
    {
        if(backgroundBox.isMouseInside())
        {
            Mouse.setActiveHovering(true);
            Window.CurrentlyBoundWindow.getMouse().setCursor(Mouse.GUM_CURSOR_HAND);
            backgroundBox.setColor(vec4.sub(v4Color, new vec4(0.01f)));
            if(isHoldingLeftClick())
            {
                backgroundBox.setColor(vec4.sub(v4Color, new vec4(0.03f)));
            }
            if(!Mouse.isBusy() && isClicked())
            {
                if(pCallback != null)
                {
                    pCallback.run();
                }
            }
        }
        else
        {
            backgroundBox.setColor(v4Color);
        }
    }

    public void setCallbackFunction(ButtonCallback func) { this.pCallback = func; }
    //void setTexture(Texture *newtex)                   { this.backgroundBox.setTexture(newtex); }
    public void setSecondColor(vec4 col)                 { this.backgroundBox.getBox().setSecondColor(col); }
    public void setHasGradient(boolean val)              { this.backgroundBox.getBox().setHasGradient(val); }
    public void setGradientDirectionRight(boolean val)   { this.backgroundBox.getBox().setGradientDirectionRight(val); }
    public void setCornerRadius(vec4 radius)             { this.backgroundBox.getBox().setCornerRadius(radius); }

    //Texture* getTexture()                                   { return this.backgroundBox.getBox().getTexture(); }
    public vec4 getSecondColor()                         { return this.backgroundBox.getBox().getSecondColor(); }
    public TextBox getBox()                              { return this.backgroundBox; }

    public static Button createFromXMLNode(XMLNode node)
    {
        String fontName = node.getAttribute("font");
        Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());

        int fontsize     = node.getIntAttribute("fontsize", 12);
        Button retbutton = new Button(new ivec2(0,0), new ivec2(1,1), "", font);
        retbutton.getBox().setTextSize(fontsize);
        

        return retbutton;
    }
};