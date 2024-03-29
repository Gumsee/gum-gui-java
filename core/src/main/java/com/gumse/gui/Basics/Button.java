package com.gumse.gui.Basics;

import com.gumse.gui.GUI;
import com.gumse.gui.Locale;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
import com.gumse.maths.*;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Mouse;
import com.gumse.textures.Texture;

public class Button extends RenderGUI
{
    private TextBox backgroundBox;

    public Button(ivec2 pos, ivec2 size, String title, Font font)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.sType = "button";

        backgroundBox = new TextBox(title, font, new ivec2(0), new ivec2(100, 100));
        backgroundBox.setSizeInPercent(true, true);
        backgroundBox.setAlignment(TextBox.Alignment.CENTER);
        backgroundBox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
        backgroundBox.getBox().setCornerRadius(GUI.getTheme().cornerRadius);
        backgroundBox.setTextSize(size.y - 5);
        addGUI(backgroundBox);

        onLeave(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            {
                backgroundBox.setColor(getColor(GUI.getTheme().primaryColor));
            }
        });

        onHover(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            {
                backgroundBox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), 0.02f));
                if(isHoldingLeftClick())
                {
                    backgroundBox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), 0.05f));
                }
            }
        }, Mouse.GUM_CURSOR_HAND);

        resize();
        reposition();
    }

    @Override
    protected void updateOnTitleChange() 
    {
        backgroundBox.setString(sTitle);
    }

    @Override
    protected void updateOnColorChange() 
    {
        backgroundBox.setColor(getColor(GUI.getTheme().primaryColor));    
    }

    @Override
    protected void updateOnThemeChange() 
    {
        backgroundBox.setColor(getColor(GUI.getTheme().primaryColor));
        backgroundBox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
        backgroundBox.getBox().setCornerRadius(getCornerRadius());
        if(!sLocaleID.isEmpty())
            setTitle(Locale.getCurrentLocale().getString(sLocaleID));
    }

    public void setTexture(Texture newtex)               { this.backgroundBox.setTexture(newtex); }
    public void setSecondColor(vec4 col)                 { this.backgroundBox.getBox().setSecondColor(col); }
    public void setHasGradient(boolean val)              { this.backgroundBox.getBox().setHasGradient(val); }
    public void setGradientDirectionRight(boolean val)   { this.backgroundBox.getBox().setGradientDirectionRight(val); }
    public void setCornerRadius(vec4 radius)             { this.v4CornerRadius = radius; this.backgroundBox.getBox().setCornerRadius(radius); }

    public Texture getTexture()                          { return this.backgroundBox.getBox().getTexture(); }
    public vec4 getSecondColor()                         { return this.backgroundBox.getBox().getSecondColor(); }
    public TextBox getBox()                              { return this.backgroundBox; }

    
    public static XMLGUICreator createFromXMLNode()
    {
        return (XMLNode node) -> { 
            String fontName = node.getAttribute("font");
            Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());

            int fontsize     = node.getIntAttribute("fontsize", 0);
            Button retbutton = new Button(new ivec2(0,0), new ivec2(1,1), "", font);
            if(fontsize > 0)
                retbutton.getBox().setTextSize(fontsize);
            

            return retbutton;
        };
    }
};