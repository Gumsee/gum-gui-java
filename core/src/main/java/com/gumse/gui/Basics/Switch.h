#pragma once
#include "../Primitives/Box.h"

class Switch : public RenderGUI
{
private:
    Box* pBackground;
    Box* pTickbox;
    
public:
    Switch(ivec2 pos, ivec2 size, float radius = 0);
    ~Switch();

    void update();

    bool isTicked();
    void tick(bool state);

    static Switch* createFromXMLNode(XMLNode* node);
};