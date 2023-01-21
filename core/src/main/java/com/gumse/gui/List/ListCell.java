package com.gumse.gui.List;

import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Primitives.RenderGUI.GUICallback;

public class ListCell 
{
    public Object data;
    public GUICallback onclickcallback;
    public Alignment alignment;
    
    public ListCell(Object data)
    {
        this.alignment = Alignment.CENTER;
        this.data = data;
        onclickcallback = null;
    }
}
