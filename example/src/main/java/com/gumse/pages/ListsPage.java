package com.gumse.pages;

import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.textures.Texture;


public class ListsPage extends RenderGUI
{
    public ListsPage()
    {
        this.vSize = new ivec2(100,100);
        FontManager fonts = FontManager.getInstance();
        
        //Add Hierarchylist and normal list

        this.setSizeInPercent(true, true);
        reposition();
        resize();
    }
}
