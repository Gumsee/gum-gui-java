package com.gumse.gui.XML;

import com.gumse.gui.Primitives.RenderGUI;

public class XMLGUI
{
	public static RenderGUI loadFile(String xmlfile)
    {
        XMLLoader loader = new XMLLoader(xmlfile);
        return loader.getRootGUI();
    }
};