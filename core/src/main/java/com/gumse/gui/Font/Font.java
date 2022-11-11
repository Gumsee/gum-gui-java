package com.gumse.gui.Font;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.gumse.maths.ivec2;
import com.gumse.textures.Texture;
import java.awt.FontFormatException;
import java.io.IOException;

public class Font
{
    private ArrayList<Character> vCharacters;

    private String sPath;
    private String sName;
    private float fHighestGlyph;

    public Font(String name, String path) 
    {
        this.sName = name;
        this.sPath = path;
        this.fHighestGlyph = 0.0f;
        vCharacters = new ArrayList<>();
        vCharacters.ensureCapacity(1);
    }


	public static com.gumse.gui.Font.Font loadFontFromResource(String name)
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
		
		for(int i = 0; i < 256; i++) 
		{
			char ch = (char)i;
			//System.out.println(fontImage.limit());


			Character character = FontLoader.getFontChar(someFont, String.valueOf(ch));
            font.setCharacter(i, character);

			//fontImage = null;
		}
		

		font.calcHighestGlyph();
		
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
            if(ch.texture.getSize().y > fHighestGlyph)
                fHighestGlyph = ch.texture.getSize().y;
    }


    //Setter
    public void setCharacter(int codepoint, Character charStruct)  
    {
        if(codepoint >= this.vCharacters.size() - 1)
            this.vCharacters.ensureCapacity(codepoint + 1);
        this.vCharacters.add(codepoint, charStruct);
    }

    //Getter
    public String getPath()                                       { return this.sPath; }
    public String getName()                                       { return this.sName; }
    public float getHighestGlyphSize()                                 { return this.fHighestGlyph; }
    public Character getCharacter(int codepoint)               
    { 
        if(codepoint >= 0 && codepoint < vCharacters.size())
            return this.vCharacters.get(codepoint); 
        return new Character();
    }
};