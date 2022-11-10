#include "Button.h"
#include <Essentials/Tools.h>
#include <System/MemoryManagement.h>

#include "../Font/FontManager.h"
#include "../../gum-gui.h"

Button::Button(std::string title) : Button(ivec2(0,0), ivec2(50, 20), title, Gum::GUI::Fonts->getDefaultFont())
{
}

Button::Button(ivec2 pos, ivec2 size, std::string title, Gum::GUI::Font* font)
{
    this->vPos = pos;
    this->vSize = size;
    this->sType = "button";

    backgroundBox = new TextBox(title, font, vec2(0), ivec2(100, 100));
    backgroundBox->setSizeInPercent(true, true);
    addGUI(backgroundBox);

    resize();
    reposition();

    backgroundBox->setTextSize(getSize().y - 10);
}

Button::~Button() 
{
    Gum::_delete(backgroundBox);
}

void Button::update()
{
    if(backgroundBox->isMouseInside())
    {
        backgroundBox->setColor(v4Color - vec4(0.1));
        if(isHoldingLeftClick())
        {
            backgroundBox->setColor(v4Color - vec4(0.3));
        }
        if(!Gum::Window::CurrentlyBoundWindow->getMouse()->isBusy() && isClicked())
        {
            if(callbackFunction != nullptr)
            {
                callbackFunction();
            }
        }
    }
    else
    {
        backgroundBox->setColor(v4Color);
    }
}

void Button::setCallbackFunction(std::function<void()> func)    { this->callbackFunction = func; }
//void Button::setTexture(Texture *newtex)                        { this->backgroundBox->setTexture(newtex); }
void Button::setSecondColor(vec4 col)                           { this->backgroundBox->getBox()->setSecondColor(col); }
void Button::setHasGradient(bool val)                           { this->backgroundBox->getBox()->setHasGradient(val); }
void Button::setGradientDirectionRight(bool val)                { this->backgroundBox->getBox()->setGradientDirectionRight(val); }

//Texture* Button::getTexture()                                   { return this->backgroundBox->getBox()->getTexture(); }
vec4 Button::getSecondColor()                                   { return this->backgroundBox->getBox()->getSecondColor(); }
TextBox *Button::getBox()                                       { return this->backgroundBox; }

Button* Button::createFromXMLNode(XMLNode* node)
{
    Gum::GUI::Font *font = Gum::GUI::Fonts->getDefaultFont();
    float borderRadius = node->mAttributes["radius"]   != "" ? Tools::StringToFloat(node->mAttributes["radius"])   : 0.0f;
    float fontsize     = node->mAttributes["fontsize"] != "" ? Tools::StringToFloat(node->mAttributes["fontsize"]) : 12.0f;
    std::string onclick = "";
    Button *retbutton = new Button(ivec2(0,0), ivec2(1,1), "", font);
    if(onclick != "")
    {
        /*script *currentScript = scripts[onclick];
        if(currentScript == nullptr)
        {
            Gum::Output::error("XMLGUILoader: script '" + onclick + "' does not exist!");
            return retbutton;
        }
        std::function<void()> scriptfunc = std::bind(&script::run, currentScript);
        retbutton->setCallbackFunction(scriptfunc);*/
    }

    return retbutton;
}