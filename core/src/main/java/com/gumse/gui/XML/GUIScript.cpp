/*#include "GUIScript.h"
#include <iostream>
#include <luaaa.hpp>
#include <System/Output.h>

GUIScript::GUIScript(std::string scriptstr, RenderGUI* rootgui)
{
    //initLua();

    lua::LuaClass<ivec2> luaVec2Class(pLuaState, "vec2");
    luaVec2Class.ctor();
    //luaGUIClass.fun("x", &RenderGUI::setSize);
    //luaGUIClass.def("type", &RenderGUI::getType);

    // To export it:
    lua::LuaClass<RenderGUI> luaGUIClass(pLuaState, "GUI");
    luaGUIClass.ctor();
    luaGUIClass.fun("setSize", &RenderGUI::setSize);
    luaGUIClass.fun("getSize", (ivec2*(RenderGUI::*)()) &RenderGUI::getSize);
    luaGUIClass.fun("setOrigin", &RenderGUI::setOrigin);
    //luaGUIClass.def("type", &RenderGUI::getType);

    lua::LuaModule elementsModule(pLuaState, "elements");
    elementsModule.fun("byID", [rootgui](std::string id) -> RenderGUI* {
        return rootgui->findChildByID(id);
    });
    
    compile(scriptstr);
}

GUIScript::~GUIScript()
{
}

void GUIScript::initLua()
{
    const luaL_Reg lualibs[] = {
        { LUA_COLIBNAME, luaopen_base },
        { LUA_LOADLIBNAME, luaopen_package },
        { LUA_TABLIBNAME, luaopen_table },
        { LUA_IOLIBNAME, luaopen_io },
        { LUA_OSLIBNAME, luaopen_os },
        { LUA_STRLIBNAME, luaopen_string },
        { LUA_MATHLIBNAME, luaopen_math },
        { LUA_DBLIBNAME, luaopen_debug },
        { NULL, NULL }
    };

    pLuaState = luaL_newstate();

    if (pLuaState != NULL)
    {
        const luaL_Reg *lib = lualibs;
        for (; lib->func; lib++) {
            lua_pushcfunction(pLuaState, lib->func);
            lua_pushstring(pLuaState, lib->name);
            lua_call(pLuaState, 1, 0);
        }
    }
    else
    {
        Gum::Output::error("Something went wrong initializing Lua");
    }
}

void GUIScript::compile(std::string code)
{
    int err = luaL_loadbuffer(pLuaState, code.c_str(), code.length(), "console");
    if (err == 0)
    {
        err = lua_pcall(pLuaState, 0, 0, 0);
    }

    if (err)
    {
        Gum::Output::error(std::string("LUA Error: ") + lua_tostring(pLuaState, -1));
        lua_pop(pLuaState, 1);
    }

    lua_close(pLuaState);
}*/