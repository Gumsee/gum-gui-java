#pragma once
#include "../Primitives/RenderGUI.h"
#include "Essentials/Unicode.h"
#include "TextBox.h"
#include <Essentials/SmoothFloat.h>
#include <functional>

class Dropdown : public RenderGUI
{
public:
	struct MenuEntry
	{
		TextBox *box;
		std::function<void(Gum::Unicode)> function;
	};

private:
	TextBox *pPreviewTextbox;
	SmoothFloat *pSmoothFloat;
	Gum::GUI::Font* pFont;

	std::vector<MenuEntry*> vMenuEntries;
	vec3 vColor;

	bool bDone = false;
	bool bIsOpen = false;
	bool bIsClicked = false;
	float fScrollOffset = 0.0f;
	int iTextSize = 0;
	int iCurrentIndex;
	Gum::Unicode sCurrentString;

protected:
    void updateOnTitleChange();

public:
	Dropdown(const Gum::Unicode& text, Gum::GUI::Font* font, const ivec2& pos, const ivec2& size, const float& textsize);
	~Dropdown();

	void render();
	void update();


	void addEntry(const Gum::Unicode& title, std::function<void(Gum::Unicode)> OnCLickFunction, const bool& active = false);
	void close();
	void open();
	void Switch();
	void clearMenu();
	
	//Getter
	Gum::Unicode getCurrentTitle() const;
	bool isCurrentClicked() const;
	bool isOpen() const;
	vec3 getColor() const;
	int getCurrentEntry() const;
	int numEntries() const;

    static Dropdown* createFromXMLNode(XMLNode* node);
};