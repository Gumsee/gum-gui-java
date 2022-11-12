#pragma once
#include "../../Primitives/RenderGUI.h"
#include "../../Basics/TextField.h"
#include "Essentials/Unicode.h"

class HierarchyList;

class HierarchyListEntry : public RenderGUI
{
private:
    TextField* title;
    bool bIsOpen;
    bool bHasChildEntries;
    std::function<void()> pCallback;
    int indent = 0;
    static VertexArrayObject *pArrowVAO; 
    mat4 m4ArrowMatrix;
    HierarchyList* pParentList;

    void repositionEntries(bool checkforselectedentry = false);
    static void initVAO();

protected:
    void updateOnPosChange();
    void updateBoundingBox(bool override = false);

public:
    HierarchyListEntry(std::string name, HierarchyList* parentlist, std::function<void()> callback);
    ~HierarchyListEntry();
    static const int INDENT_SIZE = 10;

    void update();
    void render();

    void openState(bool opn);
    void openAll();
    void addEntry(HierarchyListEntry* entry);

    void setIndent(int indent);
    void setRenameCallback(std::function<void(Gum::Unicode)> callback);

    int getHeight();

    static void cleanup();
};