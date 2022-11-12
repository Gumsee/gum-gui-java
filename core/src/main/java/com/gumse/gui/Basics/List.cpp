#include "List.h"
#include "../Font/FontManager.h"
#include "../GUIShader.h"
#include <System/MemoryManagement.h>


List::ListEntry::ListEntry(std::string title, vec4 color)
{
    titleText = new Text(title, Gum::GUI::Fonts->getDefaultFont(), ivec2(10, 0), 0);
    titleText->setCharacterHeight(20);
    titleText->setColor(vec4(0.76, 0.76, 0.76, 1));
    addElement(titleText);
}

List::ListEntry::~ListEntry()
{
    Gum::_delete(titleText);
    Gum::_delete(entryitem);
}

List::List(ivec2 pos, ivec2 size, std::string title)
{
    this->vSize = size;
    this->vPos = pos;
    this->sTitle = title;
    this->v4FirstColor = vec4(0.3, 0.3, 0.3, 1);
    this->v4SecondColor = vec4(0.24, 0.24, 0.24, 1);


    pBackground = new Box(ivec2(0,0), ivec2(100, 100));
    pBackground->setSizeInPercent(true, true);
    addElement(pBackground);

    pTitleBox = new TextBox(title, Gum::GUI::Fonts->getDefaultFont(), ivec2(0,0), ivec2(100, 30));
    pTitleBox->setAlignment(TextBox::Alignment::LEFT);
    pTitleBox->setTextSize(20);
    pTitleBox->setTextOffset(ivec2(-10, 5));
    pTitleBox->setSizeInPercent(true, false);
    pTitleBox->setColor(vec4(0.1, 0.1, 0.1, 1));
    pTitleBox->setTextColor(vec4(0.76, 0.76, 0.76, 1));
    addElement(pTitleBox);
    
    

    resize();
    reposition();
}

List::~List()
{
    Gum::_delete(pTitleBox);
    Gum::_delete(pBackground);

    for(ListEntry *entry : vEntries)
        Gum::_delete(entry);
}

void List::render()
{
    Gum::GUI::getStripesShaderProgram()->use();
    Gum::GUI::getStripesShaderProgram()->LoadUniform("transmat", pBackground->getTransformation());
    Gum::GUI::getStripesShaderProgram()->LoadUniform("orthomat", Gum::Window::CurrentlyBoundWindow->getScreenMatrix());
    //Gum::GUI::getStripesShaderProgram()->LoadUniform("patternoffset", (float)vActualPos.y + (float)pScroller->getOffset());
    Gum::GUI::getStripesShaderProgram()->LoadUniform("lineheight", 30.0f);
    Gum::GUI::getStripesShaderProgram()->LoadUniform("color1", vec4(0.16f, 0.16f, 0.16f, 1.0f));
    Gum::GUI::getStripesShaderProgram()->LoadUniform("color2", vec4(0.18f, 0.18f, 0.18f, 1.0f));
    pBackground->renderCustom();
    Gum::GUI::getStripesShaderProgram()->unuse();

    pTitleBox->render();

    for(ListEntry *entry : vEntries)
        entry->render();
}


void List::addEntry(std::string title, ENTRY_TYPE type)
{
    ListEntry* entry = new ListEntry(title, (vEntries.size() % 2 == 0) ? v4FirstColor : v4SecondColor);
    entry->setSize(ivec2(100, 40));
    entry->setPosition(ivec2(0, vEntries.size() * 30 + 30));
    vEntries.push_back(entry);
    addElement(entry);
    entry->setSizeInPercent(true, false);

}