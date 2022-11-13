package com.gumse.pages;

import com.gumse.gui.Basics.List;
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

        List testList = new List(new ivec2(20, 40), new ivec2(300, 300), "Test List");
        testList.addEntry("Some Text entry", List.ENTRY_TYPE.STRING);
        testList.addEntry("Some Switch entry", List.ENTRY_TYPE.BOOLEAN);
        testList.addEntry("Some Dropdown entry", List.ENTRY_TYPE.DROPDOWN);
        testList.addEntry("Some Number entry", List.ENTRY_TYPE.INTEGER);
        testList.addEntry("Some Time entry", List.ENTRY_TYPE.TIME);
        addElement(testList);

        this.setSizeInPercent(true, true);
        reposition();
        resize();
    }
}
