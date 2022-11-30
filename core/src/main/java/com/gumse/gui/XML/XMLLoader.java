package com.gumse.gui.XML;

import java.util.Map;

import com.gumse.gui.Basics.*;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.Color;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec3;
import com.gumse.maths.vec4;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.filesystem.XML.XMLReader;
import com.gumse.system.filesystem.XML.XMLReader.NodeCallback;
import com.gumse.tools.Toolbox;

public class XMLLoader
{
    private RenderGUI pRootGUI;
    
    public XMLLoader(String filename)
    {
        pRootGUI = new RenderGUI();
        
        new XMLReader(filename, XMLNode.NODE_TYPES.ELEMENT, new NodeCallback() {
            @Override public void run(XMLNode node, int depth) 
            {
                String type = node.name;
                //String content = node.content;
                RenderGUI gui = null;
                RenderGUI parentGUI = null;
                if(node.parent != null)
                    parentGUI = (RenderGUI)node.parent.userptr;

                if(type == "tab")
                {
                    /*if(parentGUI.getType() == "Tabs")
                    {
                        RenderGUI tabHandle = new RenderGUI();
                        tabHandle.setSize(new ivec2(100, 100));
                        tabHandle.setSizeInPercent(true, true);
                        tabHandle.setType("tab");
                        xmlFileRecursiveFunc(node.children, tabHandle);
                        String tabName = attrs["name"] != "" 
                                                ? attrs["name"] 
                                                : std.to_string(((Tabs*)parentGUI).numTabs());
                        bool active = attrs["active"] == "true";
                        ((Tabs*)parentGUI).addTab(tabName, active);
                        ((Tabs*)parentGUI).addGUIToTab(tabHandle, tabName);
                    }
                    else
                    {
                        Gum.Output.error("Tab tag added outside of tabs tag");
                    }*/
                }
                else if(type == "item")
                {
                    /*if(parentGUI.getType() == "Dropdown")
                    {
                        bool active = attrs["active"] == "true";
                        ((Dropdown*)parentGUI).addEntry(content, nullptr, active);
                    }
                    else
                    {
                        Gum.Output.error("item tag added outside of dropdown tag");
                    }*/
                }
                else if(type == "gui-root") { gui = pRootGUI; }
                else if(type == "gui")      { gui = new RenderGUI(); }
                else if(type == "box")      { gui = Box.createFromXMLNode(node); }
                else if(type == "button")   { gui = Button.createFromXMLNode(node); }
                else if(type == "tabs")     { gui = Tabs.createFromXMLNode(node); }
                else if(type == "group")    { gui = Group.createFromXMLNode(node); }
                else if(type == "dropdown") { gui = Dropdown.createFromXMLNode(node); }
                else if(type == "scroller") { gui = Scroller.createFromXMLNode(node); }
                else if(type == "slider")   { gui = Slider.createFromXMLNode(node); }
                else if(type == "switch")   { gui = Switch.createFromXMLNode(node); }
                else if(type == "text")     { gui = Text.createFromXMLNode(node); }
                else if(type == "textfield"){ gui = TextField.createFromXMLNode(node); }
                else if(type == "textbox")  { gui = TextBox.createFromXMLNode(node); }

                if(gui != null)
                {
                    processGeneralArguments(node.mAttributes, gui);
                    node.userptr = gui;

                    if(parentGUI != null)
                        parentGUI.addGUI(gui);
                }
                
            }
        });
    }


    private void processGeneralArguments(Map<String, String> args, RenderGUI gui)
    {
        for(Map.Entry<String, String> arg : args.entrySet())
        {
            String name = arg.getKey();
            String valueStr = arg.getValue();
            String[] values = valueStr.split(",");
            
            if (name == "title")
            {
                gui.setTitle(values[0]);
            }
            
            else if(name == "pos")
            {
                gui.setPositionInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setPosition(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name == "size")
            {
                gui.setSizeInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setSize(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name == "origin")
            {
                gui.setOriginInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setOrigin(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name == "margin")
            {
                gui.setMargin(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name == "max-size")
            {
                gui.setMaxSizeInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setMaxSize(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name == "min-size")
            {
                gui.setMinSizeInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setMinSize(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name == "color")         { gui.setColor(vec4.div(Color.HEXToRGBA(valueStr), 255.0f)); }
            else if(name == "id")            { gui.setID(values[0]); }
            else if(name == "border-radius") { gui.setCornerRadius(Toolbox.StringToVec4(valueStr)); }
            else if(name == "tooltip")       { gui.setToolTip(values[0]); }
            else                             { gui.addAttribute(name, valueStr); }
        }
    }


    //
    // Getter
    //
    public RenderGUI getRootGUI() { return this.pRootGUI; }
};