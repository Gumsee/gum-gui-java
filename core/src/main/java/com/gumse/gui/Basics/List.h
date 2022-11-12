#pragma once
#include "../Primitives/Box.h"
#include "../Primitives/Text.h"
#include "../Basics/TextBox.h"


class List : public RenderGUI
{
private:
    struct ListEntry : public RenderGUI
    {
        Text* titleText = nullptr;
        RenderGUI* entryitem = nullptr;

        ListEntry(std::string title, vec4 color);
        ~ListEntry();
    };

    std::vector<ListEntry*> vEntries;
    TextBox* pTitleBox = nullptr;
    Box* pBackground = nullptr;

    vec4 v4FirstColor, v4SecondColor;

public:
    List(ivec2 pos, ivec2 size, std::string title);
    ~List();

    enum ENTRY_TYPE {
        STRING,
        INTEGER,
        DROPDOWN,
        DATE,
        TIME,
        BOOLEAN,
    };

    void render();

    void addEntry(std::string title, ENTRY_TYPE type);
};