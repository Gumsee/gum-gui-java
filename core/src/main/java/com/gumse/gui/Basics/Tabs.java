package com.gumse.gui.Basics;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.basics.SmoothFloat;
import com.gumse.gui.GUI;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.tools.Toolbox;

public class Tabs extends RenderGUI
{
    private TextBox pActiveTab;
    private Box pBackground;
    private ArrayList<TextBox> vTabs;
    private ivec2 vTabSize;
    private SmoothFloat sfOffset;


    public Tabs(ivec2 pos, ivec2 size, ivec2 tabsize)
    {
        this.sType = "Tabs";
        this.vPos.set(pos);
        this.vSize.set(size);
        this.vTabSize = tabsize;
        this.pActiveTab = null;
        this.sfOffset = new SmoothFloat();
        this.vTabs = new ArrayList<>();

        pBackground = new Box(new ivec2(0,0), new ivec2(100, tabsize.y));
        pBackground.setColor(GUI.getTheme().secondaryColor);
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

    public void updateextra()
    {
        if(vTabs.size() > 0)
        {
            for(int i = 0; i < vTabs.size(); i++)
            {
                if(vTabs.get(i).isClicked())
                {
                    if(pActiveTab != null)
                    {
                        pActiveTab.setColor(GUI.getTheme().primaryColor);
                        pActiveTab.hideChildren(true);
                    }
                    vTabs.get(i).setColor(GUI.getTheme().primaryColorShade);
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
        }
    }

    @Override
    protected void updateOnThemeChange() 
    {
        pBackground.setColor(GUI.getTheme().secondaryColor);
        for(int i = 0; i < vTabs.size(); i++)
            vTabs.get(i).setColor(GUI.getTheme().primaryColorShade);

        if(pActiveTab != null)
            pActiveTab.setColor(GUI.getTheme().primaryColorShade);
    }

    public void renderextra()
    {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(vActualPos.x, Framebuffer.CurrentlyBoundFramebuffer.getSize().y - vActualPos.y - vActualSize.y + vTabSize.y, vActualSize.x, vActualSize.y);
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
            tab.setColor(GUI.getTheme().primaryColorShade);
            tab.hideChildren(false);
            pActiveTab = tab;
        }
        else
        {
            tab.setColor(GUI.getTheme().primaryColor);
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

    public static Tabs createFromXMLNode(XMLNode node)
    {
        ivec2 tabSize = node.getIvec2Attribute("tabsize", new ivec2(100, 30));
        return new Tabs(new ivec2(0,0), new ivec2(1,1), tabSize);
    }
};