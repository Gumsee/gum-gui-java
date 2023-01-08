package com.gumse.gui.List;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.gumse.gui.Basics.Switch;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.List.List.ColumnType;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.ivec2;

public class ListEntry <E> extends RenderGUI
{
    E pUserPtr;
    ListCell[] alCells;


    public ListEntry(ListCell[] cells, List<E> parent, E usrptr)
    {
        this.alCells = cells;
        this.pUserPtr = usrptr;
        int columnSize = 100 / parent.getColumnTypes().length;

        for(int i = 0; i < parent.getColumnTypes().length; i++)
        {
            RenderGUI item = new RenderGUI();
            item.setPosition(new ivec2(columnSize * i, 0));
            item.setPositionInPercent(true, false);
            item.setSize(new ivec2(columnSize, 30));
            item.setSizeInPercent(true, false);
            item.onClick(cells[i].onclickcallback);
            addGUI(item);

            ColumnType type = parent.getColumnTypes()[i];
            switch(type)
            {
                case BOOLEAN:
                    Switch boolGUI = new Switch(new ivec2(50, 5), new ivec2(20), 0);
                    switch(cells[i].alignment) 
                    {
                        case CENTER:
                            boolGUI.setPositionX(50);
                            boolGUI.setPositionInPercent(true, false);
                            boolGUI.setOrigin(new ivec2(10, 0));
                            break;
                        case LEFT:
                            boolGUI.setPositionX(0);
                            boolGUI.setPositionInPercent(false, false);
                            boolGUI.setOrigin(new ivec2(0, 0));
                            break;
                        case RIGHT:
                            boolGUI.setPositionX(100);
                            boolGUI.setPositionInPercent(true, false);
                            boolGUI.setOrigin(new ivec2(20, 0));
                            break;
                    }
                    item.addGUI(boolGUI);
                    break;
                    
                case DATE:
                    TextBox dateGUI = new TextBox(new SimpleDateFormat("yyyy.MM.dd").format((Timestamp)cells[i].data), FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 30));
                    dateGUI.setTextSize(25);
                    dateGUI.getBox().hide(true);
                    dateGUI.setAlignment(cells[i].alignment);
                    dateGUI.setSizeInPercent(true, false);
                    item.addGUI(dateGUI);
                    break;

                case DROPDOWN:
                    break;

                case INTEGER:
                    TextBox numGUI = new TextBox(String.valueOf((Integer)cells[i].data), FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 30));
                    numGUI.setTextSize(25);
                    numGUI.getBox().hide(true);
                    numGUI.setAlignment(cells[i].alignment);
                    numGUI.setSizeInPercent(true, false);
                    item.addGUI(numGUI);
                    break;

                case STRING:
                    TextBox strGUI = new TextBox((String)cells[i].data, FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 30));
                    strGUI.setTextSize(25);
                    strGUI.getBox().hide(true);
                    strGUI.setAlignment(cells[i].alignment);
                    strGUI.setSizeInPercent(true, false);
                    item.addGUI(strGUI);
                    break;

                case TIME:
                    int seconds = (Integer)cells[i].data;
                    int hours = seconds / 3600;
                    int minutes = (seconds % 3600) / 60;
                    seconds = seconds % 60;

                    TextBox timeGUI = new TextBox(String.format("%02d:%02d:%02d", hours, minutes, seconds), FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 30));
                    timeGUI.setTextSize(25);
                    timeGUI.getBox().hide(true);
                    timeGUI.setAlignment(cells[i].alignment);
                    timeGUI.setSizeInPercent(true, false);
                    break;
                default:
                    break;
                
            }
        }
    }
    
    public ListCell getColumn(int index)  { return alCells[index]; }
    public E getUserPtr()                 { return pUserPtr; }
};