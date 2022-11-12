#pragma once
#include "../../Basics/TextBox.h"
#include "../../Basics/Scroller.h"
#include <OpenGL/ShaderProgram.h>
#include <functional>
#include <vector>

class HierarchyListEntry;

class HierarchyList : public RenderGUI
{
private:
    TextBox* pTitleBox;
    Scroller* pScroller;
    Box* pBackground;
    HierarchyListEntry* pSelectedEntry;
    Box* pSelectedEntryIndicator;
    HierarchyListEntry* pRootEntry;

protected:
    void updateOnSizeChange() {}

public:
    HierarchyList(ivec2 pos, ivec2 size, std::string title, std::string rootname = "Root");
    ~HierarchyList();

    void render();
    void update();

    void addEntry(HierarchyListEntry* entry);
    void selectEntry(HierarchyListEntry* entry);
    HierarchyListEntry* getRootEntry();
    HierarchyListEntry* getSelectedEntry();
};