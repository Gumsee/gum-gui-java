package com.gumse.gui.AltMenu;

import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Debug;

public class AltMenuEntry extends RenderGUI
{
    private TextBox pTextBox;
    private Box pChildBorderBox;
    private boolean bIsOpen;
    private int iWidestChild;

    public interface AltMenuEntryCallback {
        void run();
    };

    private AltMenuEntryCallback pCallback = null;

    public AltMenuEntry(String title, AltMenuEntryCallback callback)
    {
        this.vPos = new ivec2(0,0);
        this.sType = "AltMenuEntry";
        this.bIsOpen = false;
        this.iWidestChild = 0;
        this.pCallback = callback;

        pTextBox = new TextBox(title, FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 100));
        pTextBox.setSizeInPercent(true, true);
        pTextBox.setTextSize(20);
        //pTextBox.setColor(vec4(0.26f, 0.26f, 0.26f, 1.0f));
        pTextBox.setColor(new vec4(0.14f, 0.14f, 0.14f, 1.0f));
        pTextBox.setTextColor(new vec4(0.76f, 0.76f, 0.76f, 1.0f));
        pTextBox.setAlignment(TextBox.Alignment.LEFT);
        pTextBox.setTextOffset(new ivec2(-5, 3));
        addElement(pTextBox);

        pChildBorderBox = new Box(new ivec2(0,0), new ivec2(0,0));
        pChildBorderBox.setColor(new vec4(0,0,0,0));
        pChildBorderBox.setBorderColor(new vec4(0.3f, 0.3f, 0.3f, 1.0f));
        pChildBorderBox.setBorderThickness(2.0f);
        addElement(pChildBorderBox);

        this.vSize = new ivec2(pTextBox.getTextSize().x + 30, 30);

        resize();
        reposition();
    }

    public void cleanup() 
    {
        /*Gum::_delete(pTextBox);
        Gum::_delete(pChildBorderBox);*/
    }

    private void close()
    {
        pTextBox.setColor(new vec4(0.14f, 0.14f, 0.14f, 1.0f));
        //pTextBox.setColor(vec4(0.26f, 0.26f, 0.26f, 1.0f));
        if(bIsOpen)
        {
            bIsOpen = false;
            if(pParent.getType().equals("AltMenu"))
                Mouse.setBusiness(false);
        }
        hideChildren(true);
    }

    public void update()
    {
        updatechildren();
        if(isMouseInside())
        {
            Mouse.setActiveHovering(true);
            if(isClicked())
            {
                if(!bIsOpen)
                {
                    bIsOpen = true;
                    if(pCallback != null)
                        pCallback.run();

                    pChildBorderBox.setSize(new ivec2(iWidestChild, numChildren() * getSize().y));

                    if(pParent.getType() == "AltMenu") { pChildBorderBox.setPosition(new ivec2(0, getSize().y)); }
                    else                                { pChildBorderBox.setPosition(new ivec2(getSize().x, 0)); }

                    if(pParent.getType().equals("AltMenu"))
                        Mouse.setBusiness(true);
                    hideChildren(false);
                    RenderGUI.clickedSomething(true);
                }
                else
                {
                    close();
                }
            }
        
            pTextBox.setColor(new vec4(0.2f, 0.2f, 0.2f, 1.0f));
        }
        else
        {
            close();
        }
    }

    public void render()
    {
        if(!bChildrenHidden)
        {
            for(int i = 0; i < numChildren(); i++)
                vChildren.get(i).render();
            pChildBorderBox.render();
        }

        pTextBox.render();
    }

    protected void updateOnSizeChange()
    {
        for(int i = 0; i < numChildren(); i++)
        {
            if(pParent.getType() == "AltMenuEntry")
                vChildren.get(i).setPosition(new ivec2(getSize().x, vChildren.get(i).getRelativePosition().y));
        }
    }

    public void addEntry(AltMenuEntry entry)
    {
        if(pParent == null)
            Debug.error("Add the Menu Entry to its parent first before adding subentries to it!");

        if(entry.getSize().x > iWidestChild)
            iWidestChild = entry.getSize().x;
        
        addGUI(entry);
        
        for(int i = 0; i < numChildren(); i++)
            vChildren.get(i).setSize(new ivec2(iWidestChild, getSize().y));

        if(pParent.getType() == "AltMenu") { entry.setPosition(new ivec2(0, numChildren() * getSize().y)); }
        else                                { entry.setPosition(new ivec2(getSize().x, (numChildren() - 1) * getSize().y)); }
        
    }

    public TextBox getTextBox()
    {
        return this.pTextBox;
    }
};