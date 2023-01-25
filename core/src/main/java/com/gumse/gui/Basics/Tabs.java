package com.gumse.gui.Basics;

import org.lwjgl.opengl.GL11;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.basics.SmoothFloat;
import com.gumse.gui.GUI;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Output;

public class Tabs extends RenderGUI
{
    private RenderGUI pActiveTab;
    private Box pBackground;
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
    }

    public void updateextra()
    {
        if(pBackground.numChildren() > 0)
        {
            TextBox lastTab = (TextBox)pBackground.getChild(pBackground.numChildren() - 1);
            if(pBackground.isMouseInsideSkipChildren())
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
        for(int i = 0; i < vElements.size(); i++)
            vElements.get(i).setColor(GUI.getTheme().primaryColorShade);

        if(getTab(pActiveTab.getTitle()) != null)
            getTab(pActiveTab.getTitle()).setColor(GUI.getTheme().primaryColorShade);
    }

    public void renderextra()
    {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(vActualPos.x, Framebuffer.CurrentlyBoundFramebuffer.getSize().y - vActualPos.y - vActualSize.y + vTabSize.y, vActualSize.x, vActualSize.y);
        renderchildren();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

    }

    public void addGUIToTab(RenderGUI gui, String tabName)
    {
        RenderGUI tab = getTabContent(tabName);
        if(tab != null)
            tab.addGUI(gui);
    }

    public void addTab(final String name, final boolean active)
    {
        RenderGUI tabContent = new RenderGUI();
        tabContent.setTitle(name);
        tabContent.setSize(new ivec2(100, 100));
        tabContent.setSizeInPercent(true, true);
        addElement(tabContent);

        int xoffset = 0;
        for(RenderGUI tab : pBackground.getChildren())
            xoffset += tab.getSize().x;

        TextBox tabSelector = new TextBox(name, FontManager.getInstance().getDefaultFont(), new ivec2(xoffset, 0), vTabSize);
        tabSelector.setSize(new ivec2(tabSelector.getTextSize().x + 30, vTabSize.y));
        tabSelector.setCornerRadius(new vec4(0,0,0,0));
        tabSelector.onClick((RenderGUI thisselector) -> {
            setActiveTab(name);
        });
        tabSelector.onHover(null, Mouse.GUM_CURSOR_HAND);
        pBackground.addGUI(tabSelector);

        if(active)
            setActiveTab(name);
    }

    public void setActiveTab(String tabname)
    {
        TextBox tab = getTab(tabname);
        RenderGUI content = getTabContent(tabname);
        if(tab != null)
        {
            //Disable all other tabs
            for(RenderGUI othertab : pBackground.getChildren())
                othertab.setColor(GUI.getTheme().primaryColor);

            for(RenderGUI othercontent : vElements)
            {
                if(!othercontent.getType().equals("box"))
                    othercontent.hide(true);
            }

            tab.setColor(GUI.getTheme().primaryColorShade);
            this.pActiveTab = content;
            content.hide(false);
        }
    }

    public boolean isActiveTab(String tabname)
    {
        RenderGUI pFoundTab = getTabContent(tabname);
        return pFoundTab != null && pActiveTab == pFoundTab;
    }

    private TextBox getTab(String name)
    {
        for(RenderGUI child : pBackground.getChildren())
        {
            if(child.getTitle().equals(name))
                return (TextBox)child;
        }
        return null;
    }

    public RenderGUI getTabContent(String name)
    {
        for(RenderGUI child : vElements)
        {
            if(child.getTitle().equals(name))
                return child;
        }
        return null;
    }

    public RenderGUI getActiveTabContent() { return pActiveTab; }
    public int numTabs()                   { return vElements.size(); }


    public static XMLGUICreator createFromXMLNode() 
    {
        return (XMLNode node) -> { 
            ivec2 tabSize = node.getIvec2Attribute("tabsize", new ivec2(100, 30));
            return new Tabs(new ivec2(0,0), new ivec2(1,1), tabSize);
        };
    };
};