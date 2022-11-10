#include "Scroller.h"
#include <System/MemoryManagement.h>
#include <System/Output.h>
#include "Maths/Maths.h"
#include <gum-maths.h>
#include <OpenGL/WrapperFunctions.h>

#include <iostream>

Scroller::Scroller(ivec2 position, ivec2 size)
{
	this->sType = "Scroller";
	this->vSize = size;
	this->vPos = position;
    this->vMargin.x = -20;
    this->bSnapped = false;
    this->iIndicatorPos = 0;
    this->iStepSize = 30;
	this->iMaxValue = 0;
	this->bHasOverflow = false;

    pContent = new RenderGUI();
    pContent->setSize(ivec2(100, 100));
    pContent->setSizeInPercent(true, true);
    pContent->shouldKeepTrackOfBoundingbox(true);
    addElement(pContent);
    this->pMainChildContainer = pContent;

	pScrollBar = new Box(ivec2(100,5), ivec2(10, 100));
    pScrollBar->setCornerRadius(vec4(8.0f));
    pScrollBar->setPositionInPercent(true, false);
    pScrollBar->setSizeInPercent(false, true);
    pScrollBar->setOrigin(ivec2(15, 0));
    pScrollBar->setMargin(ivec2(0, -10));
	pScrollBar->setColor(vec4(0.4,0.4,0.4,1));
    addElement(pScrollBar);

    pScrollIndicator = new Box(ivec2(0,0), ivec2(10, 10));
    pScrollIndicator->setCornerRadius(vec4(8.0f));
    //pScrollIndicator->setPositionInPercent(false, true);
    pScrollIndicator->setSizeInPercent(false, true);
	pScrollIndicator->setColor(vec4(0.19, 0.2, 0.42, 1.0));
    pScrollBar->addGUI(pScrollIndicator);

    resize();
    reposition();
}


Scroller::~Scroller() 
{
    Gum::_delete(pScrollBar);
    Gum::_delete(pScrollIndicator);
}

void Scroller::moveContent()
{
    for(unsigned int i = 0; i < pMainChildContainer->numChildren(); i++)
    {   
        RenderGUI* child = pMainChildContainer->getChild(i);
        float ypos = child->getPosition().y;
        //Hide if outside
        child->hide(ypos + child->getSize().y < vActualPos.y + vOrigin.y ||
                    ypos > vActualPos.y + vActualSize.y + vOrigin.y); 
    }

    float contentHeight = pContent->getBoundingBox().size.y - getSize().y;
    if(contentHeight == 0.0f)
    {
        bHasOverflow = false;
        pContent->setPosition(ivec2(0, 0));
        return;
    }
    bHasOverflow = true;
    float sizepercent = (float)pContent->getSize().y / contentHeight;
    pScrollIndicator->setSize(ivec2(pScrollIndicator->getSize().x, sizepercent * 100));

    iMaxValue = pScrollBar->getSize().y - pScrollIndicator->getSize().y;
    Gum::Maths::clamp(iIndicatorPos, 0, std::max(iMaxValue, 0));
    
    pScrollIndicator->setPosition(ivec2(0, iIndicatorPos));
    pContent->setPosition(ivec2(0, -(iIndicatorPos * contentHeight) / iMaxValue));
}

void Scroller::update()
{
    if(!bIsHidden)
    {
        if(!Gum::IO::Mouse::isBusy())
        {
            if(isMouseInside())
            {
                pScrollIndicator->setColor(vec4(0.41, 0.43, 0.88, 1.0));
                if(Gum::Window::CurrentlyBoundWindow->getMouse()->getMouseWheelState() != 0)
                {
                    //iMaxValue = pContent->getBoundingBox().size.y - getSize().y;
                    //float oneperc = 1.0f / (float)(pContent->getBoundingBox().size.y - getSize().y);
                    iIndicatorPos += -Gum::Window::CurrentlyBoundWindow->getMouse()->getMouseWheelState() * iStepSize;// * FPS::get();// ((float)iStepSize * oneperc);
                    moveContent();
                }
            }
            else 
            { 
                pScrollIndicator->setColor(vec4(0.19, 0.2, 0.42, 1.0));
            }

            if(pScrollBar->hasClickedInside())
            {
                bSnapped = true;
                iSnapOffset = pScrollIndicator->getPosition().y - Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().y;
	            pScrollIndicator->setColor(vec4(0.41, 0.43, 0.88, 1.0));
                Gum::IO::Mouse::setBusiness(true);
            }
        }

        if(bSnapped)
        {
            //float dist = Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().y - pScrollBar->getPosition().y;
            //dist /= (pScrollBar->getSize().y - pScrollIndicator->getSize().y);
            //iMaxValue = pContent->getBoundingBox().size.y - getSize().y - pScrollBar->getPosition().y;
            //iIndicatorPos = dist * iMaxValue;
            iIndicatorPos = Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition().y - pScrollBar->getPosition().y + iSnapOffset;
            moveContent();

            if(!Gum::Window::CurrentlyBoundWindow->getMouse()->hasLeftClick())
            {
	            pScrollIndicator->setColor(vec4(0.19, 0.2, 0.42, 1.0));
                bSnapped = false;
                Gum::IO::Mouse::setBusiness(false);
            }
        }

        updatechildren();
    }
}


void Scroller::updateOnAddGUI(RenderGUI* gui)
{
    pContent->addGUI(gui);
    vChildren.clear();
    moveContent();
}

void Scroller::updateContent()
{
    moveContent();
}

void Scroller::render()
{
    if(!bIsHidden)
    {
        glEnable(GL_SCISSOR_TEST);
        //glScissor(vActualPos.x, Gum::Window::CurrentlyBoundWindow->getSize().y - vActualPos.y - vActualSize.y, vActualSize.x, vActualSize.y);
        gumScissor(bbox2i(ivec2(vActualPos.x, Gum::Window::CurrentlyBoundWindow->getSize().y - vActualPos.y - vActualSize.y), vActualSize));
        for(unsigned int i = 0; i < pContent->numChildren(); i++)
        {
            pContent->getChild(i)->render();
        }
        Gum::Window::CurrentlyBoundWindow->resetViewport();
        glDisable(GL_SCISSOR_TEST);

        if(bHasOverflow)
            pScrollIndicator->render();
    }
}

void Scroller::setStepSize(int step)
{
    this->iStepSize = step;
}

void Scroller::setMainChildContainer(RenderGUI* gui)
{
    this->pMainChildContainer = gui;
}


int Scroller::getOffset()
{
    return this->pContent->getRelativePosition().y;
}

Scroller* Scroller::createFromXMLNode(XMLNode* node)
{
    return new Scroller(ivec2(0,0), ivec2(1,1));
}