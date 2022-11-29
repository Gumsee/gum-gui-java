package com.gumse.gui.Basics;

import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.system.filesystem.XML.XMLNode;

public class Group extends RenderGUI
{
    private Text pTitle;

    public Group(ivec2 pos, ivec2 size)
    {
        this.sType = "Group";
        this.vPos.set(pos);
        this.vSize.set(size);
        this.pTitle = null;

        resize();
        reposition();
    }


    public void cleanup()
    {
        //Gum::_delete(pTitle);
    }

    protected void updateOnTitleChange()
    {
        if(pTitle == null)
        {
            pTitle = new Text("", FontManager.getInstance().getDefaultFont(), new ivec2(0, -25), 0);
            pTitle.setCharacterHeight(20.0f);
            pTitle.setColor(this.getColor());
            addElement(pTitle);
        }

        pTitle.setString(this.getTitle());
    }

    protected void updateOnColorChange()
    {
        if(pTitle != null)
        {
            pTitle.setColor(this.getColor());
        }
    }

    protected void updateOnCornerRadiusChange()  { updateRadius(); }
    protected void updateOnAddGUI(RenderGUI gui) { updateRadius(); }

    private void updateRadius()
    {
        if(numChildren() > 1)
        {
            vChildren.get(0).setCornerRadius(new vec4(v4CornerRadius.x, v4CornerRadius.y, 0.0f, 0.0f));
            for(int i = 1; i < numChildren() - 1; i++)
            {
                vChildren.get(i).setCornerRadius(new vec4(0.0f));
            }
            vChildren.get(numChildren() - 1).setCornerRadius(new vec4(0.0f, 0.0f, v4CornerRadius.z, v4CornerRadius.w));
        }
        else if(numChildren() == 1)
        {
            vChildren.get(0).setCornerRadius(v4CornerRadius);
        }
    }


    public static Group createFromXMLNode(XMLNode node)
    {
        Group groupgui = new Group(new ivec2(0,0), new ivec2(1,1));
        return groupgui;
    }
};