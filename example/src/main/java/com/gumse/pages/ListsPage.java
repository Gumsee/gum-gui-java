package com.gumse.pages;

import com.gumse.gui.Basics.Dropdown;
import com.gumse.gui.Basics.List;
import com.gumse.gui.Basics.Tabs;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Basics.List.ColumnType;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.HierarchyList.HierarchyList;
import com.gumse.gui.HierarchyList.HierarchyListEntry;
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
        Tabs listTabs = new Tabs(new ivec2(0, 30), new ivec2(100, 100), new ivec2(100, 20));
        listTabs.setSizeInPercent(true, true);
        listTabs.addTab("List", true);
        listTabs.addTab("HierarchyList", false);
        listTabs.addTab("Some other tab", false);
        listTabs.addTab("Scroll here", false);
        listTabs.addTab("Reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeally long tab", false);
        addElement(listTabs);

        List<String> testList = new List<>(
            new ivec2(0, 0), 
            new ivec2(100, 100), 
            new String[] {"Column1", "Column2", "Column3", "Column4", "Column5"}, 
            new ColumnType[] { ColumnType.STRING, ColumnType.STRING, ColumnType.BOOLEAN, ColumnType.INTEGER, ColumnType.TIME}
        );
        testList.setSizeInPercent(true, true);
        testList.addEntry(new Object[] { "kek",  "lolol", true,  69,  600  }, "userdata");
        testList.addEntry(new Object[] { "kek2", "eeehh", false, 420, 4    }, "userdata");
        testList.addEntry(new Object[] { "kek3", "bleb",  true,  666, 3600 }, "userdata");
        testList.addEntry(new Object[] { "kek4", "hehe",  true,  776, 42   }, "userdata");
        testList.addEntry(new Object[] { "kek5", "eegg",  false, 42,  123  }, "userdata");
        listTabs.addGUIToTab(testList, "List");

        HierarchyList testHierarchyList = new HierarchyList(new ivec2(0, 0), new ivec2(100, 100), "Test HierarchyList", "Root Element");
        testHierarchyList.setSizeInPercent(true, true);
        HierarchyListEntry childEntry = new HierarchyListEntry("Some entry", testHierarchyList, null);
        childEntry.addEntry(new HierarchyListEntry("Child of another Child :O", testHierarchyList, null));
        childEntry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        childEntry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        childEntry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        testHierarchyList.addEntry(childEntry);
        listTabs.addGUIToTab(testHierarchyList, "HierarchyList");

        this.setSizeInPercent(true, true);
        reposition();
        resize();
    }
}
