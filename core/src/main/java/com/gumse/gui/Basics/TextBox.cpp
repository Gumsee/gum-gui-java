#include "TextBox.h"
#include "Essentials/Unicode.h"
#include <System/Output.h>
#include <System/MemoryManagement.h>

TextBox::TextBox(Gum::Unicode str, Gum::GUI::Font *font, vec2 pos, vec2 size)
{
	this->sType = "TextBox";
	this->vPos = pos;
	this->vSize = size;
	this->sTitle = str;
	this->iAlignment = Alignment::CENTER;

	pBackgroundBox = new Box(vec2(0,0), ivec2(100,100));
    pBackgroundBox->setSizeInPercent(true, true);
	pBackgroundBox->setColor(vec4(0.3, 0.3, 0.3, 1));
	addElement(pBackgroundBox);

	pText = new Text(str, font, ivec2(50, 50), 0);
    pText->setPositionInPercent(true, true);
    pText->setOrigin(pText->getSize() / 2.0f);
	addElement(pText);

	resize();
	reposition();
}

TextBox::~TextBox() 
{
	Gum::_delete(pBackgroundBox);
	Gum::_delete(pText);
	Gum::_delete(value);
}

void TextBox::update()
{
	updatechildren();
}

void TextBox::updateText()
{
	pText->applyStringChanges();
    pText->setOrigin(pText->getSize() / 2.0f);
    pText->reposition();
}


void TextBox::updateOnPosChange()
{
	pText->setRenderBox(bbox2i(vActualPos, vActualSize));
}

void TextBox::updateOnSizeChange()
{
	switch(iAlignment)
	{
		case Alignment::LEFT:
    		pText->setOrigin(ivec2(0, pText->getSize().y / 2.0f) + v2TextOffset);
    		pText->setPosition(ivec2(0,50));
			break;

		case Alignment::CENTER:
    		pText->setOrigin(pText->getSize() / 2.0f + v2TextOffset);
    		pText->setPosition(ivec2(50,50));
			break;

		case Alignment::RIGHT:
    		pText->setOrigin(pText->getSize() * vec2(1.0f, 0.5f) + v2TextOffset);
    		pText->setPosition(ivec2(100,50));
			break;
	};
	pText->setRenderBox(bbox2i(vActualPos, vActualSize));
    pText->reposition();
}

void TextBox::updateOnColorChange()
{
	this->pBackgroundBox->setColor(v4Color);
}

void TextBox::setString(Gum::Unicode str)
{
	pText->setString(str);
	updateText();
}

void TextBox::setAlignment(Alignment alignment) { this->iAlignment = alignment; updateOnSizeChange(); }
void TextBox::setTextOffset(ivec2 offset)		{ this->v2TextOffset = offset; updateOnSizeChange(); }
void TextBox::setTexture(Texture *tex) 			{ this->pBackgroundBox->setTexture(tex); }
void TextBox::setTextColor(vec4 color)      	{ this->pText->setColor(color); }
void TextBox::setTextSize(int size)				{ this->pText->setCharacterHeight(size); }
void TextBox::setMaxTextlength(int length)		{ this->pText->setMaxLength(length); }

ivec2 TextBox::getTextSize()					{ return this->pText->getSize(); }
bool TextBox::isMouseInside() 					{ return this->pBackgroundBox->isMouseInside(); }
Texture* TextBox::getTexture() 					{ return this->pBackgroundBox->getTexture(); }
Text* TextBox::getText()						{ return this->pText; }
ivec2 TextBox::getTextOffset()					{ return this->v2TextOffset; }
Box* TextBox::getBox()							{ return this->pBackgroundBox; }
Gum::Unicode& TextBox::getString()				{ return this->pText->getString(); }