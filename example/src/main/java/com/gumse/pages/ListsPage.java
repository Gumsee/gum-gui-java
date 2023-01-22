package com.gumse.pages;

import com.gumse.gui.Basics.Tabs;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.HierarchyList.HierarchyList;
import com.gumse.gui.HierarchyList.HierarchyListEntry;
import com.gumse.gui.List.ColumnInfo;
import com.gumse.gui.List.List;
import com.gumse.gui.List.ListCell;
import com.gumse.gui.List.ColumnInfo.ColumnType;
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
        listTabs.addTab("List", false);
        listTabs.addTab("HierarchyList", true);
        listTabs.addTab("Some other tab", false);
        listTabs.addTab("Scroll here", false);
        listTabs.addTab("Reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeally long tab", false);
        addElement(listTabs);

        List<String> testList = new List<>(
            new ivec2(0, 0), 
            new ivec2(100, 100), 
            new ColumnInfo[] {
                new ColumnInfo("Column1", ColumnType.STRING,  30, ""),
                new ColumnInfo("Column2", ColumnType.STRING,  30, ""),
                new ColumnInfo("Bool",    ColumnType.BOOLEAN, 10, ""),
                new ColumnInfo("Num",     ColumnType.INTEGER, 10, ""),
                new ColumnInfo("Column5", ColumnType.TIME,    20, ""),
            }
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

        HierarchyList testHierarchyList = new HierarchyList(new ivec2(0, 0), new ivec2(100, 100), "Test HierarchyList", "Root Element", false, "");
        testHierarchyList.setSizeInPercent(true, true);
        HierarchyListEntry childEntry = new HierarchyListEntry("Someee entry", testHierarchyList, null);
        childEntry.addEntry(new HierarchyListEntry("Child of another Child :O", testHierarchyList, null));
        childEntry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        childEntry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        HierarchyListEntry subChildEntry = new HierarchyListEntry("Sub child", testHierarchyList, null);
        subChildEntry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        subChildEntry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        subChildEntry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        childEntry.addEntry(subChildEntry);
        testHierarchyList.addEntry(childEntry);

        HierarchyListEntry child2Entry = new HierarchyListEntry("eeh entry", testHierarchyList, null);
        child2Entry.addEntry(new HierarchyListEntry("Child of another Child :O", testHierarchyList, null));
        child2Entry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        child2Entry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        child2Entry.addEntry(new HierarchyListEntry("We're siblings", testHierarchyList, null));
        testHierarchyList.addEntry(child2Entry);
        listTabs.addGUIToTab(testHierarchyList, "HierarchyList");

        this.setSizeInPercent(true, true);
        reposition();
        resize();
    }
}
