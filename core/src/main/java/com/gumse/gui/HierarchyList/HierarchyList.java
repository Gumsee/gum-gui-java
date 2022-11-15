package com.gumse.gui.HierarchyList;

import com.gumse.gui.GUIShader;
import com.gumse.gui.Basics.Scroller;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.system.Window;

public class HierarchyList extends RenderGUI
{
    private TextBox pTitleBox;
    private Scroller pScroller;
    private Box pBackground;
    private HierarchyListEntry pSelectedEntry;
    private Box pSelectedEntryIndicator;
    private HierarchyListEntry pRootEntry;


    public HierarchyList(ivec2 pos, ivec2 size, String title, String rootname)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.sType = "HierarchyList";

        this.pSelectedEntry = null;

        pTitleBox = new TextBox(title, FontManager.getInstance().getDefaultFont(), new ivec2(0,0), new ivec2(100, 30));
        pTitleBox.setAlignment(TextBox.Alignment.LEFT);
        pTitleBox.setTextSize(20);
        pTitleBox.setTextOffset(new ivec2(-10, 0));
        pTitleBox.setSizeInPercent(true, false);
        pTitleBox.setColor(new vec4(0.1f, 0.1f, 0.1f, 1));
        pTitleBox.setTextColor(new vec4(0.76f, 0.76f, 0.76f, 1));

        pBackground = new Box(new ivec2(0, 0), new ivec2(100, 100));
        pBackground.setSizeInPercent(true, true);
        pBackground.setColor(new vec4(0,1,0,1));

        pScroller = new Scroller(new ivec2(0, pTitleBox.getSize().y), new ivec2(100, 100));
        pScroller.setSizeInPercent(true, true);
        pScroller.setMargin(new ivec2(0, -pTitleBox.getSize().y));
        //pScroller.setStepSize(30);

        pSelectedEntryIndicator = new Box(new ivec2(0,0), new ivec2(100, 30));
        pSelectedEntryIndicator.setSizeInPercent(true, false);
        pSelectedEntryIndicator.setColor(new vec4(0.34f, 0.5f, 0.76f, 1));
        pSelectedEntryIndicator.hide(true);
        pScroller.addGUI(pSelectedEntryIndicator);


        pRootEntry = new HierarchyListEntry(rootname, this, null);
        pRootEntry.setPosition(new ivec2(10, 0));
        pRootEntry.shouldKeepTrackOfBoundingbox(true);
        pScroller.addGUI(pRootEntry);
        pScroller.setMainChildContainer(pRootEntry);

        addElement(pBackground);
        addElement(pScroller);
        addElement(pTitleBox);

        resize();
        reposition();
    }

    public void cleanup()
    {
        pScroller.destroyChildren();
        /*for(RenderGUI* gui : vElements)
            Gum::_delete(gui);*/
    }

    public void update()
    {
        updatechildren();

        if(pSelectedEntry != null)
        {
            pSelectedEntryIndicator.setPosition(new ivec2(0, pSelectedEntry.getPosition().y - pScroller.getPosition().y));
        }
    }

    public void render()
    {
        GUIShader.getStripesShaderProgram().use();
        GUIShader.getStripesShaderProgram().LoadUniform("transmat", pBackground.getTransformation());
        GUIShader.getStripesShaderProgram().LoadUniform("orthomat", Window.CurrentlyBoundWindow.getScreenMatrix());
        GUIShader.getStripesShaderProgram().LoadUniform("patternoffset", (float)vActualPos.y + (float)pScroller.getOffset());
        GUIShader.getStripesShaderProgram().LoadUniform("lineheight", 30.0f);
        GUIShader.getStripesShaderProgram().LoadUniform("color1", new vec4(0.16f, 0.16f, 0.16f, 1.0f));
        GUIShader.getStripesShaderProgram().LoadUniform("color2", new vec4(0.18f, 0.18f, 0.18f, 1.0f));
        pBackground.renderCustom();
        GUIShader.getStripesShaderProgram().unuse();
            

        pScroller.render();
        pTitleBox.render();
    }

    public void addEntry(HierarchyListEntry entry)
    {
        pRootEntry.addEntry(entry);
        pScroller.updateContent();
    }

    public void selectEntry(HierarchyListEntry entry)
    {
        this.pSelectedEntry = entry;
        pSelectedEntryIndicator.hide(entry == null);
    }

    public HierarchyListEntry getRootEntry()
    {
        return this.pRootEntry;
    }

    public HierarchyListEntry getSelectedEntry()
    {
        return this.pSelectedEntry;
    }
};