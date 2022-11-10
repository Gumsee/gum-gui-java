#pragma once
#include <Essentials/Clock.h>
#include <Essentials/Unicode.h>
#include <functional>

#include "TextBox.h"

class TextField : public RenderGUI
{
private:
	TextBox* pBackgroundBox;
	Box* pIndicatorBox;
	Box* pSelectionBox;
	int iIndicatorCharIndex;
	int iSelectorStartCharIndex, iSelectorEndCharIndex;
	bool bIsEditing;
	bool bActivateOnDoubleclick;
	Clock* pClock;
	int iTextScrollRightOffset;
	uint8_t uiCursorShape;
	
	Gum::Unicode sOrigString;
	std::function<void(Gum::Unicode str)> pReturnFunc;

	void updateText();

protected:
    void updateOnColorChange();
    void updateOnPosChange();
    void updateOnSizeChange();

public:
	TextField(Gum::Unicode str, Gum::GUI::Font* font, vec2 pos, vec2 size);
    ~TextField();
    
	void update();
	void render();

	void appendString(Gum::Unicode utf8);
	void backspaceString(int backspaces = 1);
	void moveIndicator(int direction);
	void setIndicator(int pos);
	void setSelection(int from, int to);
	void selectAll();
	void finishEditing();

	//Setter
	void setString(Gum::Unicode utf8);
	void setCursorShapeOnHover(uint8_t shape);
	void shouldActivateOnDoubleclick(bool activate);
	void setSelectionColor(vec4 color);
	void setIndicatorColor(vec4 color);
	void setTextColor(vec4 color);
	void setReturnCallback(std::function<void(Gum::Unicode str)> func);

	//Getter
	TextBox *getBox();
	bool isEditing();
};