#pragma once
#include "../Primitives/Box.h"
#include <Essentials/SmoothFloat.h>

class Scroller : public RenderGUI
{
private:
	Box* pScrollBar;
	Box* pScrollIndicator;
	RenderGUI* pContent;
	RenderGUI* pMainChildContainer;

	int iMaxValue;
	int iStepSize;
	int iIndicatorPos;
    bool bSnapped;
	bool bHasOverflow;
	int iSnapOffset;

    void moveContent();

protected:
    void updateOnAddGUI(RenderGUI* gui);


public:
	Scroller(ivec2 position, ivec2 size);
	~Scroller();

	void update();
	void render();

	void updateContent();

	void setStepSize(int step);
	void setMainChildContainer(RenderGUI* gui);
	int getOffset();

    static Scroller* createFromXMLNode(XMLNode* node);
};