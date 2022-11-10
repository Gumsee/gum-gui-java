#pragma once
#include "Button.h"

class RightClickMenu : public RenderGUI
{
private:
    std::vector<Button*> vEntries;

    vec2 vEntrySize;

    bool bIsOpen;
    float fCornerRadius;

    void buildEntries();
public:

    RightClickMenu(vec2 pos, vec2 entrysize, vec2 area, float cornerRadius);
    ~RightClickMenu();

    void update();
    void render();

    void addEntry(Button *btn);
    void removeEntry(int index);
    void setEntrySize(vec2 size);

    Button* getEntry(int index);
};