#include "Group.h"
#include "../Font/FontManager.h"
#include <System/MemoryManagement.h>

Group::Group(const ivec2& pos, const ivec2& size)
{
    this->sType = "Group";
    this->vPos = pos;
    this->vSize = size;
    this->pTitle = nullptr;

    resize();
    reposition();
}


Group::~Group()
{
    Gum::_delete(pTitle);
}

void Group::updateOnTitleChange()
{
    if(pTitle == nullptr)
    {
        pTitle = new Text("", Gum::GUI::Fonts->getDefaultFont(), ivec2(0, -25));
        pTitle->setCharacterHeight(20.0f);
        pTitle->setColor(this->getColor());
        addElement(pTitle);
    }

    pTitle->setString(this->getTitle());
}

void Group::updateOnColorChange()
{
    if(pTitle != nullptr)
    {
        pTitle->setColor(this->getColor());
    }
}

void Group::updateOnCornerRadiusChange()    { updateRadius(); }
void Group::updateOnAddGUI(RenderGUI* gui)  { updateRadius(); }

void Group::updateRadius()
{
    if(numChildren() > 1)
    {
        vChildren[0]->setCornerRadius(vec4(v4CornerRadius.x, v4CornerRadius.y, 0.0f, 0.0f));
        for(size_t i = 1; i < numChildren() - 1; i++)
        {
            vChildren[i]->setCornerRadius(vec4(0.0f));
        }
        vChildren[numChildren() - 1]->setCornerRadius(vec4(0.0f, 0.0f, v4CornerRadius.z, v4CornerRadius.w));
    }
    else if(numChildren() == 1)
    {
        vChildren[0]->setCornerRadius(v4CornerRadius);
    }
}


Group* Group::createFromXMLNode(XMLNode* node)
{
    Group* groupgui = new Group(ivec2(0,0), ivec2(1,1));
    return groupgui;
}