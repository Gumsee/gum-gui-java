package com.gumse.gui.HierarchyList;

import java.util.List;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.gui.GUIShader;
import com.gumse.gui.Basics.Scroller;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Basics.Switch.OnSwitchTicked;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;

public class HierarchyList <T> extends RenderGUI
{
    private TextBox pTitleBox;
    private Scroller pScroller;
    private Box pBackground;
    private HierarchyListEntry<T> pRootEntry;
    private HierarchyListEntry<T> pSelectedEntry;
    private Box pSelectedEntryIndicator;
    private boolean bEditable, bSelectable;
    private OnSwitchTicked pTickCallback;
    private GUICallback pEntryClickCallback;


    public HierarchyList(ivec2 pos, ivec2 size, String title, String rootname, boolean editable, boolean selectable, String localeid)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.sType = "HierarchyList";
        this.pSelectedEntry = null;
        this.bEditable = editable;
        this.bSelectable = selectable;

        pTitleBox = new TextBox(title, FontManager.getInstance().getDefaultFont(), new ivec2(0,0), new ivec2(100, 30));
        pTitleBox.setAlignment(TextBox.Alignment.LEFT);
        pTitleBox.setTextSize(20);
        pTitleBox.setTextOffset(new ivec2(-10, 0));
        pTitleBox.setSizeInPercent(true, false);
        pTitleBox.setColor(GUI.getTheme().primaryColorShade);
        pTitleBox.setLocaleID(localeid);

        pBackground = new Box(new ivec2(0, 0), new ivec2(100, 100));
        pBackground.setSizeInPercent(true, true);
        pBackground.setColor(new vec4(0,1,0,1));

        pScroller = new Scroller(new ivec2(0, pTitleBox.getSize().y), new ivec2(100, 100));
        pScroller.setSizeInPercent(true, true);
        pScroller.setMargin(new ivec2(0, -pTitleBox.getSize().y));
        //pScroller.setStepSize(30);

        pSelectedEntryIndicator = new Box(new ivec2(0,0), new ivec2(100, 30));
        pSelectedEntryIndicator.setSizeInPercent(true, false);
        pSelectedEntryIndicator.setColor(GUI.getTheme().accentColor);
        pSelectedEntryIndicator.hide(true);
        pScroller.addGUI(pSelectedEntryIndicator);


        pRootEntry = new HierarchyListEntry<T>(rootname, this, null);
        pRootEntry.setPosition(new ivec2(10, 0));
        pRootEntry.shouldKeepTrackOfBoundingbox(true);
        pRootEntry.setMargin(new ivec2(-30, 0));
        pScroller.addGUI(pRootEntry);
        pScroller.setMainChildContainer(pRootEntry);

        addElement(pBackground);
        addElement(pScroller);
        addElement(pTitleBox);

        resize();
        reposition();
    }

    public void reset()
    {
        pRootEntry.destroyChildren();
    }

    public void updateextra()
    {
        if(pSelectedEntry != null)
        {
            pSelectedEntryIndicator.setPosition(new ivec2(0, pSelectedEntry.getPosition().y - pScroller.getPosition().y));
        }
    }

    @Override
    protected void updateOnThemeChange() 
    {
        pTitleBox.setColor(GUI.getTheme().primaryColorShade);
        pSelectedEntryIndicator.setColor(GUI.getTheme().accentColor);
    }

    public void renderextra()
    {
        GUIShader.getStripesShaderProgram().use();
        GUIShader.getStripesShaderProgram().loadUniform("transmat", pBackground.getTransformation());
        GUIShader.getStripesShaderProgram().loadUniform("orthomat", Framebuffer.CurrentlyBoundFramebuffer.getScreenMatrix());
        GUIShader.getStripesShaderProgram().loadUniform("patternoffset", (float)vActualPos.y + (float)pScroller.getOffset());
        GUIShader.getStripesShaderProgram().loadUniform("lineheight", 30.0f);
        GUIShader.getStripesShaderProgram().loadUniform("color1", vec4.sub(GUI.getTheme().primaryColor, new vec4(0.02f, 0.02f, 0.02f, 0.0f)));
        GUIShader.getStripesShaderProgram().loadUniform("color2", GUI.getTheme().primaryColor);
        Box.renderCustom();
        GUIShader.getStripesShaderProgram().unuse();
            

        pScroller.render();
        pTitleBox.render();
    }

    public void addEntry(HierarchyListEntry<T> entry)
    {
        pRootEntry.addGUI(entry);
        pScroller.updateContent();
    }

    public void selectEntry(HierarchyListEntry<T> entry)
    {
        this.pSelectedEntry = entry;
        pSelectedEntryIndicator.hide(entry == null);
    }

    public void onTick(OnSwitchTicked callback)
    {
        this.pTickCallback = callback;
    }

    public void onEntryClick(GUICallback callback)
    {
        this.pEntryClickCallback = callback;
    }

    public void repositionEntries()
    {
        pRootEntry.repositionEntries();
    }


    //
    // Getter
    //
    public HierarchyListEntry<T> getRootEntry()            { return this.pRootEntry; }
    public HierarchyListEntry<T> getSelectedEntry()        { return this.pSelectedEntry; }
    public boolean isEditable()                            { return this.bEditable; }
    public boolean isSelectable()                          { return this.bSelectable; }
    public OnSwitchTicked getTickCallback()                { return this.pTickCallback; }
    public GUICallback getClickCallback()                  { return this.pEntryClickCallback; }
    public List<HierarchyListEntry<T> > getTickedEntries() { return this.pRootEntry.getTickedEntries(); }
};