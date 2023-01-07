package com.gumse.gui.List;

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
                    boolGUI.setPositionInPercent(true, false);
                    boolGUI.setOrigin(new ivec2(10, 0));
                    item.addGUI(boolGUI);
                    break;
                case DATE:
                    break;
                case DROPDOWN:
                    break;
                case INTEGER:
                    TextBox numGUI = new TextBox(String.valueOf((Integer)cells[i].data), FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 30));
                    numGUI.setTextSize(25);
                    numGUI.getBox().hide(true);
                    numGUI.setAlignment(Alignment.CENTER);
                    numGUI.setSizeInPercent(true, false);
                    item.addGUI(numGUI);
                    break;
                case STRING:
                    TextBox strGUI = new TextBox((String)cells[i].data, FontManager.getInstance().getDefaultFont(), new ivec2(0, 0), new ivec2(100, 30));
                    strGUI.setTextSize(25);
                    strGUI.getBox().hide(true);
                    strGUI.setAlignment(Alignment.LEFT);
                    strGUI.setSizeInPercent(true, false);
                    item.addGUI(strGUI);
                    break;
                case TIME:
                    int seconds = (Integer)cells[i].data;
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
    
    public ListCell getColumn(int index)  { return alCells[index]; }
    public E getUserPtr()                 { return pUserPtr; }
};