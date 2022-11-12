#include "HierarchyListEntry.h"
#include "HierarchyList.h"
#include "../../Font/FontManager.h"
#include "../../GUIShader.h"


#include <string>

#include <System/Output.h>
#include <OS/IO/Mouse.h>
#include <System/MemoryManagement.h>
#include <OS/Window.h>

#include <Essentials/Unicode.h>
#include <Essentials/Tools.h>

VertexArrayObject* HierarchyListEntry::pArrowVAO = nullptr;

void HierarchyListEntry::initVAO()
{
    if(pArrowVAO == nullptr)
    {
        pArrowVAO = new VertexArrayObject();
        VertexBufferObject<vec3> pArrowVBO;
        pArrowVBO.setData({ 
            vec3( 0.5f,  0.0f, 0.0f),
            vec3(-0.5f,  0.0f, 0.0f),
            vec3( 0.0f, -0.5f, 0.0f),
        });
        pArrowVAO->addAttribute(&pArrowVBO, 0, 3, GL_FLOAT, sizeof(vec3), offsetof(vec3, x));
    }
}

//
// List Entry
//
HierarchyListEntry::HierarchyListEntry(std::string name, HierarchyList* parentlist, std::function<void()> callback)
{
    this->bIsOpen = false;
    this->bHasChildEntries = false;
    this->pCallback = callback;
    this->sType = "HierarchyListEntry";
    this->pParentList = parentlist;

    this->title = new TextField(name, Gum::GUI::Fonts->getDefaultFont(), ivec2(20,0), ivec2(100, 100));
    this->title->setSizeInPercent(true, true);
    this->title->shouldActivateOnDoubleclick(true);
    this->title->getBox()->getBox()->hide(true);
    this->title->getBox()->getText()->setCharacterHeight(20);
    this->title->getBox()->setTextOffset(ivec2(0, 3));
    this->title->setCursorShapeOnHover(GUM_CURSOR_DEFAULT);

    addElement(title);
    initVAO();
    updateOnPosChange();

    setSize(ivec2(100, 30));
    setSizeInPercent(true, false);

    resize();
    reposition();
}

HierarchyListEntry::~HierarchyListEntry()
{
    Gum::_delete(title);
    destroyChildren();
}

void HierarchyListEntry::update()
{
    if(bIsOpen)
        for(unsigned int i = 0; i < numChildren(); i++)
            vChildren[i]->update();
    
    Gum::IO::Mouse* mouse = Gum::Window::CurrentlyBoundWindow->getMouse();
    if(!title->isEditing() && isMouseInside() && !RenderGUI::somethingHasBeenClicked())
    {
        if(bHasChildEntries)
        {
            if(Tools::checkPointInBox(mouse->getPosition(), bbox2i(vActualPos, ivec2(20, 30))))
            {
                mouse->setCursor(GUM_CURSOR_HAND);
                if(mouse->hasLeftRelease() && Tools::checkPointInBox(mouse->getLeftClickPosition(), bbox2i(vActualPos, ivec2(20, 30))))
                {
                    openState(!bIsOpen);
                }
            }
        }

        //Call callback
        if(Tools::checkPointInBox(mouse->getLeftClickPosition(), bbox2i(vActualPos + ivec2(20, 0), vActualSize - ivec2(20, 0))))
        {
            if(mouse->hasLeftRelease())
            {
                pParentList->selectEntry(this);
                if(pCallback != nullptr)
                    pCallback();
            }
            if(mouse->hasLeftDoubleClick())
            {
                pParentList->selectEntry(nullptr);
            }
        }
    }
    title->update();
}

void HierarchyListEntry::updateOnPosChange()
{
    quat rot;
    if(!bIsOpen)
        rot = quat::toQuaternion(vec3(0, 0, 90));

    m4ArrowMatrix = Gum::Maths::createTransformationMatrix(vec3(vActualPos.x + 5, Gum::Window::CurrentlyBoundWindow->getSize().y - vActualPos.y - 15, 0), rot, vec3(15));
    
}

void HierarchyListEntry::render()
{
    if(bHasChildEntries)
    {
        Gum::GUI::getShaderProgram()->use();
        Gum::GUI::getShaderProgram()->LoadUniform("orthomat", Gum::Window::CurrentlyBoundWindow->getScreenMatrix());
        Gum::GUI::getShaderProgram()->LoadUniform("transmat", m4ArrowMatrix);
        Gum::GUI::getShaderProgram()->LoadUniform("Uppercolor", vec4(0.76, 0.76, 0.76,1));
        Gum::GUI::getShaderProgram()->LoadUniform("borderThickness", 0.0f);
        Gum::GUI::getShaderProgram()->LoadUniform("hasTexture", false);
        pArrowVAO->bind();
        glDrawArrays(GL_TRIANGLES, 0, 3);
        pArrowVAO->unbind();
        Gum::GUI::getShaderProgram()->unuse();
    }

    title->render();
    
    if(bIsOpen)
        for(unsigned int i = 0; i < numChildren(); i++)
            vChildren[i]->render();
}

void HierarchyListEntry::repositionEntries(bool checkforselectedentry)
{
    if(checkforselectedentry)
        pParentList->selectEntry(nullptr);

    for(unsigned int i = 0; i < numChildren(); i++)
    {
        HierarchyListEntry* entry = (HierarchyListEntry*)vChildren[i];
        entry->repositionEntries(!bIsOpen);
        int ypos = (i+1) * entry->getSize().y;
        if(i > 0)
        {
            ypos += ((HierarchyListEntry*)vChildren[i - 1])->getHeight();
        }

        entry->setPosition(ivec2(INDENT_SIZE, ypos));
    }
}

void HierarchyListEntry::addEntry(HierarchyListEntry* entry)
{
    entry->setIndent(indent + INDENT_SIZE);
    addGUI(entry);
    repositionEntries();
    bHasChildEntries = true;
}

void HierarchyListEntry::updateBoundingBox(bool override)
{ 
    if(bKeepTrackOfBoundingBox || override)
    {
        bBoundingBox.pos = vActualPos;
        bBoundingBox.size = getSize();

        if(bIsOpen)
        {
            //bBoundingBox.size.y += getSize().y * (numChildren());
            for(unsigned int i = 0; i < numChildren(); i++)
            {
                HierarchyListEntry* child = ((HierarchyListEntry*)vChildren[i]);
                child->updateBoundingBox(true);
                bBoundingBox.size.y += child->getBoundingBox().size.y;
            }
        }
    }
}

int HierarchyListEntry::getHeight()
{
    if(bIsOpen)
        return getSize().y * (numChildren());
    return 0;
}

void HierarchyListEntry::openState(bool opn)
{
    bIsOpen = !bIsOpen;
    if(pParent != nullptr && pParent->getType() == "HierarchyListEntry")
        ((HierarchyListEntry*)pParent)->repositionEntries();
}

void HierarchyListEntry::openAll()
{
    bIsOpen = true;
    for(unsigned int i = 0; i < numChildren(); i++)
    {
        HierarchyListEntry* entry = (HierarchyListEntry*)vChildren[i];
        entry->openAll();
    }
    if(pParent != nullptr && pParent->getType() == "HierarchyListEntry")
        ((HierarchyListEntry*)pParent)->repositionEntries();
}

void HierarchyListEntry::setIndent(int indent)
{
    this->indent = indent;
    //this->title->setPosition(ivec2(indent, title->getPosition().y));
}
void HierarchyListEntry::setRenameCallback(std::function<void(Gum::Unicode)> callback)
{
    this->title->setReturnCallback(callback);
}

void HierarchyListEntry::cleanup()
{
    Gum::_delete(pArrowVAO);
}