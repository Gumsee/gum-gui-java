package com.gumse.gui.AltMenu;

import java.util.ArrayList;

import com.gumse.gui.GUI;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;

public class AltMenu extends RenderGUI
{
    private Box pBackground;
    private ArrayList<RenderGUI> vEntries;

    //First add entries, then add anything else
    public AltMenu(ivec2 pos, ivec2 size)
    {
        this.vPos = pos;
        this.vSize = size;
        this.setType("AltMenu");
        this.vEntries = new ArrayList<>();
        this.bUpdateFromFirstToLast = true;
    
        pBackground = new Box(new ivec2(0, 0), new ivec2(100, 30));
        pBackground.setSizeInPercent(true, false);
        pBackground.setColor(GUI.getTheme().secondaryColor);
        addElement(pBackground);
    }
    
    
    public void cleanup() 
    {
        //Gum::_delete(pBackground);
    }

    public void renderextra()
    {
        for(int i = vEntries.size(); i < vChildren.size(); i++)
            vChildren.get(i).render();

        pBackground.render();
        for(int i = 0; i < vEntries.size(); i++)
            vEntries.get(i).render();
    }
    
    public void addEntry(AltMenuEntry entry)
    {
        int offset = 0;
        for(int i = 0; i < vEntries.size(); i++)
            offset += vEntries.get(i).getSize().x - 1;
    
        entry.setPosition(new ivec2(offset, 0));
        entry.getTextBox().setAlignment(TextBox.Alignment.CENTER);
        entry.getTextBox().setTextOffset(new ivec2(2,3));
        addGUI(entry);
        vEntries.add(entry);
    }
};