#include "Switch.h"
#include <gum-maths.h>
#include <Essentials/Tools.h>

Switch::Switch(ivec2 pos, ivec2 size, float radius) 
{
    this->sType = "Switch";
    this->vPos = pos;
    this->vSize = size;

    pBackground = new Box(ivec2(0,0), ivec2(100, 100));
    pBackground->setCornerRadius(vec4(radius));
    pBackground->setSizeInPercent(true, true);
    pBackground->setColor(vec4(0.3f, 0.3f, 0.3f, 1.0f));
    addElement(pBackground);


    pTickbox = new Box(ivec2(10,10), ivec2(80, 80));
    pTickbox->setCornerRadius(vec4(radius));
    pTickbox->setPositionInPercent(true, true);
    pTickbox->setSizeInPercent(true, true);
    pTickbox->setColor(vec4(Gum::Maths::HSVToRGB(vec3(rand() % 360, 100, 70)),1.0));
    addElement(pTickbox);

    resize();
    reposition();
}

Switch::~Switch() {};

void Switch::update()
{
    if(isClicked())
    {
        pTickbox->hide(!pTickbox->isHidden());
    }


    updatechildren();
}

bool Switch::isTicked()       { return pTickbox->isHidden(); }

void Switch::tick(bool state) { pTickbox->hide(state); }

Switch* Switch::createFromXMLNode(XMLNode* node)
{
    float borderRadius = node->mAttributes["border-radius"] != "" ? Tools::StringToFloat(node->mAttributes["border-radius"]) : 0.0f;
    return new Switch(ivec2(0,0), ivec2(1,1), borderRadius);
}