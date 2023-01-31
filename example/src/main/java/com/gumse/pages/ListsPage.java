package com.gumse.pages;

import com.gumse.gui.Basics.Tabs;
import com.gumse.gui.HierarchyList.HierarchyList;
import com.gumse.gui.HierarchyList.HierarchyListEntry;
import com.gumse.gui.List.ColumnInfo;
import com.gumse.gui.List.List;
import com.gumse.gui.List.ListCell;
import com.gumse.gui.List.ColumnInfo.ColumnType;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.tools.Output;


public class ListsPage extends RenderGUI
{
    public ListsPage()
    {
        this.vSize = new ivec2(100,100);
        
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
        //for(int i = 0; i < 200; i++)
        //    testList.addEntry(new ListCell[] { new ListCell("kek" + i), new ListCell("eegg"),  new ListCell(false), new ListCell(42),  new ListCell(123)  }, "userdata");
        testList.setMargin(new ivec2(-50, -50));
        testList.onBottomHit((RenderGUI gui) -> {
            Output.info("Hit Bottom");
        });
        listTabs.addGUIToTab(testList, "List");

        HierarchyList<String> testHierarchyList = new HierarchyList<>(new ivec2(0, 0), new ivec2(100, 100), "Test HierarchyList", "Root Element", false, true, "");
        testHierarchyList.setSizeInPercent(true, true);
        HierarchyListEntry<String> childEntry = new HierarchyListEntry<String>("Someee entry", testHierarchyList, "");
        childEntry.addEntry(new HierarchyListEntry<String>("Child of another Child :O", testHierarchyList, ""));
        childEntry.addEntry(new HierarchyListEntry<String>("We're siblings", testHierarchyList, ""));
        childEntry.addEntry(new HierarchyListEntry<String>("We're siblings", testHierarchyList, ""));
        HierarchyListEntry<String> subChildEntry = new HierarchyListEntry<String>("Sub child", testHierarchyList, "");
        subChildEntry.addEntry(new HierarchyListEntry<String>("We're siblings", testHierarchyList, ""));
        subChildEntry.addEntry(new HierarchyListEntry<String>("We're siblings", testHierarchyList, ""));
        subChildEntry.addEntry(new HierarchyListEntry<String>("We're siblings", testHierarchyList, ""));
        childEntry.addEntry(subChildEntry);
        testHierarchyList.addEntry(childEntry);

        HierarchyListEntry<String> child2Entry = new HierarchyListEntry<String>("eeh entry", testHierarchyList, "");
        child2Entry.addEntry(new HierarchyListEntry<String>("Child of another Child :O", testHierarchyList, ""));
        child2Entry.addEntry(new HierarchyListEntry<String>("We're siblings", testHierarchyList, ""));
        child2Entry.addEntry(new HierarchyListEntry<String>("We're siblings", testHierarchyList, ""));
        child2Entry.addEntry(new HierarchyListEntry<String>("We're siblings", testHierarchyList, ""));
        testHierarchyList.addEntry(child2Entry);
        listTabs.addGUIToTab(testHierarchyList, "HierarchyList");

        this.setSizeInPercent(true, true);
        reposition();
        resize();
    }
}
