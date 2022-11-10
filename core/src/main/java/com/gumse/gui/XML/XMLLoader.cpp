#include "XMLLoader.h"
#include <System/Output.h>
#include <Essentials/Tools.h>
#include <Essentials/Filesystem/XMLReader.h>

#include "../Basics/Scroller.h"
#include "../Basics/Switch.h"
#include "../Basics/Button.h"
#include "../Basics/Slider.h"
#include "../Basics/Scroller.h"
#include "../Basics/TextBox.h"
#include "../Basics/Tabs.h"
#include "../Basics/Group.h"
#include "../Basics/Dropdown.h"
#include "../Primitives/RenderGUI.h"


XMLLoader::XMLLoader(std::string filename)
{
    pRootGUI = new RenderGUI();
    
    XMLReader(filename, XMLNode::NODE_TYPES::ELEMENT, [this](XMLNode* node) {
        std::string type = node->name;
        std::string content = node->content;
        RenderGUI* gui = nullptr;
        RenderGUI* parentGUI = nullptr;
        if(node->parent != nullptr)
            parentGUI = (RenderGUI*)node->parent->userptr;

        if(type == "script")
        {
            if(content != "")
                {//GUIScript* pScript = new GUIScript(content, pRootGUI);
                }
            else
                Gum::Output::error("XMLGUILoader: script is empty!");
        }
        else if(type == "style")
        {
            if(content != "")
                {//GUIStyle* pStyle = new GUIStyle(content, pRootGUI);
                }
            else
                Gum::Output::error("XMLGUILoader: style is empty!");
        }
        else if(type == "tab")
        {
            /*if(parentGUI->getType() == "Tabs")
            {
                RenderGUI* tabHandle = new RenderGUI();
                tabHandle->setSize(ivec2(100, 100));
                tabHandle->setSizeInPercent(true, true);
                tabHandle->setType("tab");
                xmlFileRecursiveFunc(node->children, tabHandle);
                std::string tabName = attrs["name"] != "" 
                                        ? attrs["name"] 
                                        : std::to_string(((Tabs*)parentGUI)->numTabs());
                bool active = attrs["active"] == "true";
                ((Tabs*)parentGUI)->addTab(tabName, active);
                ((Tabs*)parentGUI)->addGUIToTab(tabHandle, tabName);
            }
            else
            {
                Gum::Output::error("Tab tag added outside of tabs tag");
            }*/
        }
        else if(type == "item")
        {
            /*if(parentGUI->getType() == "Dropdown")
            {
                bool active = attrs["active"] == "true";
                ((Dropdown*)parentGUI)->addEntry(content, nullptr, active);
            }
            else
            {
                Gum::Output::error("item tag added outside of dropdown tag");
            }*/
        }
        else if(type == "gui-root") { gui = pRootGUI; }
        else if(type == "box")      { gui = Box::createFromXMLNode(node); }
        else if(type == "button")   { gui = Button::createFromXMLNode(node); }
        else if(type == "tabs")     { gui = Tabs::createFromXMLNode(node); }
        else if(type == "group")    { gui = Group::createFromXMLNode(node); }
        else if(type == "dropdown") { gui = Dropdown::createFromXMLNode(node); }
        else if(type == "scroller") { gui = Scroller::createFromXMLNode(node); }
        else if(type == "slider")   { gui = Slider::createFromXMLNode(node); }
        else if(type == "switch")   { gui = Switch::createFromXMLNode(node); }

        if(gui != nullptr)
        {
            processGeneralArguments(node->mAttributes, gui);
            node->userptr = gui;

            if(parentGUI != nullptr)
                parentGUI->addGUI(gui);
        }
    });
}

XMLLoader::~XMLLoader()
{

}


void XMLLoader::processGeneralArguments(std::map<std::string, std::string> args, RenderGUI* gui)
{
    for(auto arg : args)
    {
        std::string name = arg.first;
        std::vector<std::string> values = Tools::splitStr(arg.second, ',');
        
        if (name == "title")
        {
            gui->setTitle(values[0]);
        }
        else if(name == "pos")
        {
            gui->setPositionInPercent(Tools::strContains(values[0], "%"), Tools::strContains(values[1], "%"));
            gui->setPosition(Tools::StringToVec2(arg.second));
        }
        else if(name == "size")
        {
            gui->setSizeInPercent(Tools::strContains(values[0], "%"), Tools::strContains(values[1], "%"));
            gui->setSize(Tools::StringToVec2(arg.second));
        }
        else if(name == "origin")
        {
            gui->setOriginInPercent(Tools::strContains(values[0], "%"), Tools::strContains(values[1], "%"));
            gui->setOrigin(Tools::StringToVec2(arg.second));
        }
        else if(name == "margin")
        {
            gui->setMargin(Tools::StringToVec2(arg.second));
        }
        else if(name == "max-size")
        {
            gui->setMaxSizeInPercent(Tools::strContains(values[0], "%"), Tools::strContains(values[1], "%"));
            gui->setMaxSize(Tools::StringToVec2(arg.second));
        }
        else if(name == "min-size")
        {
            gui->setMinSizeInPercent(Tools::strContains(values[0], "%"), Tools::strContains(values[1], "%"));
            gui->setMinSize(Tools::StringToVec2(arg.second));
        }
        else if(name == "color")         { gui->setColor(vec4(Gum::Maths::HEXToRGB(arg.second.substr(1, arg.second.length())) / 255.0f, 1.0f)); }
        else if(name == "id")            { gui->setID(values[0]); }
        else if(name == "border-radius") { gui->setCornerRadius(Tools::StringToVec4(arg.second)); }
        else if(name == "tooltip")       { gui->setToolTip(values[0]); }
        else                             { gui->addAttribute(arg.first, arg.second); }
    }
}


//
// Getter
//
RenderGUI* XMLLoader::getRootGUI() { return this->pRootGUI; }