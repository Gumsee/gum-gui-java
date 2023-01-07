package com.gumse.gui.List;

import com.gumse.gui.Primitives.RenderGUI.GUICallback;

public class ListCell 
{
    public Object data;
    public GUICallback onclickcallback;
    
    public ListCell(Object data)
    {
        this.data = data;
    }
}
