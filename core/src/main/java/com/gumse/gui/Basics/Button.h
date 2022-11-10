#pragma once
#include "TextBox.h"
#include <functional>

class Button : public RenderGUI
{
private:
    TextBox *backgroundBox;

    std::function<void()> callbackFunction = nullptr;
    bool bPressedDown = false;

public:
    Button(ivec2 pos, ivec2 size, std::string title, Gum::GUI::Font* font);
    Button(std::string title);
    ~Button();

    void update();


    //void setTexture(Texture *newtex);
	void setSecondColor(vec4 col);
    void setHasGradient(bool val);
	void setGradientDirectionRight(bool val);
    void setCallbackFunction(std::function<void()> func);

    TextBox *getBox();
	vec4 getSecondColor();
	//Texture* getTexture();

    static Button* createFromXMLNode(XMLNode* node);
};