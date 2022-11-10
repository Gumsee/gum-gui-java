/*#pragma once
#include <string>
#include "../Primitives/RenderGUI.h"
#include <lua/lua.h>

class GUIScript
{
private:
    lua_State* pLuaState;

    void initLua();
    void compile(std::string code);

public:
    GUIScript(std::string scriptstr, RenderGUI* rootgui);
    ~GUIScript();
};*/