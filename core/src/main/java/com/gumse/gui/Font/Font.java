package com.gumse.gui.Font;

import java.util.ArrayList;

import java.awt.FontFormatException;
import java.io.IOException;

public class Font
{
    private ArrayList<Character> vCharacters;

    private String sPath;
    private String sName;
    private float fHighestGlyph;
    private String sKnownAlphabet;

    public Font(String name, String path) 
    {
        this.sName = name;
        this.sPath = path;
        this.fHighestGlyph = 0.0f;
        vCharacters = new ArrayList<>();
        vCharacters.ensureCapacity(1);
    }


	public static com.gumse.gui.Font.Font loadFontFromResource(String name, String charactersToLoad)
	{
		com.gumse.gui.Font.Font font = new com.gumse.gui.Font.Font(name, "resource");
		//genTextures(font, face);


		java.awt.Font someFont = null;
		try {
			someFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, FontManager.class.getClassLoader().getResourceAsStream(name));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(someFont == null)
			return null;
		
		for(int i = 0; i < charactersToLoad.length(); i++) 
		{
            String ch = charactersToLoad.substring(i, i + 1);
            int codepoint = ch.codePointAt(0);

			Character character = FontLoader.getFontChar(someFont, ch);
            font.setCharacter(codepoint, character);
		}
		
        font.setCharacter(127, new Character());
		font.calcHighestGlyph();
        font.sKnownAlphabet = charactersToLoad;
		
		return font;
	}

    public void cleanup()
    {
        for(int i = 0; i < vCharacters.size(); i++)
        {
            if(vCharacters.get(i).texture != null)
                vCharacters.get(i).texture.cleanup();
        }
    }

    public void calcHighestGlyph()
    {
        for(Character ch : vCharacters)
        {
            if(ch == null) continue;
            if(ch.texture == null) continue;
            if(ch.texture.getSize().y > fHighestGlyph)
                fHighestGlyph = ch.texture.getSize().y;
        }
    }


    //Setter
    public void setCharacter(int codepoint, Character charStruct)  
    {
        if(codepoint >= vCharacters.size())
        {
            for(int i = vCharacters.size(); i <= codepoint; i++)
                vCharacters.add(i, null);
        }
        this.vCharacters.ensureCapacity(codepoint + 1);
        this.vCharacters.set(codepoint, charStruct);
    }

    //Getter
    public String getPath()                                     { return this.sPath; }
    public String getName()                                     { return this.sName; }
    public String getKnownAlphabet()                            { return this.sKnownAlphabet; }
    public float getHighestGlyphSize()                          { return this.fHighestGlyph; }
    public Character getCharacter(int codepoint)               
    { 
        if(codepoint >= 0 && codepoint < vCharacters.size())
            return this.vCharacters.get(codepoint); 
        return new Character();
    }
};