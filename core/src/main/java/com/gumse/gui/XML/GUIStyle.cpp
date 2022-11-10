#include "GUIStyle.h"

#include <iostream>

GUIStyle::GUIStyle(std::string scriptstr, RenderGUI* rootgui)
{
    
    compile(scriptstr);
}

GUIStyle::~GUIStyle()
{}

void GUIStyle::compile(std::string code)
{
    for(unsigned int i = 0; i < code.length(); i++)
    {
        char c = code[i];
        std::string command;
        switch(c)
        {
            case '#':
                command = code.substr(i + 1, code.find(';', i) - 4);
                std::cout << "command: " << command << std::endl;
            break;
            case '.':
            break;
            default:
            break;
        }
    }
}