#pragma once
#include <Essentials/SmoothFloat.h>
#include "TextBox.h"

class Tabs : public RenderGUI
{
private:
    TextBox* pActiveTab;
    Box* pBackground;
    std::vector<TextBox*> vTabs;
    ivec2 vTabSize;
    vec4 v4ActiveColor, v4InactiveColor;
    SmoothFloat sfOffset;

public:
    Tabs(vec2 pos, vec2 size, ivec2 tabsize);
    ~Tabs();

    void update();
    void render();
    bool updateToolTip();

    void addGUIToTab(RenderGUI *gui, std::string tabName);
    void addTab(std::string name, bool active = false);
    void setActiveTab(std::string tabname);

    bool isActiveTab(std::string tabname);
    TextBox* getActiveTab();
    TextBox* getTab(std::string name);

    unsigned int numTabs();

    static Tabs* createFromXMLNode(XMLNode* node);
};