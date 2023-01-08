package com.gumse.pages;

import com.gumse.gui.Basics.Tabs;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.HierarchyList.HierarchyList;
import com.gumse.gui.HierarchyList.HierarchyListEntry;
import com.gumse.gui.List.List;
import com.gumse.gui.List.ListCell;
import com.gumse.gui.List.List.ColumnType;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;


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
        testList.addEntry(new ListCell[] { new ListCell("kek"),  new ListCell("lolol"), new ListCell(true),  new ListCell(69),  new ListCell(600)  }, "userdata");
        testList.addEntry(new ListCell[] { new ListCell("kek2"), new ListCell("eeehh"), new ListCell(false), new ListCell(420), new ListCell(4)    }, "userdata");
        testList.addEntry(new ListCell[] { new ListCell("kek3"), new ListCell("bleb"),  new ListCell(true),  new ListCell(666), new ListCell(3600) }, "userdata");
        testList.addEntry(new ListCell[] { new ListCell("kek4"), new ListCell("hehe"),  new ListCell(true),  new ListCell(776), new ListCell(42)   }, "userdata");
        for(int i = 0; i < 200; i++)
            testList.addEntry(new ListCell[] { new ListCell("kek" + i), new ListCell("eegg"),  new ListCell(false), new ListCell(42),  new ListCell(123)  }, "userdata");
        testList.setMargin(new ivec2(-50, -50));
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
