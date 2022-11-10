#pragma once
#include "../Primitives/RenderGUI.h"
#include "../Primitives/Text.h"

class Group : public RenderGUI
{
private:
    Text* pTitle;

    void updateRadius();

protected:
    void updateOnTitleChange();
    void updateOnColorChange();
    void updateOnCornerRadiusChange();
    void updateOnAddGUI(RenderGUI* gui);

public:
    Group(const ivec2& pos, const ivec2& size);
    ~Group();

    static Group* createFromXMLNode(XMLNode* node);
};