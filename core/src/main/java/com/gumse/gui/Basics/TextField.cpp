#include "TextField.h"
#include "TextBox.h"

#include <System/Output.h>
#include <System/MemoryManagement.h>

#include <Essentials/Unicode.h>
#include <Maths/Maths.h>
#include <OpenGL/WrapperFunctions.h>

TextField::TextField(Gum::Unicode str, Gum::GUI::Font* font, vec2 pos, vec2 size)
{
	this->vPos = pos;
	this->vSize = size;
	this->sType = "TextField";
	this->bIsEditing = false;
	this->bActivateOnDoubleclick = false;
	this->iIndicatorCharIndex = 0;
	this->pReturnFunc = nullptr;
	this->uiCursorShape = GUM_CURSOR_IBEAM;

	iTextScrollRightOffset = 30;

	pBackgroundBox = new TextBox(str, font, ivec2(0, 0), ivec2(100, 100));
	pBackgroundBox->setSizeInPercent(true, true);
    pBackgroundBox->setTextColor(vec4(0.76, 0.76, 0.76, 1));
	pBackgroundBox->setAlignment(TextBox::LEFT);
	addElement(pBackgroundBox);

	pIndicatorBox = new Box(ivec2(0,0), ivec2(2, 100));
	pIndicatorBox->setSizeInPercent(false, true);
    pIndicatorBox->setColor(vec4(0.76, 0.76, 0.76, 1));
	pIndicatorBox->hide(true);
	addElement(pIndicatorBox);

	pSelectionBox = new Box(ivec2(0, 0), ivec2(0, 100));
	pSelectionBox->setSizeInPercent(false, true);
    pSelectionBox->setColor(vec4(0.34, 0.5, 0.76, 0.3));
	addElement(pSelectionBox);

	pClock = new Clock();
	pClock->runEveryNMilliSeconds([this]() {
		if(bIsEditing && iSelectorStartCharIndex == iSelectorEndCharIndex)
			pIndicatorBox->hide(!pIndicatorBox->isHidden());
	}, 1000);

	resize();
	reposition();
	
	pBackgroundBox->setTextSize(getSize().y * 0.9f);
	pBackgroundBox->setTextOffset(ivec2(0, 7));
}


TextField::~TextField()
{
	Gum::_delete(pBackgroundBox);
	Gum::_delete(pSelectionBox);
	Gum::_delete(pIndicatorBox);
	Gum::_delete(pClock);
}

void TextField::updateOnColorChange()
{
	pBackgroundBox->setColor(v4Color);
}

void TextField::updateOnPosChange()
{
	updateText();
}

void TextField::updateOnSizeChange()
{
	updateText();
}


void TextField::render()
{
	pBackgroundBox->render();
	if(iSelectorStartCharIndex != iSelectorEndCharIndex)
	{
        glEnable(GL_SCISSOR_TEST);
        gumScissor(bbox2i(ivec2(vActualPos.x, Gum::Window::CurrentlyBoundWindow->getSize().y - vActualPos.y - vActualSize.y), vActualSize));
		pSelectionBox->render();
        Gum::Window::CurrentlyBoundWindow->resetViewport();
        glDisable(GL_SCISSOR_TEST);
	}
	pIndicatorBox->render();
}

void TextField::update()
{
	if(RenderGUI::somethingHasBeenClicked())
		return;

	Gum::IO::Mouse* mouse = Gum::Window::CurrentlyBoundWindow->getMouse();
	Gum::IO::Keyboard* keyboard = Gum::Window::CurrentlyBoundWindow->getKeyboard();
	
	if(isMouseInside())
	{
		mouse->setCursor(uiCursorShape);
		if(bIsEditing)
		{
			mouse->setCursor(uiCursorShape);
			if(mouse->hasLeftDoubleClick())
			{
				selectAll();
			}
			else if(mouse->hasLeftClickStart())
			{
				Gum::IO::Mouse::setBusiness(true);
				int point = pBackgroundBox->getText()->getClosestCharacterIndex(mouse->getPosition());
				setSelection(point, point);
			}
			else if(mouse->hasLeftClick())
			{
				int point = pBackgroundBox->getText()->getClosestCharacterIndex(mouse->getPosition());
				setSelection(iSelectorStartCharIndex, point);
			}
			else if(mouse->hasLeftRelease())
			{
				Gum::IO::Mouse::setBusiness(false);
			}
		}
		
		if(!Gum::IO::Mouse::isBusy())
		{
			if((!bActivateOnDoubleclick && isClicked()) || 
			   ( bActivateOnDoubleclick && mouse->hasLeftDoubleClick()))
			{
				if(!bIsEditing)
				{
					bIsEditing = true;
					sOrigString = pBackgroundBox->getString();
					selectAll();
				}
			}
		}
	}
	else
	{
		if(bIsEditing && Gum::Window::CurrentlyBoundWindow->getMouse()->isBusy() && mouse->hasLeftClick())
		{
			int dir = 0;
			if(mouse->getPosition().x < pBackgroundBox->getPosition().x)
				dir = -1;
			else if(mouse->getPosition().x > pBackgroundBox->getPosition().x + pBackgroundBox->getSize().x)
				dir = 1;
			
			setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex + dir);
		}
		else
		{
			if(mouse->hasLeftClick())
			{
				finishEditing();
			}
		}
	}

	if(bIsEditing)
	{
		pClock->update();
		if     (keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_BACKSPACE))            { int spaces = abs(iSelectorEndCharIndex - iSelectorStartCharIndex); backspaceString(spaces > 0 ? spaces : 1); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_DELETE))               { int spaces = abs(iSelectorEndCharIndex - iSelectorStartCharIndex); backspaceString(spaces > 0 ? spaces : -1); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_ENTER)) 	             { finishEditing(); if(pReturnFunc != nullptr) pReturnFunc(pBackgroundBox->getString()); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_ESCAPE)) 	             { finishEditing(); setString(sOrigString); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_LEFT)) 	             { moveIndicator(-1); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_RIGHT)) 	             { moveIndicator(1); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_END)) 	             { setIndicator(pBackgroundBox->getString().length()); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_HOME)) 	             { setIndicator(0); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_LEFT, Gum::IO::GUM_MOD_SHIFT))  { setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex - 1); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_RIGHT, Gum::IO::GUM_MOD_SHIFT)) { setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex + 1); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_END, Gum::IO::GUM_MOD_SHIFT)) 	 { setSelection(iSelectorStartCharIndex, pBackgroundBox->getString().length()); }
		else if(keyboard->checkLastPressedKey(Gum::IO::GUM_KEY_HOME, Gum::IO::GUM_MOD_SHIFT))  { setSelection(iSelectorStartCharIndex, 0); }
		else if(keyboard->getTextInput() != "") 				   		     { appendString(keyboard->getTextInput()); }
	}

	updatechildren();
}

void TextField::updateText()
{
	pBackgroundBox->updateText();
	//pBackgroundBox->setTextSize(getSize().y * 0.9f);
	pBackgroundBox->setTextOffset(ivec2(0, pBackgroundBox->getTextOffset().y));
}

void TextField::moveIndicator(int direction)
{
	setIndicator(iIndicatorCharIndex + direction);
}

void TextField::setIndicator(int pos)
{
	iIndicatorCharIndex = pos;
	Gum::Maths::clamp(iIndicatorCharIndex, 0, (int)pBackgroundBox->getString().length());

	//Selector reset
	pSelectionBox->hide(true);
	iSelectorStartCharIndex = iIndicatorCharIndex;
	iSelectorEndCharIndex = iIndicatorCharIndex;

	//Indicator part
	unsigned int boxpos = pBackgroundBox->getText()->getTextSize(pBackgroundBox->getString(), 0, iIndicatorCharIndex).x;
	pIndicatorBox->setPosition(ivec2(boxpos - pBackgroundBox->getText()->getOrigin().x, 0)); 
	pIndicatorBox->hide(false);

	int rightoffset = iTextScrollRightOffset;
	if(rightoffset > pBackgroundBox->getSize().x * 0.25f)
		rightoffset = pBackgroundBox->getSize().x * 0.25f;

	int distance = pBackgroundBox->getSize().x - boxpos + pBackgroundBox->getText()->getOrigin().x;
	if(distance < rightoffset)
	{
		pBackgroundBox->setTextOffset(pBackgroundBox->getTextOffset() - ivec2(distance - rightoffset, 0));
		pIndicatorBox->setPosition(ivec2(pBackgroundBox->getSize().x - rightoffset, 0));
	}
	if(distance > pBackgroundBox->getSize().x)
	{
		pBackgroundBox->setTextOffset(pBackgroundBox->getTextOffset() - ivec2(distance - pBackgroundBox->getSize().x, 0));
		pIndicatorBox->setPosition(ivec2(0, 0));
	}
}

void TextField::appendString(Gum::Unicode utf8)
{
	Gum::Maths::clamp(iIndicatorCharIndex, 0, (int)pBackgroundBox->getString().length());
	backspaceString(abs(iSelectorEndCharIndex - iSelectorStartCharIndex));
	
	pBackgroundBox->getText()->getString().insert(utf8, iIndicatorCharIndex);
	updateText();
	setIndicator(iIndicatorCharIndex + 1);
}

void TextField::backspaceString(int backspaces)
{
	int startingpoint = iSelectorEndCharIndex;
	if(startingpoint < iSelectorStartCharIndex)
		startingpoint = iSelectorStartCharIndex;

	if(backspaces < 0)
	{
		backspaces = -backspaces;
		startingpoint += backspaces;
	}

	if(backspaces > (int)startingpoint)
		return;

	pBackgroundBox->getText()->getString().erase(startingpoint - backspaces, backspaces);
	updateText();
	setIndicator(startingpoint - backspaces);
}

void TextField::setSelection(int from, int to)
{
	setIndicator(to);
	iSelectorStartCharIndex = from;
	iSelectorEndCharIndex = to;
	if(to < from)
	{
		int tmp = from;
		from = to;
		to = tmp;
	}

	Gum::Maths::clamp(iSelectorStartCharIndex, 0, (int)pBackgroundBox->getString().length());
	Gum::Maths::clamp(iSelectorEndCharIndex, 0, (int)pBackgroundBox->getString().length());
	Gum::Maths::clamp(from, 0, (int)pBackgroundBox->getString().length());
	Gum::Maths::clamp(to, 0, (int)pBackgroundBox->getString().length());
	pSelectionBox->hide(false);
	pSelectionBox->setPosition(ivec2(pBackgroundBox->getText()->getTextSize(pBackgroundBox->getString(), 0, from).x - pBackgroundBox->getTextOffset().x, 0));
	pSelectionBox->setSize(ivec2(pBackgroundBox->getText()->getTextSize(pBackgroundBox->getString(), from, to).x, 100));
}

void TextField::selectAll()
{
	setSelection(0, pBackgroundBox->getString().length());
}

void TextField::finishEditing()
{
	bIsEditing = false;
	pIndicatorBox->hide(true);
	pSelectionBox->hide(true);
}


//
// Setter
//
void TextField::setString(Gum::Unicode utf8)
{
	pBackgroundBox->setString(utf8);
	updateText();
}

void TextField::setCursorShapeOnHover(uint8_t shape) 						  { this->uiCursorShape = shape; }
void TextField::shouldActivateOnDoubleclick(bool activate)  				  { this->bActivateOnDoubleclick = activate; }
void TextField::setSelectionColor(vec4 color) 								  { this->pSelectionBox->setColor(color); }
void TextField::setIndicatorColor(vec4 color) 								  { this->pIndicatorBox->setColor(color); }
void TextField::setTextColor(vec4 color) 	  								  { this->pBackgroundBox->setTextColor(color); }
void TextField::setReturnCallback(std::function<void(Gum::Unicode str)> func) { this->pReturnFunc = func; }


//
// Getter
//
TextBox* TextField::getBox() { return this->pBackgroundBox; }
bool TextField::isEditing()  { return this->bIsEditing; }