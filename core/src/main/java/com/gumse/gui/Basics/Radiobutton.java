package com.gumse.gui.Basics;

import java.util.ArrayList;

import com.gumse.gui.GUI;
import com.gumse.gui.Basics.Switch.Shape;
import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.system.filesystem.XML.XMLNode;

public class Radiobutton extends RenderGUI
{
    class Option extends RenderGUI
    {
        private Switch pSwitch;
        private TextBox pTextBox;
        private int iFontSize;

        public Option(String str, Font font, int fontsize)
        {
            this.sType = "RadiobuttonOption";
            this.iFontSize = fontsize;

            pSwitch = new Switch(new ivec2(0, 0), new ivec2(fontsize), 0);
            pSwitch.setShape(Shape.CIRCLE);
            pSwitch.tick(false);
            addElement(pSwitch);

            int xoffset = iFontSize * 2;
            pTextBox = new TextBox(str, font, new ivec2(xoffset, 0), new ivec2(vActualSize.x - xoffset, 30));
            pTextBox.setTextSize(fontsize);
            pTextBox.setAutoInsertLinebreaks(true);
            pTextBox.setAlignment(Alignment.LEFT);
            pTextBox.setSize(new ivec2(vActualSize.x - xoffset, pTextBox.getText().getSize().y));
            pTextBox.getBox().hide(true);
            addElement(pTextBox);

            vSize.y = pTextBox.getText().getSize().y;
        }

        @Override
        protected void updateOnSizeChange()
        {
            int xoffset = iFontSize * 2;
            pTextBox.setSize(new ivec2(vActualSize.x - xoffset, pTextBox.getText().getSize().y));
            vSize.y = pTextBox.getText().getSize().y;
        }

        public boolean isSelected()
        {
            return pSwitch.isTicked();
        }
    }

    private int iGapSize;

    public Radiobutton(ivec2 pos, int fontsize, int width, Font font, String[] options)
    {
        this.sType = "Radiobutton";
        this.vPos.set(pos);
        this.iGapSize = fontsize / 2;

        int maxheight = 0;
        for(int i = 0; i < options.length; i++)
        {
            int ypos = maxheight;
            Option option = new Option(options[i], font, fontsize);
            option.setPosition(new ivec2(0, ypos));
            option.setSize(new ivec2(100, 30));
            option.setSizeInPercent(true, false);
            addElement(option);

            maxheight += option.getSize().y + iGapSize;
        }

        this.vSize.set(new ivec2(width, maxheight));

        resize();
        reposition();
    }


    public void cleanup()
    {
        //Gum::_delete(pTitle);
    }

    @Override
    protected void updateOnColorChange()
    {
        for(RenderGUI child : vElements)
        {
            if(child.getType() == "TextBox")
                ((TextBox)child).getText().setColor(this.getColor(GUI.getTheme().textColor));
            else
                child.setColor(this.getColor(GUI.getTheme().primaryColor));
        }
    }

    @Override
    protected void updateOnSizeChange()
    {
        int maxheight = 0;
        for(RenderGUI child : vElements)
        {
            child.resize();
            child.setPosition(new ivec2(0, maxheight));
            maxheight += child.getSize().y + iGapSize;
        }

        this.vSize.y = maxheight;
    }

    public ArrayList<Integer> getSelected()
    {
        ArrayList<Integer> retArr = new ArrayList<>();
        for(int i = 0; i < vElements.size(); i++)
            if(((Option)vElements.get(i)).isSelected())
                retArr.add(i);

        return retArr;
    }

    public static Radiobutton createFromXMLNode(XMLNode node)
    {
        Radiobutton radiobuttongui = new Radiobutton(new ivec2(0,0), 1, 20, FontManager.getInstance().getDefaultFont(), new String[] {});
        return radiobuttongui;
    }
};