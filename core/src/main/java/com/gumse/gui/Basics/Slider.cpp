#include "Slider.h"
#include "../Font/FontManager.h"
#include <System/MemoryManagement.h>
#include "Essentials/Tools.h"
#include "Essentials/Unicode.h"
#include <iostream>
#include <iomanip>
#include <sstream>

Slider::Slider(ivec2 pos, float length, std::string name, int iPrecision)
{
	this->sType = "Slider";
	this->vSize = ivec2(length, 30);
	this->vPos = pos;
	this->iPrecision = iPrecision;
	this->sUnit = "";
    this->bSnapped = false;

	pBackground = new Box(ivec2(0,0), ivec2(100, 100));
    pBackground->setSizeInPercent(true, true);
	pBackground->setColor(vec4(0.7,0.7,0.7,1.0));
    pBackground->setCornerRadius(vec4(15.0f));
	addElement(pBackground);

	pSliderRect = new Box(ivec2(0,0), ivec2(0, 100));
    pSliderRect->setSizeInPercent(true, true);
	pSliderRect->setColor(vec4(0.4, 0.4, 0.4, 1));
	addElement(pSliderRect);

	this->value = new Gum::Unicode("0");
	this->pValuePtr = new float(0.0f);

	pVariableText = new Text("0", Gum::GUI::Fonts->getDefaultFont(), ivec2(0,0));
	pVariableText->setCharacterHeight(15);
	pVariableText->setPosition(ivec2(getSize().x - pVariableText->getSize().x - 10, 5));
	addElement(pVariableText);

	pNameText = new Text(name, Gum::GUI::Fonts->getDefaultFont(), vec2(10, 5));
	pNameText->setCharacterHeight(15.0f);
	addElement(pNameText);

    resize();
    reposition();
}

Slider::~Slider() 
{
    Gum::_delete(pBackground);
    Gum::_delete(pSliderRect);
    Gum::_delete(value);
    Gum::_delete(pValuePtr);
    Gum::_delete(pVariableText);
    Gum::_delete(pNameText);
}

void Slider::update()
{
    if(pBackground->isMouseInside() && !Gum::IO::Mouse::isBusy())
	{
        Gum::Window::CurrentlyBoundWindow->getMouse()->setCursor(GUM_CURSOR_HORIZONTAL_RESIZE);
	    pBackground->setColor(vec4(0.6, 0.6, 0.6, 1.0));
	    pSliderRect->setColor(vec4(0.3, 0.3, 0.3, 1.0));

        if(hasClickedInside())
        {
            bSnapped = true;
            Gum::IO::Mouse::setBusiness(true);
        }
	}
    else 
    {
        pBackground->setColor(vec4(0.7, 0.7, 0.7, 1.0));
        pSliderRect->setColor(vec4(0.4, 0.4, 0.4, 1.0));
    }

    if(bSnapped)
    {
	    pBackground->setColor(vec4(0.6, 0.6, 0.6, 1.0));
	    pSliderRect->setColor(vec4(0.3, 0.3, 0.3, 1.0));
        float newvar = *pValuePtr;
        if(bInfinite)
        {
            float speed = 0.1f;
            if(Gum::Window::CurrentlyBoundWindow->getKeyboard()->checkKeyPressed(Gum::IO::GUM_KEY_LEFT_CONTROL))
                speed *= 10;
            else if(Gum::Window::CurrentlyBoundWindow->getKeyboard()->checkKeyPressed(Gum::IO::GUM_KEY_LEFT_SHIFT))
                speed *= 0.01;
                
            Gum::IO::Mouse::freeze(true);
            Gum::Window::CurrentlyBoundWindow->getMouse()->hide(true);
            newvar += (Gum::IO::Mouse::getDelta().x + Gum::Window::CurrentlyBoundWindow->getMouse()->getMouseWheelState()) * speed;
        }
        else 
        {
            newvar = (float)(Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().x - getPosition().x) / (float)getSize().x;
            Gum::Maths::clamp(newvar, 0.0f, 1.0f);
        }
        if(newvar != *pValuePtr)
        {
            *pValuePtr = newvar;
            updateData();
            if(callbackFunction != nullptr) { callbackFunction(*pValuePtr); }
        }
        if(!Gum::Window::CurrentlyBoundWindow->getMouse()->hasLeftClick())
        {
            bSnapped = false;
            Gum::IO::Mouse::freeze(false);
            Gum::Window::CurrentlyBoundWindow->getMouse()->hide(false);
            Gum::IO::Mouse::setBusiness(false);
        }
    }
}

void Slider::updateData()
{
	*value = std::to_string(*pValuePtr);
	std::stringstream sstream;
	sstream << std::fixed << std::setprecision(iPrecision) << (*pValuePtr * fViewMultiplier);

	pVariableText->setString(sstream.str() + sUnit);
	pVariableText->setPosition(ivec2(getSize().x - pVariableText->getSize().x - 10, 5));

    if(bInfinite)
    {
        pSliderRect->setSize(ivec2(0,0));
    }
    else 
    {
        pSliderRect->setSize(ivec2((int)(*pValuePtr * 100.0f), 100));
        if(pSliderRect->getSize().x > getSize().x - getSize().y * 0.5f)
        {
            pSliderRect->setCornerRadius(pBackground->getCornerRadius());
        }
        else
        {
            pSliderRect->setCornerRadius(vec4(pBackground->getCornerRadius().x, 0.0f, 0.0f, pBackground->getCornerRadius().w));
        }
    }
}

void Slider::updateOnSizeChange()
{
	pVariableText->setPosition(ivec2(getSize().x - pVariableText->getSize().x - 10, 5));
}

void Slider::updateOnCornerRadiusChange()
{
    pBackground->setCornerRadius(v4CornerRadius);
}


//
// Getter
//
float Slider::getData() 											{ return *pValuePtr; }
float Slider::getLengh() 											{ return this->vActualSize.x; }
float *Slider::getValuePtr()										{ return this->pValuePtr; }
std::string Slider::getUnit()										{ return sUnit; }


//
// Setter
//
void Slider::setInfinite(bool infinite)                             { this->bInfinite = infinite; }
void Slider::setUnit(std::string unit)								{ this->sUnit = unit; updateData(); }
void Slider::setData(float NewData) 								{ *this->pValuePtr = NewData; updateData(); }
void Slider::setLineColor(vec4 col)							        { this->pBackground->setColor(col); }
void Slider::setSliderColor(vec4 col) 							    { this->pSliderRect->setColor(col); }
void Slider::setLengh(float NewLengh) 								{ this->pBackground->setSize(vec2(NewLengh, pBackground->getSize().y)); }
void Slider::IncreaseLengh(float NewLengh) 							{ this->pBackground->setSize(vec2(pBackground->getSize().x + NewLengh, pBackground->getSize().y)); }
void Slider::setViewMultiplier(float multiplier) 					{ this->fViewMultiplier = multiplier; }
void Slider::setValuePtr(float *valptr)								{ this->pValuePtr = valptr; }
void Slider::setCallbackFunction(std::function<void(float)> func) 	{ this->callbackFunction = func; }



Slider* Slider::createFromXMLNode(XMLNode* node)
{
    float length        = node->mAttributes["length"]        != "" ? Tools::StringToFloat(Tools::strExtractNumbers(node->mAttributes["length"]))     : 10.0f;
    int precision       = node->mAttributes["precision"]     != "" ? Tools::StringToInt(Tools::strExtractNumbers(node->mAttributes["precision"]))    : 1;
    float multiplier    = node->mAttributes["multiplier"]    != "" ? Tools::StringToFloat(Tools::strExtractNumbers(node->mAttributes["multiplier"])) : 1.0f;
    bool infiniteslider = node->mAttributes["infinite"] == "true";
    std::string unit    = node->mAttributes["unit"];
    std::string name    = node->mAttributes["title"];

    Slider* slidergui = new Slider(ivec2(0,0), length, name, precision);
    slidergui->setSizeInPercent(Tools::strContains(node->mAttributes["length"], "%"), false);
    slidergui->setViewMultiplier(multiplier);
    slidergui->setInfinite(infiniteslider);
    slidergui->setUnit(unit);
    return slidergui;
}