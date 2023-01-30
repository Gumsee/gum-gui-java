package com.gumse.gui.Basics;

import java.util.ArrayList;

import com.gumse.gui.GUI;
import com.gumse.gui.Basics.Switch.SwitchShape;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.system.filesystem.XML.XMLNode;

public class Radiobutton extends RenderGUI
{
    public interface OnSelectCallback
    {
        public void run(int index, String content);
    }

    private int iGapSize, iFontSize;
    private boolean bSingleSelectMode;
    private Font pFont, pSymbolFont;
    private SwitchShape iShape;
    private char cSymbol;


    public Radiobutton(ivec2 pos, int width, Font font, int fontsize)
    {
        this.vPos.set(pos);
        this.vSize.set(new ivec2(width, 0));
        this.sType             = "Radiobutton";
        this.pFont             = this.pSymbolFont = font;
        this.iShape            = SwitchShape.CIRCLE;
        this.iGapSize          = fontsize / 2;
        this.iFontSize         = fontsize;
        this.bSingleSelectMode = false;
        this.cSymbol           = '0';

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
        for(RenderGUI child : vChildren)
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
        for(RenderGUI child : vChildren)
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
        for(int i = 0; i < vChildren.size(); i++)
            if(((RadiobuttonOption)vChildren.get(i)).isSelected())
                retArr.add(i);

        return retArr;
    }

    public void singleSelect(boolean singleselect)
    {
        this.bSingleSelectMode = singleselect;
    }

    public boolean inSingleSelectMode()
    {
        return bSingleSelectMode;
    }

    public void select(int index)
    {
        ((RadiobuttonOption)vChildren.get(index)).select();
    }

    public void onSelect(OnSelectCallback callback)
    {
        for(RenderGUI child : vChildren)
            ((RadiobuttonOption)child).setCallback(callback);
    }

    public void setShape(SwitchShape shape)
    {
        iShape = shape;
    }

    public void setSymbol(char sym)
    {
        cSymbol = sym;
    }

    public RadiobuttonOption addOption(String name) { return addOption(name, "", ""); }
    public RadiobuttonOption addOption(String name, String localeid, String icon)
    {
        RadiobuttonOption option = new RadiobuttonOption(numChildren(), name, localeid, iShape, cSymbol, pFont, pSymbolFont, iFontSize);
        option.setPosition(new ivec2(0, 0));
        option.setSize(new ivec2(100, 30));
        option.setSizeInPercent(true, false);
        addGUI(option);
        updateOnSizeChange();

        return option;
    }

    public void setSymbolFont(Font font)
    {
        this.pSymbolFont = font;
    }

    public static Radiobutton createFromXMLNode(XMLNode node)
    {
        Radiobutton radiobuttongui = new Radiobutton(new ivec2(0,0), 1, FontManager.getInstance().getDefaultFont(), 20);
        return radiobuttongui;
    }
};