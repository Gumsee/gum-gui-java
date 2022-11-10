#pragma once
#include <System/Output.h>
#include "XMLLoader.h"

class XMLGUI
{
public:
	XMLGUI() = delete;
	~XMLGUI() = delete;

	static RenderGUI* loadFile(std::string xmlfile)
    {
        XMLLoader loader(xmlfile);
        return loader.getRootGUI();
    }
};