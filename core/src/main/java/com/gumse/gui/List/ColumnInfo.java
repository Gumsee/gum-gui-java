package com.gumse.gui.List;

import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI.GUICallback;

public class ColumnInfo 
{
    public enum ColumnType
    {
        STRING,
        INTEGER,
        DROPDOWN,
        DATE,
        TIME,
        BOOLEAN,
    };

    public String title;
    public ColumnType type;
    public int width;
    public Font font;
    public GUICallback onclickcallback;
    public Alignment alignment;

    public ColumnInfo(String title, ColumnType type, int width)
    {
        this.title = title;
        this.type  = type;
        this.width = width;
        this.font  = FontManager.getInstance().getDefaultFont();
        this.onclickcallback = null;
        this.alignment = Alignment.LEFT;
    }
}
