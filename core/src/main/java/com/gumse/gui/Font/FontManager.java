package com.gumse.gui.Font;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL30;

import com.gumse.textures.Texture;
import com.gumse.tools.Debug;


public class FontManager
{
	private String FONT_PATH;
	private com.gumse.gui.Font.Font pDefaultFont;
	private Map<String, com.gumse.gui.Font.Font> mFonts;
	private static FontManager pInstance = null;

	

	private FontManager()
	{
		mFonts = new HashMap<>();
		FontLoader.init();
		//Load Default Font from memory
		//pDefaultFont = loadFontFromMemory("default", defaultFont, sizeof(defaultFont) * sizeof(unsigned char));
		String germanKeyboard = "abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"§$%&/()=?`´²³#'*+~-_.:,;<>|°^\\ß{[]}@€µ öäü\n";
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
		Debug.debug("FontManager: Cleaning...");
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
			Debug.error("Font: " + fontname + " does not exist");
		return mFonts.get(fontname);
	}

};
