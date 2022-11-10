#pragma once
#include "../Primitives/Box.h"
#include "../Primitives/Text.h"
#include <functional>

class Slider : public RenderGUI
{
private:
	Box *pBackground;
	Box *pSliderRect;

	Text *pVariableText;
	Text *pNameText;

	float fViewMultiplier = 1;
	float *pValuePtr;
	int iPrecision = 1;
    bool bSnapped;
	bool bInfinite;

	std::function<void(float)> callbackFunction = nullptr;
	std::string sUnit;

protected:
    void updateOnSizeChange();
    void updateOnCornerRadiusChange();

public:
	Slider(ivec2 pos, float length, std::string name, int varPrecision = 1);
	~Slider();

	void update();

	void updateData();
	float getData();
	float getLengh();
	float *getValuePtr();
	std::string getUnit();

	void setInfinite(bool infinite);
	void setUnit(std::string unit);
	void setLineColor(vec4 col);
	void setSliderColor(vec4 col);
	void setLengh(float NewLengh);
	void IncreaseLengh(float NewLengh);
	void setData(float NewData);
	void setViewMultiplier(float multiplier);
	void setValuePtr(float *valptr);
	void setCallbackFunction(std::function<void(float)> func);

    static Slider* createFromXMLNode(XMLNode* node);
};

