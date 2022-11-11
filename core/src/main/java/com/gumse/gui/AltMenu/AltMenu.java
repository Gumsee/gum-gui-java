package com.gumse.gui.AltMenu;

import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;

public class AltMenu extends RenderGUI
{
    private Box pBackground;

    public AltMenu(ivec2 pos, ivec2 size)
    {
        this.vPos = pos;
        this.vSize = size;
        this.setType("AltMenu");
    
        pBackground = new Box(new ivec2(0, 0), new ivec2(100, 100));
        pBackground.setSizeInPercent(true, true);
        pBackground.setColor(new vec4(0.14f, 0.14f, 0.14f, 1.0f));
        addElement(pBackground);
    }
    
    
    public void cleanup() 
    {
        //Gum::_delete(pBackground);
    }
    
    public void addEntry(AltMenuEntry entry)
    {
        int offset = 0;
        for(int i = 0; i < numChildren(); i++)
            offset += vChildren.get(i).getSize().x - 1;
    
        entry.setPosition(new ivec2(offset, 0));
        entry.getTextBox().setAlignment(TextBox.Alignment.CENTER);
        entry.getTextBox().setTextOffset(new ivec2(2,3));
        addGUI(entry);
    }
};