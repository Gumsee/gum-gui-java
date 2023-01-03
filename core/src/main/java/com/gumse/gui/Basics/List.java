package com.gumse.gui.Basics;

import java.util.ArrayList;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.gui.GUIShader;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.*;
import com.gumse.system.Window;

public class List extends RenderGUI
{
    private class ListEntry extends RenderGUI
    {
        Text titleText = null;
        //RenderGUI entryitem = null;


        public ListEntry(String title, vec4 color)
        {
            titleText = new Text(title, FontManager.getInstance().getDefaultFont(), new ivec2(10, 0), 0);
            titleText.setCharacterHeight(20);
            titleText.setColor(new vec4(0.76f, 0.76f, 0.76f, 1.0f));
            addElement(titleText);
        }
        
        public void cleanup()
        {
            //Gum::_delete(titleText);
            //Gum::_delete(entryitem);
        }
    };

    private ArrayList<ListEntry> vEntries;
    private TextBox pTitleBox = null;
    private Box pBackground = null;


    public enum ENTRY_TYPE {
        STRING,
        INTEGER,
        DROPDOWN,
        DATE,
        TIME,
        BOOLEAN,
    };

    public List(ivec2 pos, ivec2 size, String title)
    {
        this.vSize.set(size);
        this.vPos.set(pos);
        this.sTitle = title;
        this.vEntries = new ArrayList<>();
    
    
        pBackground = new Box(new ivec2(0,0), new ivec2(100, 100));
        pBackground.setSizeInPercent(true, true);
        addElement(pBackground);
    
        pTitleBox = new TextBox(title, FontManager.getInstance().getDefaultFont(), new ivec2(0,0), new ivec2(100, 30));
        pTitleBox.setAlignment(TextBox.Alignment.LEFT);
        pTitleBox.setTextSize(20);
        pTitleBox.setTextOffset(new ivec2(-10, 0));
        pTitleBox.setSizeInPercent(true, false);
        pTitleBox.setColor(new vec4(0.1f, 0.1f, 0.1f, 1.0f));
        pTitleBox.setTextColor(new vec4(0.76f, 0.76f, 0.76f, 1.0f));
        addElement(pTitleBox);
        
        
    
        resize();
        reposition();
    }
    
    public void cleanup()
    {
        /*Gum::_delete(pTitleBox);
        Gum::_delete(pBackground);
    
        for(ListEntry *entry : vEntries)
            Gum::_delete(entry);*/
    }
    
    public void render()
    {
        GUIShader.getStripesShaderProgram().use();
        GUIShader.getStripesShaderProgram().loadUniform("transmat", pBackground.getTransformation());
        GUIShader.getStripesShaderProgram().loadUniform("orthomat", Framebuffer.CurrentlyBoundFramebuffer.getScreenMatrix());
        GUIShader.getStripesShaderProgram().loadUniform("patternoffset", (float)vActualPos.y);// + (float)pScroller.getOffset());
        GUIShader.getStripesShaderProgram().loadUniform("lineheight", 30.0f);
        GUIShader.getStripesShaderProgram().loadUniform("color1", new vec4(0.16f, 0.16f, 0.16f, 1.0f));
        GUIShader.getStripesShaderProgram().loadUniform("color2", new vec4(0.18f, 0.18f, 0.18f, 1.0f));
        pBackground.renderCustom();
        GUIShader.getStripesShaderProgram().unuse();
    
        pTitleBox.render();
    
        for(ListEntry entry : vEntries)
            entry.render();
    }
    
    
    public void addEntry(String title, ENTRY_TYPE type)
    {
        ListEntry entry = new ListEntry(
            title, 
            (vEntries.size() % 2 == 0) ? GUI.getTheme().primaryColor : GUI.getTheme().secondaryColor
        );
        entry.setSize(new ivec2(100, 40));
        entry.setPosition(new ivec2(0, vEntries.size() * 30 + 30));
        vEntries.add(entry);
        addElement(entry);
        entry.setSizeInPercent(true, false);
    
    }
};