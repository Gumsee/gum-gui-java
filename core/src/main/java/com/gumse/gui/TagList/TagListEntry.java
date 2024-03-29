package com.gumse.gui.TagList;

import com.gumse.gui.GUI;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Primitives.Cross;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.system.io.Mouse;

public class TagListEntry <T> extends RenderGUI
{
    private TextBox pTextBox;
    private Cross pCross;
    private String sName;
    private T pUserPtr;
    private static final vec4 CROSS_COLOR = new vec4(0.7f, 0.22f, 0.22f, 1.0f);


    public TagListEntry(ivec2 pos, String tagstr, Font font, GUICallback removecallback, T userptr)
    {
        this.vPos.set(pos);
        this.vSize.set(new ivec2(0, 100));
        this.sName = tagstr;
        this.pUserPtr = userptr;


        pTextBox = new TextBox(tagstr, font, new ivec2(0, 0), new ivec2(100, 100));
        pTextBox.setSizeInPercent(true, true);
        pTextBox.setColor(GUI.getTheme().secondaryColor);
        pTextBox.setCornerRadius(new vec4(10));
        pTextBox.setAlignment(Alignment.LEFT);
        pTextBox.setTextOffset(new ivec2(-5, 0));
        addElement(pTextBox);

        pCross = new Cross(new ivec2(100, 50), new ivec2(15, 15));
        pCross.setOrigin(new ivec2(10, 4));
        pCross.setPositionInPercent(true, true);
        addElement(pCross);

        vActualSize.x = pTextBox.getTextSize().x + 25;

        onHover(null, Mouse.GUM_CURSOR_HAND);
        onEnter(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            {
                pTextBox.setColor(vec4.sub(GUI.getTheme().secondaryColor, 0.02f));
                pCross.setColor(CROSS_COLOR);
            }
        });
        onLeave(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            {
                pTextBox.setColor(GUI.getTheme().secondaryColor);
                pCross.setColor(GUI.getTheme().accentColor);
            }
        });

        TagListEntry<T> thisEntry = this;
        onClick(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            {
                if(removecallback != null)
                    removecallback.run(thisEntry);
            }
        });

        setSizeInPercent(false, true);
        reposition();
    }

    @Override
    protected void updateOnSizeChange()
    {
        pTextBox.setTextSize((int)(vActualSize.y * 0.9f));
        vActualSize.x = pTextBox.getTextSize().x + 25;
    }

    @Override
    protected void updateOnThemeChange() 
    {
        pTextBox.setColor(GUI.getTheme().secondaryColor);
        pCross.setColor(GUI.getTheme().accentColor);
    }

    
    //
    // Getter
    //
    public String getName() { return sName; }
    public T getUserptr()   { return pUserPtr; }
}