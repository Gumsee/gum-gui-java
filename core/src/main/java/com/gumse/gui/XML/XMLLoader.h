#pragma once
#include "../Primitives/RenderGUI.h"
//#include "GUIScript.h"
//#include "GUIStyle.h"

class XMLLoader
{
private:
	RenderGUI* pRootGUI;

    void processGeneralArguments(std::map<std::string, std::string> args, RenderGUI* gui);

public:
	XMLLoader(std::string filename);
	~XMLLoader();

    RenderGUI* getRootGUI();
};