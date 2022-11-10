#include "FontManager.h"
#include <System/Output.h>
#include <Essentials/Tools.h>
#include <System/MemoryManagement.h>
#include <codecvt>
#include <iostream>
#include <dirent.h>
#include <locale>
#include <GL/glew.h>
#include <utility>
//#include "DefaultFont.h"
#include "OpenSans.h"
#include <OpenGL/WrapperFunctions.h>
#include <ft2build.h>
#include <freetype/ftsystem.h>
#include FT_FREETYPE_H

namespace Gum {
namespace GUI 
{
	void genTextures(Font* font, const FT_Face& face);

	std::string FONT_PATH = "";
	FT_Library *pFreetypeLibrary = nullptr;

	FontManager::FontManager()
	{
		//Init Library
		pFreetypeLibrary = new FT_Library();
		if (FT_Init_FreeType(pFreetypeLibrary))
			Gum::Output::error("GUIManager: Couldn't initialize the Freetype library!");


		if(FONT_PATH != "")
		{
			Gum::Output::info("FontManager: Adding Fonts from \"" + FONT_PATH + "\" ...");
			
			std::vector<std::string> FontNames;
			readDir(FontNames, FONT_PATH);

			for (int i = 0; i < FontNames.size(); i++)
				loadFont(FontNames[i], FONT_PATH);
			
			if(getNumberOfFonts() == 0) { Gum::Output::warn("No Fonts have been found!"); }
			else 						{ Gum::Output::info("Successfully initialized Fonts!"); }
		}

		//Load Default Font from memory
		//pDefaultFont = loadFontFromMemory("default", defaultFont, sizeof(defaultFont) * sizeof(unsigned char));
		pDefaultFont = loadFontFromMemory("default", openSans, opensans_len);
		FT_Done_FreeType(*pFreetypeLibrary);
		//Gum::_delete(pFreetypeLibrary);
	}

	FontManager::~FontManager()
	{
		Gum::Output::print("FontManager: Cleaning...");
		//for(auto font : mFonts)
		//	Gum::_delete(font.second);
		
		Gum::_delete(pDefaultFont);

		mFonts.clear();
	}


	void FontManager::readDir(std::vector<std::string> &FontNames, std::string DirName)
	{
		struct dirent *ent;
		std::string name;
		int num = 0, subdir = 0;
		DIR *dir;

		if ((dir = opendir(DirName.c_str())) != NULL) 
		{
			while ((ent = readdir(dir)) != NULL) 
			{
				unsigned char type = ent->d_type;
				name = ent->d_name;
				if(name != ".." && name != ".")
				{
					if(type == DT_DIR)
					{
						readDir(FontNames, DirName + name + "/");
						subdir++;
						continue;
					}

				//	Gum::Output::warn(DirName + name);
					FontNames.push_back(DirName + name);
					num++;
				}
			}
			closedir(dir);
		}

		Gum::Output::info("FontManager: " + std::to_string(num) + " Fonts in \"" + DirName + "\" with " + std::to_string(subdir) + " Subdirectories");
	}

	Font* FontManager::loadFont(std::string name, std::string file)
	{
		FT_Face face;
		auto error = FT_New_Face(*pFreetypeLibrary, file.c_str(), 0, &face);
		if ( error == FT_Err_Unknown_File_Format )  { Gum::Output::error("Unknown file format: " + name); }
		else if (error)								{ Gum::Output::error("An Error occured whilest opening font file: " + name); }

		FT_Set_Pixel_Sizes(face, 0, 48);

		Font *font = new Font(name, file);

		genTextures(font, face);

		FT_Done_Face(face);

		mFonts[name] = font;

		font->calcHighestGlyph();

		return font;
	}

	Font* FontManager::loadFontFromMemory(std::string name, unsigned char* memFont, size_t length)
	{
		FT_Face face;
		FT_Error error = FT_New_Memory_Face(*pFreetypeLibrary, memFont, length, 0, &face);
		if ( error == FT_Err_Unknown_File_Format )  { Gum::Output::error("Unknown file format: " + name); }
		else if (error)								{ Gum::Output::error("An Error occured whilest opening font file: " + name); }

		FT_Set_Pixel_Sizes(face, 0, 48);

		Font *font = new Font(name, "memory");
		genTextures(font, face);

		FT_Done_Face(face);

		mFonts[name] = font;

		font->calcHighestGlyph();
		
		return font;
	}


	void genTextures(Font* font, const FT_Face& face)
	{
		gumPixelStorei(GL_UNPACK_ALIGNMENT, 1); // Disable byte-alignment restriction
		
		std::wstring_convert<std::codecvt_utf8<char32_t>, char32_t> convert;

		FT_Select_Charmap(face, FT_ENCODING_UNICODE);

		FT_ULong codepoint;
		FT_UInt gid;

		codepoint = FT_Get_First_Char(face, &gid);
		while (gid != 0)
		{
			if (FT_Load_Char(face, codepoint, FT_LOAD_RENDER))
			{
				Gum::Output::error("ERROR::FREETYTPE: Failed to load Glyph");
				continue;
			}
			
			// Generate texture
			unsigned int texture;
			gumGenTextures(1, &texture);
			glBindTexture(GL_TEXTURE_2D, texture);
			// Set texture options
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			if(!gumTexImage2D(GL_TEXTURE_2D, 0, GL_RED, ivec2(face->glyph->bitmap.width, face->glyph->bitmap.rows),	0, GL_RED, GL_UNSIGNED_BYTE, face->glyph->bitmap.buffer))
				Gum::Output::error("FontManager::genTextures: glTexImage Failed.");


			// Now store character for later use
			Font::Character character = {
				texture, 
				ivec2(face->glyph->bitmap.width, face->glyph->bitmap.rows),
				ivec2(face->glyph->bitmap_left, face->glyph->bitmap_top),
				static_cast<unsigned int>(face->glyph->advance.x)
			};

			font->setCharacter(codepoint, character);

			codepoint = FT_Get_Next_Char(face, codepoint, &gid);
		}
		//glPixelStorei(GL_UNPACK_ALIGNMENT, 4);
	}



	void FontManager::deleteFont(std::string FontName) 			  { this->mFonts.erase(FontName); }
	void FontManager::addFont(Font *font, std::string Identifier) { this->mFonts.at(Identifier) = font; }



	//
	// Getter
	//
	Font *FontManager::getDefaultFont() 					{ return this->pDefaultFont; }
	int FontManager::getNumberOfFonts() 					{ return this->mFonts.size(); }
	Font *FontManager::getFont(std::string fontname)
	{	
		if(mFonts.find(fontname) == mFonts.end())
			Gum::Output::fatal("Font: " + fontname + " does not exist");
		return mFonts[fontname];
	}

	FontManager* Fonts = nullptr;
	void initFonts()
	{
		if(Fonts == nullptr)
			Fonts = FontManager::getInstance();
	}

	void cleanupFonts()
	{
		Gum::_delete(Fonts);
	}
}}