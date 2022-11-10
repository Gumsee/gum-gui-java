#pragma once
#include <string>
#include "../Primitives/RenderGUI.h"

class GUIStyle
{
private:
    void compile(std::string code);

public:
    GUIStyle(std::string stylestr, RenderGUI* rootgui);
    ~GUIStyle();
};