package com.gumse.gui.List;

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

    public ColumnInfo(String title, ColumnType type, int width)
    {
        this.title = title;
        this.type = type;
        this.width = width;
    }
}
