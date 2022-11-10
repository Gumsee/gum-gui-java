#include "Dropdown.h"
#include <Essentials/Tools.h>
#include "../Font/FontManager.h"

Dropdown::Dropdown(const Gum::Unicode& text, Gum::GUI::Font* pFont, const ivec2& pos, const ivec2& size, const float& textsize)
{
	this->setPosition(pos);
	this->setSize(size);
	this->setType("Dropdown");
	this->iTextSize = textsize;
	this->pFont = pFont;

	pSmoothFloat = new SmoothFloat(1, 10, 1);
	pPreviewTextbox = new TextBox(text, pFont, ivec2(0,0), ivec2(100, 100));
	pPreviewTextbox->setTextSize(textsize);
	pPreviewTextbox->setSizeInPercent(true, true);
	this->addElement(pPreviewTextbox);

	sCurrentString = "";
	iCurrentIndex = 0;

	resize();
	reposition();
}


Dropdown::~Dropdown(){}

void Dropdown::update()
{
	pSmoothFloat->update();

	if(vMenuEntries.size() > 0)
	{
		if(bIsOpen)
		{
			vMenuEntries[0]->box->setPosition(ivec2(0, (pSmoothFloat->get() * vMenuEntries.size() * vMenuEntries[0]->box->getSize().y) - (vMenuEntries.size() - 1) * vMenuEntries[0]->box->getSize().y + fScrollOffset));
		}
		else
		{
			vMenuEntries[0]->box->setPosition(ivec2(0, ((1 - pSmoothFloat->get()) * vMenuEntries.size() * vMenuEntries[0]->box->getSize().y) - (vMenuEntries.size() - 1) * vMenuEntries[0]->box->getSize().y));
		}
		for(size_t i = 0; i < vMenuEntries.size(); i++)
		{
			vMenuEntries[i]->box->reposition();
		}
	}

	if(pSmoothFloat->get() >= 0.99f) { bDone = true; }

	if(bIsOpen)
	{
		//If all entries together are bigger (longer) than then screen
		if(vMenuEntries[0]->box->getSize().y * (int)vMenuEntries.size() > Gum::Window::CurrentlyBoundWindow->getSize().y - pPreviewTextbox->getSize().y)
		{
			//If Mouse is on the dropdown
			if(Tools::checkPointInBox(Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition(), bbox2i(vMenuEntries[0]->box->getPosition(), vec2(vMenuEntries[0]->box->getSize().x, vMenuEntries[0]->box->getSize().y * vMenuEntries.size()))))
			{
				fScrollOffset += Gum::Window::CurrentlyBoundWindow->getMouse()->getMouseWheelState() * vMenuEntries[0]->box->getSize().y;
				if(fScrollOffset > 0) { fScrollOffset = 0; } //Do not move below the dropdown menu
			}
		}
	}

	if(pPreviewTextbox->isMouseInside())
	{
		pPreviewTextbox->setColor(vec4(0.4, 0.4, 0.4, 1));
		if(Gum::Window::CurrentlyBoundWindow->getMouse()->hasLeftClick())
		{
			if(bDone)
			{
				bDone = false;
				this->Switch();	
			}
	 	}
	}
	else
	{
		pPreviewTextbox->setColor(vec4(0.6, 0.6, 0.6, 1));
	}

	bIsClicked = false;
	if(bIsOpen && bDone)
	{
		for(size_t i = 0; i < vMenuEntries.size(); i++)
		{
			if(vMenuEntries[i]->box->getPosition().y > pPreviewTextbox->getPosition().y)  //Only render if entry is underneath the pPreviewTextbox
			{
				if(vMenuEntries[i]->box->isMouseInside())
				{
					vMenuEntries[i]->box->setColor(vec4(0.5, 0.5, 0.5, 1));
					if(Gum::Window::CurrentlyBoundWindow->getMouse()->hasLeftRelease())
					{
						sCurrentString = vMenuEntries[i]->box->getString();
						pPreviewTextbox->setString(sCurrentString);
						iCurrentIndex = i;
						bIsClicked = true;
						close();
						if(vMenuEntries[i]->function != nullptr)
						{
							vMenuEntries[i]->function(vMenuEntries[i]->box->getString());
						}
					}
				}
				else
				{
					vMenuEntries[i]->box->setColor(vec4(0.7, 0.7, 0.7, 1));
				}
			}
		}
	}

}

void Dropdown::render()
{
	for(size_t i = 0; i < vMenuEntries.size(); i++)
	{
		if(vMenuEntries[i]->box->getPosition().y > pPreviewTextbox->getPosition().y)  //Only render if entry is underneath the pPreviewTextbox
		{
			vMenuEntries[i]->box->render();
		}
	}
	renderchildren();
}

void Dropdown::addEntry(const Gum::Unicode& title, std::function<void(Gum::Unicode)> OnCLickFunction, const bool& active)
{
	MenuEntry *entry = new MenuEntry();
	TextBox *box = new TextBox(title, pFont, ivec2(0, pPreviewTextbox->getSize().y), pPreviewTextbox->getSize());
	box->setTextSize(iTextSize);
	box->setColor(vec4(0.7, 0.7, 0.7, 1));
	if(vMenuEntries.size() == 0)
	{
		box->setParent(pPreviewTextbox);
        box->reposition();
	}
	else
	{
		box->setPosition(ivec2(0, pPreviewTextbox->getSize().y * vMenuEntries.size()));

		box->setParent(vMenuEntries[0]->box);
        box->reposition();
	}

	entry->box = box;
	entry->function = OnCLickFunction;
	if(active)
    	this->pPreviewTextbox->setString(title);
	
	vMenuEntries.push_back(entry);
}

void Dropdown::updateOnTitleChange()
{
    this->pPreviewTextbox->setString(sTitle);
}

int Dropdown::numEntries() const					{ return vMenuEntries.size(); }
int Dropdown::getCurrentEntry() const				{ return iCurrentIndex; }
Gum::Unicode Dropdown::getCurrentTitle() const		{ return sCurrentString; }
bool Dropdown::isCurrentClicked() const				{ return bIsClicked; }
vec3 Dropdown::getColor() const						{ return vColor; }
bool Dropdown::isOpen() const						{ return bIsOpen; }

void Dropdown::clearMenu() 							{ vMenuEntries.clear(); }
void Dropdown::close() 								{ bIsOpen = false; pSmoothFloat->reset(); }
void Dropdown::open() 								{ bIsOpen = true; pSmoothFloat->reset(); }
void Dropdown::Switch() 							{ bIsOpen = !bIsOpen; pSmoothFloat->reset(); }

Dropdown* Dropdown::createFromXMLNode(XMLNode* node)
{
	Gum::GUI::Font *font = Gum::GUI::Fonts->getDefaultFont();
	float fontsize     = node->mAttributes["fontsize"] != "" ? Tools::StringToFloat(node->mAttributes["fontsize"]) : 12.0f;
	return new Dropdown("", font, ivec2(0,0), ivec2(0,0), fontsize);
}