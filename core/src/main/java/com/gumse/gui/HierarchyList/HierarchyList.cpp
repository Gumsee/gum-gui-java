#include "HierarchyList.h"
#include "HierarchyListEntry.h"

#include <System/MemoryManagement.h>
#include <OS/Window.h>

#include "../../Font/FontManager.h"
#include "../../GUIShader.h"

HierarchyList::HierarchyList(ivec2 pos, ivec2 size, std::string title, std::string rootname)
{
    this->vPos = pos;
    this->vSize = size;
    this->sType = "HierarchyList";

    this->pSelectedEntry = nullptr;

    pTitleBox = new TextBox(title, Gum::GUI::Fonts->getDefaultFont(), ivec2(0,0), ivec2(100, 30));
    pTitleBox->setAlignment(TextBox::Alignment::LEFT);
    pTitleBox->setTextSize(20);
    pTitleBox->setTextOffset(ivec2(-10, 5));
    pTitleBox->setSizeInPercent(true, false);
    pTitleBox->setColor(vec4(0.1, 0.1, 0.1, 1));
    pTitleBox->setTextColor(vec4(0.76, 0.76, 0.76, 1));

    pBackground = new Box(ivec2(0, 0), ivec2(100, 100));
    pBackground->setSizeInPercent(true, true);
    pBackground->setColor(vec4(0,1,0,1));

    pScroller = new Scroller(ivec2(0, pTitleBox->getSize().y), ivec2(100, 100));
    pScroller->setSizeInPercent(true, true);
    pScroller->setMargin(ivec2(0, -pTitleBox->getSize().y));
    //pScroller->setStepSize(30);

    pSelectedEntryIndicator = new Box(ivec2(0,0), ivec2(100, 30));
    pSelectedEntryIndicator->setSizeInPercent(true, false);
    pSelectedEntryIndicator->setColor(vec4(0.34, 0.5, 0.76, 1));
    pSelectedEntryIndicator->hide(true);
    pScroller->addGUI(pSelectedEntryIndicator);


    pRootEntry = new HierarchyListEntry(rootname, this, nullptr);
    pRootEntry->setPosition(ivec2(10, 0));
    pRootEntry->shouldKeepTrackOfBoundingbox(true);
    pScroller->addGUI(pRootEntry);
    pScroller->setMainChildContainer(pRootEntry);

    addElement(pBackground);
    addElement(pScroller);
    addElement(pTitleBox);

    resize();
    reposition();
}

HierarchyList::~HierarchyList()
{
    pScroller->destroyChildren();
    for(RenderGUI* gui : vElements)
        Gum::_delete(gui);
}

void HierarchyList::update()
{
    updatechildren();

    if(pSelectedEntry != nullptr)
    {
        pSelectedEntryIndicator->setPosition(ivec2(0, pSelectedEntry->getPosition().y - pScroller->getPosition().y));
    }
}

void HierarchyList::render()
{
    Gum::GUI::getStripesShaderProgram()->use();
    Gum::GUI::getStripesShaderProgram()->LoadUniform("transmat", pBackground->getTransformation());
    Gum::GUI::getStripesShaderProgram()->LoadUniform("orthomat", Gum::Window::CurrentlyBoundWindow->getScreenMatrix());
    Gum::GUI::getStripesShaderProgram()->LoadUniform("patternoffset", (float)vActualPos.y + (float)pScroller->getOffset());
    Gum::GUI::getStripesShaderProgram()->LoadUniform("lineheight", 30.0f);
    Gum::GUI::getStripesShaderProgram()->LoadUniform("color1", vec4(0.16f, 0.16f, 0.16f, 1.0f));
    Gum::GUI::getStripesShaderProgram()->LoadUniform("color2", vec4(0.18f, 0.18f, 0.18f, 1.0f));
    pBackground->renderCustom();
    Gum::GUI::getStripesShaderProgram()->unuse();
        

    pScroller->render();
    pTitleBox->render();
}

void HierarchyList::addEntry(HierarchyListEntry* entry)
{
    pRootEntry->addEntry(entry);
    pScroller->updateContent();
}

void HierarchyList::selectEntry(HierarchyListEntry* entry)
{
    this->pSelectedEntry = entry;
    pSelectedEntryIndicator->hide(entry == nullptr);
}

HierarchyListEntry* HierarchyList::getRootEntry()
{
    return this->pRootEntry;
}

HierarchyListEntry* HierarchyList::getSelectedEntry()
{
    return this->pSelectedEntry;
}