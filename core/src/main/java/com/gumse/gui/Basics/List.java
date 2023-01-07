package com.gumse.gui.Basics;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUIShader;
import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.*;
import com.gumse.tools.Debug;

public class List <E> extends RenderGUI
{
    private class ListEntry extends RenderGUI
    {
        E pUserPtr;
        Object[] alData;


        public ListEntry(Object[] data, List<E> parent, E usrptr)
        {
            this.alData = data;
            this.pUserPtr = usrptr;
            int columnSize = 100 / parent.getColumnTypes().length;

            for(int i = 0; i < parent.getColumnTypes().length; i++)
            {
                RenderGUI item = new RenderGUI();
                item.setPosition(new ivec2(columnSize * i, 0));
                item.setPositionInPercent(true, false);
                item.setSize(new ivec2(columnSize, 30));
                item.setSizeInPercent(true, false);
                addElement(item);

                ColumnType type = parent.getColumnTypes()[i];
                switch(type)
                {
                    case BOOLEAN:
                        Switch boolGUI = new Switch(new ivec2(50, 5), new ivec2(20), 0);
                        boolGUI.setPositionInPercent(true, false);
                        boolGUI.setOrigin(new ivec2(10, 0));
                        item.addGUI(boolGUI);
                        break;
                    case DATE:
                        break;
                    case DROPDOWN:
                        break;
                    case INTEGER:
                        TextBox numGUI = new TextBox(String.valueOf((Integer)data[i]), FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 30));
                        numGUI.setTextSize(25);
                        numGUI.getBox().hide(true);
                        numGUI.setAlignment(Alignment.CENTER);
                        numGUI.setSizeInPercent(true, false);
                        item.addGUI(numGUI);
                        break;
                    case STRING:
                        TextBox strGUI = new TextBox((String)data[i], FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 30));
                        strGUI.setTextSize(25);
                        strGUI.getBox().hide(true);
                        strGUI.setAlignment(Alignment.LEFT);
                        strGUI.setSizeInPercent(true, false);
                        item.addGUI(strGUI);
                        break;
                    case TIME:
                        int seconds = (Integer)data[i];
                        int hours = seconds / 3600;
                        int minutes = (seconds % 3600) / 60;
                        seconds = seconds % 60;

                        Text timeGUI = new Text(String.format("%02d:%02d:%02d", hours, minutes, seconds), FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), 0);
                        timeGUI.setCharacterHeight(25);
                        item.addGUI(timeGUI);
                        break;
                    default:
                        break;
                    
                }
            }
        }
        
        public Object getColumn(int index)
        {
            return alData[index];
        }

        public E getUserPtr()
        {
            return pUserPtr;
        }
    };

    private static final int TITLEBAR_HEIGHT = 30;

    private ArrayList<ListEntry> vEntries;
    private Box pBackground;
    private Scroller pScroller;
    private ColumnType[] alColumns;


    public enum ColumnType
    {
        STRING,
        INTEGER,
        DROPDOWN,
        DATE,
        TIME,
        BOOLEAN,
    };

    public List(ivec2 pos, ivec2 size, String[] titles, ColumnType[] columns)
    {
        this.vSize.set(size);
        this.vPos.set(pos);
        this.vEntries = new ArrayList<>();
        this.alColumns = columns;


        if(titles.length != alColumns.length)
        {
            Debug.error("List: titles array length doesnt match column array length");
            return;
        }
    

        
        pBackground = new Box(new ivec2(0,0), new ivec2(100, 100));
        pBackground.setSizeInPercent(true, true);
        pBackground.hide(true);
        addElement(pBackground);

        int columnSize = 100 / titles.length;
        for(int i = 0; i < titles.length; i++)
        {    
            TextBox titleBox = new TextBox(titles[i], FontManager.getInstance().getDefaultFont(), new ivec2(columnSize * i, 0), new ivec2(columnSize, TITLEBAR_HEIGHT));
            titleBox.setAlignment(TextBox.Alignment.LEFT);
            titleBox.setTextSize(25);
            //titleBox.setTextOffset(new ivec2(-10, 0));
            titleBox.setSizeInPercent(true, false);
            titleBox.setPositionInPercent(true, false);
            titleBox.setColor(new vec4(0.1f, 0.1f, 0.1f, 1.0f));
            titleBox.setTextColor(new vec4(0.76f, 0.76f, 0.76f, 1.0f));
            addElement(titleBox);
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
    
        renderchildren();
    }
    
    
    public void addEntry(Object[] data, E usrptr)
    {
        if(data.length != alColumns.length)
        {
            Debug.error("List: addEntry: data array length doesnt match column count");
            return;
        }
        ListEntry entry = new ListEntry(data, this, usrptr);
        entry.setSize(new ivec2(100, 40));
        entry.setPosition(new ivec2(0, vEntries.size() * 30 + TITLEBAR_HEIGHT));
        vEntries.add(entry);
        addElement(entry);
        entry.setSizeInPercent(true, false);  
    }

    public ColumnType[] getColumnTypes()
    {
        return alColumns;
    }

    public <T> ArrayList<T> getColumnWhere(int index, Predicate<ListEntry> condition)
    {
        ArrayList<T> retList = new ArrayList<>();

        for(ListEntry entry : vEntries)
        {
            if(condition.test(entry))
                retList.add((T)entry.getColumn(index));
        }

        return retList;
    }

    public ArrayList<E> getUserdataWhere(Predicate<ListEntry> condition)
    {
        ArrayList<E> retList = new ArrayList<>();

        for(ListEntry entry : vEntries)
        {
            if(condition.test(entry))
                retList.add(entry.getUserPtr());
        }

        return retList;
    }
};