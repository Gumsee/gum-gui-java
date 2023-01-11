package com.gumse.gui.List;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.gui.GUIShader;
import com.gumse.gui.Basics.Scroller;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Box;
import com.gumse.maths.*;
import com.gumse.tools.Output;

public class List <E> extends RenderGUI
{
    private static final int TITLEBAR_HEIGHT = 30;

    private ArrayList<ListEntry<E> > vEntries;
    private RenderGUI pBackground;
    private Scroller pScroller;
    private ColumnInfo[] alColumns;



    public List(ivec2 pos, ivec2 size, ColumnInfo[] columns)
    {
        this.vSize.set(size);
        this.vPos.set(pos);
        this.vEntries = new ArrayList<>();
        this.alColumns = columns;
        this.bUpdateFromFirstToLast = true;

        int length = 0;
        for(ColumnInfo column : columns)
        {
            length += column.width;
        }

        if(length != 100)
        {
            Output.error("List: column widths must add up to 100");
            return;
        }
    

        
        pBackground = new RenderGUI();
        pBackground.setSize(new ivec2(100, 100));
        pBackground.setSizeInPercent(true, true);
        addElement(pBackground);

        int currentpos = 0;
        for(int i = 0; i < columns.length; i++)
        {    
            TextBox titleBox = new TextBox(columns[i].title, columns[i].font, new ivec2(currentpos, 0), new ivec2(columns[i].width, TITLEBAR_HEIGHT));
            titleBox.setAlignment(columns[i].alignment);
            titleBox.setTextSize(25);
            //titleBox.setTextOffset(new ivec2(-10, 0));
            titleBox.setSizeInPercent(true, false);
            titleBox.setPositionInPercent(true, false);
            titleBox.setColor(GUI.getTheme().primaryColorShade);
            titleBox.onClick(columns[i].onclickcallback);
            pBackground.addGUI(titleBox);
            currentpos += columns[i].width;
        }

        pScroller = new Scroller(new ivec2(0, TITLEBAR_HEIGHT), new ivec2(100, 100));
        pScroller.setSizeInPercent(true, true);
        pScroller.setMargin(new ivec2(0, -TITLEBAR_HEIGHT));
        addElement(pScroller);
        
        
    
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
    
    public void renderextra()
    {
        GUIShader.getStripesShaderProgram().use();
        GUIShader.getStripesShaderProgram().loadUniform("transmat", pBackground.getTransformation());
        GUIShader.getStripesShaderProgram().loadUniform("orthomat", Framebuffer.CurrentlyBoundFramebuffer.getScreenMatrix());
        GUIShader.getStripesShaderProgram().loadUniform("patternoffset", (float)(vActualPos.y + pScroller.getOffset()));// + (float)pScroller.getOffset());
        GUIShader.getStripesShaderProgram().loadUniform("lineheight", 30.0f);
        GUIShader.getStripesShaderProgram().loadUniform("color1", vec4.sub(GUI.getTheme().primaryColor, new vec4(0.02f, 0.02f, 0.02f, 0.0f)));
        GUIShader.getStripesShaderProgram().loadUniform("color2", GUI.getTheme().primaryColor);
        Box.renderCustom();
        GUIShader.getStripesShaderProgram().unuse();
    
        renderchildren();
    }
    
    
    public void addEntry(ListCell[] cells, E usrptr)
    {
        if(cells.length != alColumns.length)
        {
            Output.error("List: addEntry: data array length doesnt match column count");
            return;
        }
        ListEntry<E> entry = new ListEntry<>(cells, this, usrptr);
        entry.setSize(new ivec2(100, 40));
        entry.setPosition(new ivec2(0, vEntries.size() * 30));
        vEntries.add(entry);
        pScroller.addGUI(entry);
        entry.setSizeInPercent(true, false);  
    }

    public void reset()
    {
        vEntries.clear();
        pScroller.destroyChildren();
    }

    public int numEntries()
    {
        return vEntries.size();
    }

    public ColumnInfo[] getColumns()
    {
        return alColumns;
    }

    public <T> ArrayList<T> getColumnWhere(int index, Predicate<ListEntry<E> > condition)
    {
        ArrayList<T> retList = new ArrayList<>();

        for(ListEntry<E> entry : vEntries)
        {
            if(condition.test(entry))
                retList.add((T)entry.getColumn(index));
        }

        return retList;
    }

    public ArrayList<E> getUserdataWhere(Predicate<ListEntry<E> > condition)
    {
        ArrayList<E> retList = new ArrayList<>();

        for(ListEntry<E> entry : vEntries)
        {
            if(condition.test(entry))
                retList.add(entry.getUserPtr());
        }

        return retList;
    }

    @Override
    protected void updateOnThemeChange() 
    {
        for(RenderGUI child : pBackground.getChildren())
        {
            child.setColor(GUI.getTheme().primaryColorShade);
        }
    }
};