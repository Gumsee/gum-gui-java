package com.gumse.gui.Font;

import java.util.HashMap;
import java.util.Map;

import com.gumse.tools.Output;


public class FontManager
{
	private com.gumse.gui.Font.Font pDefaultFont;
	private Map<String, com.gumse.gui.Font.Font> mFonts;
	private static FontManager pInstance = null;

	

	private FontManager()
	{
		mFonts = new HashMap<>();
		FontLoader.init();
		//Load Default Font from memory
		//pDefaultFont = loadFontFromMemory("default", defaultFont, sizeof(defaultFont) * sizeof(unsigned char));
		String germanKeyboard = "abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"§$%&/()=?`´²³#'*+~-_.:,;<>|°^\\ß{[]}@€µ öäüÖÄÜẞ\n";
		pDefaultFont = Font.loadFontFromResource("fonts/opensans.ttf", germanKeyboard);
		mFonts.put("default", pDefaultFont);
		//Gum::_delete(pFreetypeLibrary);
	}	
	
	public static FontManager getInstance() 
	{
		if(pInstance == null)
			pInstance = new FontManager();
		return pInstance;
	}

	public void cleanup()
	{
		Output.debug("FontManager: Cleaning...");
		//for(auto font : mFonts)
		//	Gum::_delete(font.second);
		
		//Gum::_delete(pDefaultFont);

		mFonts.clear();
	}


	public void deleteFont(String FontName) 	       { this.mFonts.remove(FontName); }
	public void addFont(Font font, String Identifier) { this.mFonts.put(Identifier, font); }



	//
	// Getter
	//
	public Font getDefaultFont() 					{ return this.pDefaultFont; }
	public int getNumberOfFonts() 					{ return this.mFonts.size(); }
	public Font getFont(String fontname)
	{	
		if(!mFonts.containsKey(fontname))
			Output.error("Font: " + fontname + " does not exist");
		return mFonts.get(fontname);
	}

};
