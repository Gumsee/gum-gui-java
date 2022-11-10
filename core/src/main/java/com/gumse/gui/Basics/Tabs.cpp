#include "Tabs.h"
#include "../Font/FontManager.h"
#include "TextBox.h"

#include <System/MemoryManagement.h>
#include <Essentials/Tools.h>
#include <OpenGL/WrapperFunctions.h>

Tabs::Tabs(vec2 pos, vec2 size, ivec2 tabsize)
{
    this->sType = "Tabs";
    this->vPos = pos;
    this->vSize = size;
    this->vTabSize = tabsize;
    this->v4ActiveColor = vec4(0.5,0.5,0.5, 1.0);
    this->v4InactiveColor = vec4(0.4,0.4,0.4, 1.0);
    this->pActiveTab = nullptr;

    pBackground = new Box(ivec2(0,0), ivec2(100, tabsize.y));
    pBackground->setColor(vec4(0.25,0.25,0.25,1));
    pBackground->setSizeInPercent(true, false);
    pBackground->setOrigin(ivec2(0, tabsize.y));
    addElement(pBackground);

    vMargin.y = -tabsize.y;
    vOrigin.y = -tabsize.y;

    sfOffset.setMin(0.0f);


    resize();
    reposition();
}

Tabs::~Tabs()
{
    pBackground->destroyChildren();
    Gum::_delete(pBackground);
}

void Tabs::update()
{
    for(size_t i = 0; i < vTabs.size(); i++)
    {
        if(vTabs[i]->isClicked())
        {
            if(pActiveTab != nullptr)
            {
                pActiveTab->setColor(v4InactiveColor);
                pActiveTab->hideChildren(true);
            }
            vTabs[i]->setColor(v4ActiveColor);
            vTabs[i]->hideChildren(false);
            pActiveTab = vTabs[i];
        }
    }

    TextBox* lastTab = vTabs[vTabs.size() - 1];
    if(lastTab->getPosition().x + lastTab->getSize().x > vActualSize.x &&
        Tools::checkPointInBox(Gum::Window::CurrentlyBoundWindow->getMouse()->getPosition(), bbox2i(vActualPos - ivec2(0, vTabSize.y), ivec2(vActualSize.x, vTabSize.y))))
    {
        sfOffset.setMax(lastTab->getRelativePosition().x + lastTab->getSize().x - vActualSize.x + lastTab->getOrigin().x);
        sfOffset.increaseTarget(-50 * Gum::Window::CurrentlyBoundWindow->getMouse()->getMouseWheelState());
    }

    if(sfOffset.update())
    {
        for(unsigned int i = 0; i < pBackground->numChildren(); i++)
        {
            RenderGUI* elem = pBackground->getChild(i);
            elem->setOrigin(ivec2(sfOffset.get(), 0));
        }
    }
    updatechildren();
}

bool Tabs::updateToolTip()
{
    if(isMouseInside())
    {
        for(unsigned int i = 0; i < pActiveTab->numChildren(); i++) 
        {
            bool showToolTip = true;
            if(pActiveTab->getChild(i)->updateToolTip())
            {
                showToolTip = false;
            }

            //if(showToolTip)
            //    GumEngine::GUIS->showToolTip(sToolTip);
        }
        return true;
    }
    return false;
}


void Tabs::render()
{
    glEnable(GL_SCISSOR_TEST);
    gumScissor(bbox2i(ivec2(vActualPos.x, Gum::Window::CurrentlyBoundWindow->getSize().y - vActualPos.y - vActualSize.y + vTabSize.y), vActualSize));
    pBackground->render();
    glDisable(GL_SCISSOR_TEST);
}

void Tabs::addGUIToTab(RenderGUI *gui, std::string tabName)
{
    TextBox* tab = getTab(tabName);
    if(tab != nullptr)
    {
        tab->addGUI(gui);
        gui->setParent(this); //Overwrite parent
        gui->reposition();
        gui->resize();
    }
}

void Tabs::addTab(std::string name, bool active)
{
    float width = 0.0f;
    for(TextBox* tab : vTabs)
        width += tab->getSize().x;

    TextBox* tab = new TextBox(name, Gum::GUI::Fonts->getDefaultFont(), ivec2(width, 0), vTabSize);
    tab->setTextSize(vTabSize.y - 10);
    if(active)
    {
        tab->setColor(v4ActiveColor);
        tab->hideChildren(false);
        pActiveTab = tab;
    }
    else
    {
        tab->setColor(v4InactiveColor);
        tab->hideChildren(true);
    }
    tab->setSize(ivec2(tab->getTextSize().x + 30, vTabSize.y));
    tab->setTextOffset(ivec2(5, 3));
    pBackground->addGUI(tab);
    vTabs.push_back(tab);
}

void Tabs::setActiveTab(std::string tabname)
{
    TextBox* pFoundTab = getTab(tabname);
    if(pFoundTab != nullptr)
        this->pActiveTab = pFoundTab;
}

bool Tabs::isActiveTab(std::string tabname)
{
    TextBox* pFoundTab = getTab(tabname);
    return pFoundTab != nullptr && pActiveTab == pFoundTab;
}

TextBox* Tabs::getTab(std::string name)
{
    for(size_t i = 0; i < vTabs.size(); i++)
        if(vTabs[i]->getTitle().toString() == name)
            return vTabs[i];
    return nullptr;
}

TextBox* Tabs::getActiveTab()            { return pActiveTab; }
unsigned int Tabs::numTabs()             { return vTabs.size(); }

Tabs* Tabs::createFromXMLNode(XMLNode* node)
{
    ivec2 tabSize = node->mAttributes["tabsize"] != "" ? (ivec2)Tools::StringToVec2(node->mAttributes["tabsize"]) : ivec2(100, 30);
    return new Tabs(ivec2(0,0), ivec2(1,1), tabSize);
}