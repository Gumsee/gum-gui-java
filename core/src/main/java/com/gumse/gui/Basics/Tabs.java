package com.gumse.gui.Basics;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.gumse.basics.SmoothFloat;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.tools.Toolbox;

public class Tabs extends RenderGUI
{
    private TextBox pActiveTab;
    private Box pBackground;
    private ArrayList<TextBox> vTabs;
    private ivec2 vTabSize;
    private vec4 v4ActiveColor, v4InactiveColor;
    private SmoothFloat sfOffset;


    public Tabs(ivec2 pos, ivec2 size, ivec2 tabsize)
    {
        this.sType = "Tabs";
        this.vPos.set(pos);
        this.vSize.set(size);
        this.vTabSize = tabsize;
        this.v4ActiveColor = new vec4(0.14f,0.14f,0.14f, 1.0f);
        this.v4InactiveColor = new vec4(0.4f,0.4f,0.4f, 1.0f);
        this.pActiveTab = null;
        this.sfOffset = new SmoothFloat();
        this.vTabs = new ArrayList<>();

        pBackground = new Box(new ivec2(0,0), new ivec2(100, tabsize.y));
        pBackground.setColor(new vec4(0.25f,0.25f,0.25f,1.0f));
        pBackground.setSizeInPercent(true, false);
        pBackground.setOrigin(new ivec2(0, tabsize.y));
        addElement(pBackground);

        vMargin.y = -tabsize.y;
        vOrigin.y = -tabsize.y;

        sfOffset.setMin(0.0f);


        resize();
        reposition();
    }

    public void cleanup()
    {
        pBackground.destroyChildren();
        //Gum::_delete(pBackground);
    }

    public void update()
    {
        for(int i = 0; i < vTabs.size(); i++)
        {
            if(vTabs.get(i).isClicked())
            {
                if(pActiveTab != null)
                {
                    pActiveTab.setColor(v4InactiveColor);
                    pActiveTab.hideChildren(true);
                }
                vTabs.get(i).setColor(v4ActiveColor);
                vTabs.get(i).hideChildren(false);
                pActiveTab = vTabs.get(i);
            }
        }

        TextBox lastTab = vTabs.get(vTabs.size() - 1);
        if(lastTab.getPosition().x + lastTab.getSize().x > vActualSize.x &&
            Toolbox.checkPointInBox(Window.CurrentlyBoundWindow.getMouse().getPosition(), new bbox2i(ivec2.sub(vActualPos, new ivec2(0, vTabSize.y)), new ivec2(vActualSize.x, vTabSize.y))))
        {
            sfOffset.setMax(lastTab.getRelativePosition().x + lastTab.getSize().x - vActualSize.x + lastTab.getOrigin().x);
            sfOffset.increaseTarget(-50 * Window.CurrentlyBoundWindow.getMouse().getMouseWheelState());
        }

        if(sfOffset.update())
        {
            for(int i = 0; i < pBackground.numChildren(); i++)
            {
                RenderGUI elem = pBackground.getChild(i);
                elem.setOrigin(new ivec2((int)sfOffset.get(), 0));
            }
        }
        updatechildren();
    }

    public boolean updateToolTip()
    {
        if(isMouseInside())
        {
            for(int i = 0; i < pActiveTab.numChildren(); i++) 
            {
                boolean showToolTip = true;
                if(pActiveTab.getChild(i).updateToolTip())
                {
                    showToolTip = false;
                }

                //if(showToolTip)
                //    GumEngine::GUIS.showToolTip(sToolTip);
            }
            return true;
        }
        return false;
    }


    public void render()
    {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(vActualPos.x, Window.CurrentlyBoundWindow.getSize().y - vActualPos.y - vActualSize.y + vTabSize.y, vActualSize.x, vActualSize.y);
        pBackground.render();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void addGUIToTab(RenderGUI gui, String tabName)
    {
        TextBox tab = getTab(tabName);
        if(tab != null)
        {
            tab.addGUI(gui);
            gui.setParent(this); //Overwrite parent
            gui.reposition();
            gui.resize();
        }
    }

    public void addTab(String name, boolean active)
    {
        float width = 0.0f;
        for(TextBox tab : vTabs)
            width += tab.getSize().x;

        TextBox tab = new TextBox(name, FontManager.getInstance().getDefaultFont(), new ivec2((int)width, 0), vTabSize);
        //tab.setTextSize(vTabSize.y - 10);
        if(active)
        {
            tab.setColor(v4ActiveColor);
            tab.hideChildren(false);
            pActiveTab = tab;
        }
        else
        {
            tab.setColor(v4InactiveColor);
            tab.hideChildren(true);
        }
        tab.setSize(new ivec2(tab.getTextSize().x + 30, vTabSize.y));
        //tab.setTextOffset(new ivec2(5, 3));
        pBackground.addGUI(tab);
        vTabs.add(tab);
    }

    public void setActiveTab(String tabname)
    {
        TextBox pFoundTab = getTab(tabname);
        if(pFoundTab != null)
            this.pActiveTab = pFoundTab;
    }

    public boolean isActiveTab(String tabname)
    {
        TextBox pFoundTab = getTab(tabname);
        return pFoundTab != null && pActiveTab == pFoundTab;
    }

    public TextBox getTab(String name)
    {
        for(int i = 0; i < vTabs.size(); i++)
            if(vTabs.get(i).getTitle().toString() == name)
                return vTabs.get(i);
        return null;
    }

    public TextBox getActiveTab()    { return pActiveTab; }
    public int numTabs()             { return vTabs.size(); }

    /*public static Tabs createFromXMLNode(XMLNode node)
    {
        ivec2 tabSize = node.mAttributes["tabsize"] != "" ? (ivec2)Tools::StringToVec2(node.mAttributes["tabsize"]) : ivec2(100, 30);
        return new Tabs(ivec2(0,0), ivec2(1,1), tabSize);
    }*/
};