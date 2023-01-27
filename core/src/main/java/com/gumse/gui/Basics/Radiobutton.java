package com.gumse.gui.Basics;

import java.util.ArrayList;

import com.gumse.gui.GUI;
import com.gumse.gui.Basics.Switch.OnSwitchTicked;
import com.gumse.gui.Basics.Switch.SwitchShape;
import com.gumse.gui.Basics.TextBox.Alignment;
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

    class Option extends RenderGUI
    {
        private Switch pSwitch;
        private TextBox pTextBox;
        private OnSelectCallback pCallback;

        public Option(int index, String str, String localeid)
        {
            this.sType = "RadiobuttonOption";
            this.pCallback = null;

            pSwitch = new Switch(new ivec2(0, 0), new ivec2(iFontSize), 0);
            pSwitch.setShape(iShape);
            pSwitch.setChar(cSymbol);
            pSwitch.tick(false);
            pSwitch.setCharFont(pSymbolFont);
            pSwitch.onTick(new OnSwitchTicked() {
                @Override public void run(boolean ticked) 
                {
                    select();
                    if(pCallback != null)
                        pCallback.run(index, str);
                }
            });
            addElement(pSwitch);

            int xoffset = iFontSize * 2;
            pTextBox = new TextBox(str, pFont, new ivec2(xoffset, 0), new ivec2(vActualSize.x - xoffset, 30));
            pTextBox.setLocaleID(localeid);
            pTextBox.setTextSize(iFontSize);
            pTextBox.setAutoInsertLinebreaks(true);
            pTextBox.setAlignment(Alignment.LEFT);
            pTextBox.setSize(new ivec2(vActualSize.x - xoffset, pTextBox.getText().getSize().y));
            pTextBox.getBox().hide(true);
            pTextBox.onClick(new GUICallback() {
                @Override public void run(RenderGUI gui) 
                {
                    select();
                    if(pCallback != null)
                        pCallback.run(index, str);
                }
            });
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

        public void select()
        {
            if(((Radiobutton)pParent).bSingleSelectMode)
            {
                for(RenderGUI option : pParent.getChildren())
                {
                    if(option.getType().equals("RadiobuttonOption"))
                    {
                        ((Option)option).unselect();
                    }
                }
            }
            pSwitch.tick(true);
        }

        public void unselect()
        {
            pSwitch.tick(false);
        }

        public void setCallback(OnSelectCallback callback)
        {
            this.pCallback = callback;
        }
    }

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
            if(((Option)vChildren.get(i)).isSelected())
                retArr.add(i);

        return retArr;
    }

    public void singleSelect(boolean singleselect)
    {
        this.bSingleSelectMode = singleselect;
    }

    public void select(int index)
    {
        ((Option)vChildren.get(index)).select();
    }

    public void onSelect(OnSelectCallback callback)
    {
        for(RenderGUI child : vChildren)
            ((Option)child).setCallback(callback);
    }

    public void setShape(SwitchShape shape)
    {
        iShape = shape;
    }

    public void setSymbol(char sym)
    {
        cSymbol = sym;
    }

    public void addOption(String name) { addOption(name, "", ""); }
    public void addOption(String name, String localeid, String icon)
    {
        Option option = new Option(numChildren(), name, localeid);
        option.setPosition(new ivec2(0, 0));
        option.setSize(new ivec2(100, 30));
        option.setSizeInPercent(true, false);
        addGUI(option);
        updateOnSizeChange();
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