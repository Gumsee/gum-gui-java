#include "VerticalSlider.h"
#include "../Font/FontManager.h"
#include <iomanip>
#include <sstream>

VerticalSlider::VerticalSlider(vec2 pos, float length, std::string name, int varPrecision, int RoundCorner)
{
	middle = new Box(pos, vec2(20, length));
	middle->setCornerRadius(RoundCorner);
	middle->setColor(vec4(0.7,0.7,0.7,1.0));


	SliderRect = new Box(pos + vec2(0, length), vec2(20, 0));
	SliderRect->setCornerRadius(RoundCorner);
	SliderRect->setColor(vec4(0.4, 0.4, 0.4, 1));
	Variable = new float(0.0f);

	variableText = new Text("0", Gum::GUI::Fonts->getFont("arimo.ttf"), pos);
	variableText->setCharacterHeight(10);
	variableText->setPosition(pos + vec2(middle->getSize().x / 2 - variableText->getSize().x / 2, middle->getSize().y - variableText->getSize().y - 5));

	//NameText = new Text(name, GumEngine::Fonts->getFont("arimo.ttf"), pos + vec2(5, 0));
	//NameText->setCharacterHeight(25);

	this->setType("vertslider");
	this->setPosition(pos);
	this->varPrecision = varPrecision;

	this->setSize(vec2(length, 10));
	this->setPosition(pos);
}


VerticalSlider::~VerticalSlider(){}

void VerticalSlider::update()
{
	if (Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().x <= middle->getSize().x + middle->getPosition().x &&
	Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().x >= middle->getPosition().x && 
	Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().y <= middle->getPosition().y + middle->getSize().y && 
	Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().y >= middle->getPosition().y )
	{
		if (Gum::Window::CurrentlyBoundWindow->getMouse()->hasLeftClick())
		{
			SliderRect->setSize(vec2(SliderRect->getSize().x, -(Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().y - (middle->getPosition().y + middle->getSize().y))));
			SliderRect->setPosition(middle->getPosition() + vec2(0, middle->getSize().y - SliderRect->getSize().y));
			*Variable = (-(Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().y - (middle->getPosition().y + middle->getSize().y)) / (middle->getSize().y / 100.0f)) / 100.0f;

			std::stringstream sstream;
			sstream << std::fixed << std::setprecision(varPrecision) << (*Variable * viewMultiplier);

			variableText->setString(sstream.str());
			variableText->setPosition(vPos + vec2(middle->getSize().x / 2 - variableText->getSize().x / 2, middle->getSize().y - variableText->getSize().y - 5));
		}
	}
}

void VerticalSlider::render()
{
	middle->render();
	SliderRect->render();
	variableText->render();
	//NameText->render(shader);
}

float VerticalSlider::getData()
{
	return *Variable;
}

void VerticalSlider::setLineColor(vec4 col)
{
	middle->setColor(col);
}

void VerticalSlider::setSliderColor(vec4 col)
{
	SliderRect->setColor(col);
}

void VerticalSlider::IncreasePosition(vec2 NewPosition)
{
	/*this->pos += NewPosition;
	middle->setPosition(vec2(middle->getPosition().x + NewPosition.x, middle->getPosition().y + NewPosition.y));
	SliderRect->setPosition(middle->getPosition() + vec2(0, middle->getSize().y));
	//NameText->setPosition(middle->getPosition() + vec2(5, 0));
	variableText->setPosition(pos + vec2(middle->getSize().x / 2 - variableText->getSize().x / 2, middle->getSize().y - variableText->getSize().y - 5));*/
}

float VerticalSlider::getLengh()
{
	return middle->getSize().x;
}

void VerticalSlider::setLengh(float NewLengh)
{
	middle->setSize(vec2(middle->getSize().x, NewLengh));
}

void VerticalSlider::IncreaseLengh(float NewLengh)
{
	middle->setSize(vec2(middle->getSize().x, middle->getSize().y + NewLengh));
}

void VerticalSlider::setData(float NewData)
{
	SliderRect->setSize(vec2(SliderRect->getSize().x, NewData * (middle->getSize().y / 100)));
	SliderRect->setPosition(middle->getPosition() + vec2(0, middle->getSize().y - SliderRect->getSize().y));
	*Variable = NewData / 100;
	std::stringstream sstream;
	sstream << std::fixed << std::setprecision(varPrecision) << (*Variable * viewMultiplier);

	variableText->setString(sstream.str());
	variableText->setPosition(vPos + vec2(middle->getSize().x / 2 - variableText->getSize().x / 2, middle->getSize().y - variableText->getSize().y - 5));
}

void VerticalSlider::updateData()
{
	SliderRect->setSize(vec2(SliderRect->getSize().x, -(*Variable * middle->getSize().y)));

	std::stringstream sstream;
	sstream << std::fixed << std::setprecision(varPrecision) << (*Variable * viewMultiplier);
	variableText->setString(sstream.str());
	variableText->setPosition(vPos + vec2(middle->getSize().x / 2 - variableText->getSize().x / 2, middle->getSize().y - variableText->getSize().y - 5));
}

void VerticalSlider::setPosition(vec2 pos)
{
	/*if(!boundToParent || parent == nullptr) 
    {
		middle->setPosition(pos);
		SliderRect->setPosition(pos + vec2(0, middle->getSize().y));
		//NameText->setPosition(middle->getPosition() + vec2(5, 0));
		variableText->setPosition(pos + vec2(middle->getSize().x / 2 - variableText->getSize().x / 2, middle->getSize().y - variableText->getSize().y - 5));

		this->pos = pos;
    } 
    else 
    {
        //offsetToParent = gui->getPosition();
        //this->pos = offsetToParent + pos;
        offsetToParent = pos;
        reposition();
    } 
	for(int i = 0; i < children.size(); i++)
	{
		children[i]->reposition();
	}*/
}

void VerticalSlider::reposition()
{
	/*if(parent != nullptr)
	{
		pos = offsetToParent + parent->getPosition();
	}
	middle->setPosition(this->pos);
	SliderRect->setPosition(this->pos + vec2(0, middle->getSize().y));
	//NameText->setPosition(middle->getPosition() + vec2(5, 0));
	variableText->setPosition(pos + vec2(middle->getSize().x / 2 - variableText->getSize().x / 2, middle->getSize().y - variableText->getSize().y - 5));*/
}

void VerticalSlider::setSize(vec2 size)
{
	this->vSize.x = size.x;
	middle->setSize(vec2(this->vSize.x, middle->getSize().y));

	SliderRect->setSize(vec2(SliderRect->getSize().x, -(*Variable * (middle->getSize().y / 100))));

	variableText->setPosition(vPos + vec2(middle->getSize().x / 2 - variableText->getSize().x / 2, middle->getSize().y - variableText->getSize().y - 5));
}