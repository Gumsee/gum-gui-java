package com.gumse.gui.XML;

import java.util.HashMap;
import java.util.Map;

import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.system.filesystem.XML.XMLNode;

public class XMLGUI
{
    public interface XMLGUICreator
    {
        RenderGUI create(XMLNode node);
    }

    private static Map<String, XMLGUICreator> mXMLGUIs; 

	public static RenderGUI loadFile(String xmlfile)
    {
        XMLLoader loader = new XMLLoader(xmlfile);
        return loader.getRootGUI();
    }

    public static Map<String, XMLGUICreator> getTypes()
    {
        return mXMLGUIs;
    }

    public static void addGUIType(String tagname, XMLGUICreator creator)
    {
        if(mXMLGUIs == null)
            mXMLGUIs = new HashMap<>();
        mXMLGUIs.put(tagname, creator);
    }
};