package com.gumse.gui.Basics;

import com.gumse.gui.Basics.Radiobutton.OnSelectCallback;
import com.gumse.gui.Basics.Switch.SwitchShape;
import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.system.io.Mouse;

public class RadiobuttonOption extends RenderGUI
{
    private Switch pSwitch;
    private TextBox pTextBox;
    private OnSelectCallback pCallback;
    private int iFontSize;

    public RadiobuttonOption(int index, String str, String localeid, SwitchShape shape, char symbol, Font font, Font symbolfont, int fontsize)
    {
        this.sType = "RadiobuttonOption";
        this.pCallback = null;
        this.iFontSize = fontsize;

        pSwitch = new Switch(new ivec2(0, 0), new ivec2(fontsize), 0);
        pSwitch.setShape(shape);
        pSwitch.setChar(symbol);
        pSwitch.tick(false);
        pSwitch.setCharFont(symbolfont);
        pSwitch.onTick((boolean ticked) -> {
            select();
            if(pCallback != null)
                pCallback.run(index, str);
        });
        addElement(pSwitch);

        int xoffset = fontsize * 2;
        pTextBox = new TextBox(str, font, new ivec2(xoffset, 0), new ivec2(vActualSize.x - xoffset, 30));
        pTextBox.setLocaleID(localeid);
        pTextBox.setTextSize(fontsize);
        pTextBox.setAutoInsertLinebreaks(true);
        pTextBox.setAlignment(Alignment.LEFT);
        pTextBox.setSize(new ivec2(vActualSize.x - xoffset, pTextBox.getText().getSize().y));
        pTextBox.getBox().hide(true);
        pTextBox.onClick((RenderGUI gui) -> {
            select();
            if(pCallback != null)
                pCallback.run(index, str);
        });
        pTextBox.onHover(null, Mouse.GUM_CURSOR_HAND);
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
        if(((Radiobutton)pParent).inSingleSelectMode())
        {
            for(RenderGUI option : pParent.getChildren())
            {
                if(option.getType().equals("RadiobuttonOption"))
                {
                    ((RadiobuttonOption)option).unselect();
                }
            }
        }
        pSwitch.tick(!isSelected());
    }

    public void unselect()
    {
        pSwitch.tick(false);
    }

    public void setCallback(OnSelectCallback callback)
    {
        this.pCallback = callback;
    }

    public TextBox getTextBox()
    {
        return this.pTextBox;
    }
}