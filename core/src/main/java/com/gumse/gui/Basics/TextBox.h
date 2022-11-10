#pragma once
#include "../Primitives/Box.h"
#include "../Primitives/Text.h"
#include "Essentials/Unicode.h"

class TextBox : public RenderGUI
{
public:
	enum Alignment {
		LEFT,
		CENTER,
		RIGHT,
	};

private:
    Box	*pBackgroundBox;
	Text *pText;
    
	Gum::Unicode CurrentText;
	Alignment iAlignment;
	ivec2 v2TextOffset;

protected:
    void updateOnPosChange();
    void updateOnSizeChange();
    void updateOnColorChange();

public:
	TextBox(Gum::Unicode str, Gum::GUI::Font* font, vec2 pos, vec2 size);
    ~TextBox();
    
	void update();
	void updateText();

	void setString(Gum::Unicode str);
	void setAlignment(Alignment alignment);
	void setTextOffset(ivec2 offset);
	void setTextColor(vec4 color);
	void setTexture(Texture *tex);
	void setTextSize(int size);
	void setMaxTextlength(int length);

	ivec2 getTextSize();
	Texture* getTexture();
	bool isMouseInside();
	Gum::Unicode& getString();
	ivec2 getTextOffset();
	Text* getText();
	Box *getBox();
};