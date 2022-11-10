#include "RightClickMenu.h"
#include "Essentials/Tools.h"

RightClickMenu::RightClickMenu(vec2 pos, vec2 entrysize, vec2 size, float cornerRadius)
{
    this->vEntrySize = entrysize;
    this->vPos = pos;
    this->vSize = size;
    this->fCornerRadius = cornerRadius;

    this->bIsOpen = false;
    this->setType("rightclickmenu");
}

RightClickMenu::~RightClickMenu() {}

void RightClickMenu::update()
{
    if(bIsOpen)
        updatechildren();
        
    if(isMouseInside())
    {
        Gum::IO::Mouse* mouse = Gum::Window::CurrentlyBoundWindow->getMouse();
        if(mouse->hasLeftRelease())
            bIsOpen = false;
        
        if(mouse->hasRightRelease())
        {
            ivec2 mousepos = mouse->getPosition();
            int halfscreen = Gum::Window::CurrentlyBoundWindow->getSize().y / 2;
            if(mousepos.y < halfscreen)
                setPosition(mousepos - vPos);
            else
                setPosition(mousepos - vPos - vec2(0, vEntries.size() * vEntrySize.y));
            bIsOpen = !bIsOpen;
        }
    }
}

void RightClickMenu::render()
{
    if(bIsOpen)
        renderchildren();

}

void RightClickMenu::buildEntries()
{
    if(vEntries.size() > 0)
    {
        if(vEntries.size() == 1)
        {
            vEntries[0]->setSize(vEntrySize);
            vEntries[0]->getBox()->getBox()->setCornerRadius(fCornerRadius);
        }
        else
        {
            vEntries[0]->setSize(vEntrySize);
            vEntries[0]->getBox()->getBox()->setCornerRadius(vec4(fCornerRadius, fCornerRadius, 0.0f, 0.0f));
            for(size_t i = 1; i < vEntries.size() - 1; i++)
            {
                vEntries[i]->setSize(vEntrySize);
                vEntries[i]->setPosition(vec2(0, vEntrySize.y * i - 1));
                vEntries[i]->getBox()->getBox()->setCornerRadius(0.0f);
            }   
            vEntries[vEntries.size() - 1]->setSize(vEntrySize);
            vEntries[vEntries.size() - 1]->setPosition(vec2(0, vEntrySize.y * (vEntries.size() - 1) - 1));
            vEntries[vEntries.size() - 1]->getBox()->getBox()->setCornerRadius(vec4(0.0f, 0.0f, fCornerRadius, fCornerRadius));
        }
        
    }
}

void RightClickMenu::addEntry(Button *entry) 
{ 
    addGUI(entry); 
    vEntries.push_back(entry); 
    buildEntries(); 
}
void RightClickMenu::removeEntry(int index) 
{ 
    removeChild(index); 
    vEntries.erase(vEntries.begin() + index); 
    buildEntries(); 
}


void RightClickMenu::setEntrySize(vec2 size)     { this->vEntrySize = size; }

Button* RightClickMenu::getEntry(int index)      { return vEntries[index]; }