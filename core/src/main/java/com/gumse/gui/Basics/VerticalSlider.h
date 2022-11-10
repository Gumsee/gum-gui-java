#pragma once
#include "../Primitives/Box.h"
#include "../Primitives/Text.h"

class VerticalSlider : public RenderGUI
{
private:
	Box *middle;
	Box *SliderRect;

	Text *variableText;
	Text *NameText;

	float *Variable;
	int viewMultiplier = 1;
	int varPrecision = 0;

public:
	VerticalSlider(vec2 pos, float length, std::string name, int varPrecision = 0, int RoundCorner = 20);
	~VerticalSlider();


	void render();
	void update();

	float getData();

	void setLineColor(vec4 col);
	void setSliderColor(vec4 col);

	void IncreasePosition(vec2 NewPosition);

	float getLengh();
	void setLengh(float NewLengh);
	void IncreaseLengh(float NewLengh);
	void setData(float NewData);
	void setPosition(vec2 pos);
	void setSize(vec2 size);
	void bindData(float *data)
	{
		Variable = data;
	}

	void updateData();

	void setViewMultiplier(int multiplier)
	{
		this->viewMultiplier = multiplier;
	}

	void reposition();
};

