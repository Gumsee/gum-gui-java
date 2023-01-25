package com.gumse.gui.XML;

import java.util.Map;

import com.gumse.gui.Basics.*;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
import com.gumse.maths.Color;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.filesystem.XML.XMLReader;
import com.gumse.system.filesystem.XML.XMLReader.NodeCallback;
import com.gumse.tools.Output;
import com.gumse.tools.Toolbox;

public class XMLLoader
{
    private RenderGUI pRootGUI;
    
    public XMLLoader(String filename)
    {
        pRootGUI = new RenderGUI();
        pRootGUI.setType("XMLRoot");
        
        new XMLReader(filename, XMLNode.NODE_TYPES.ELEMENT, new NodeCallback() {
            @Override public void run(XMLNode node, int depth) 
            {
                String type = node.name;
                //String content = node.content;
                RenderGUI gui = null;
                RenderGUI parentGUI = null;
                if(node.parent != null)
                    parentGUI = (RenderGUI)node.parent.userptr;

                if(type.equals("item"))
                {
                    if(parentGUI.getType() == "Dropdown")
                    {
                        boolean active = node.getAttribute("active") == "true";
                        ((Dropdown)parentGUI).addEntry(node.content, null, active);
                    }
                    else
                    {
                        Output.error("item tag added outside of dropdown tag");
                    }
                }
                else if(type.equals("gui-root")) { gui = pRootGUI; }
                else if(type.equals("gui"))      { gui = new RenderGUI(); }
                else
                {
                    for(Map.Entry<String, XMLGUICreator> entry : XMLGUI.getTypes().entrySet())
                    {
                        if(type.equals(entry.getKey()))
                        {
                            gui = entry.getValue().create(node);
                            break;
                        }
                    }
                }

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
            
            if (name.equals("title"))
            {
                gui.setTitle(values[0]);
            }
            
            else if(name.equals("pos"))
            {
                gui.setPositionInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setPosition(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name.equals("size"))
            {
                gui.setSizeInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setSize(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name.equals("origin"))
            {
                gui.setOriginInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setOrigin(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name.equals("margin"))
            {
                gui.setMargin(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name.equals("max-size"))
            {
                gui.setMaxSizeInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setMaxSize(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name.equals("min-size"))
            {
                gui.setMinSizeInPercent(values[0].contains("%"), values[1].contains("%"));
                gui.setMinSize(new ivec2(Toolbox.StringToVec2(valueStr)));
            }
            else if(name.equals("color"))         { gui.setColor(vec4.div(Color.HEXToRGBA(valueStr), 255.0f)); }
            else if(name.equals("id"))            { gui.setID(new String(values[0])); }
            else if(name.equals("locale-id"))     { gui.setLocaleID(new String(values[0])); }
            else if(name.equals("border-radius")) { gui.setCornerRadius(Toolbox.StringToVec4(valueStr)); }
            else if(name.equals("tooltip"))       { gui.setToolTip(values[0]); }
            else                                  { gui.addAttribute(name, valueStr); }
        }
    }


    //
    // Getter
    //
    public RenderGUI getRootGUI() { return this.pRootGUI; }
};