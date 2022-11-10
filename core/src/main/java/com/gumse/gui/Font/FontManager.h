#pragma once
#include "Font.h"
#include <vector>
#include <map>

namespace Gum {
namespace GUI 
{
	class FontManager
	{
	private:
		FontManager();

		Font *pDefaultFont;
		std::map<std::string, Font*> mFonts;

		void readDir(std::vector<std::string> &FontNames, std::string DirName);

	public:
		FontManager(FontManager const&)     = delete;
		void operator=(FontManager const&)  = delete;
		static FontManager* getInstance() 
		{
			static FontManager* instance = new FontManager();
			return instance;
		}
		~FontManager();


		/*
		* Deletes a spezifified Font
		*/
		void deleteFont(std::string FontName);

		/*
		* Adds a Font that can be Called later in the Program with spezific Identifier
		*
		* Warning: Identifier shouldn't be used twice
		*/
		void addFont(Font *font, std::string Identifier);

		/**
		* This method loads a font into memory 
		*/
		Font* loadFont(std::string name, std::string Dir);

		/**
		* This method loads a font from memory 
		*/
		Font* loadFontFromMemory(std::string name, unsigned char* memFont, size_t length);


		//Getter
		int getNumberOfFonts();
		Font *getFont(std::string fontname);
		Font *getDefaultFont();
	};

	extern std::string FONT_PATH;
	extern FontManager* Fonts;

	extern void initFonts();
	extern void cleanupFonts();
}}