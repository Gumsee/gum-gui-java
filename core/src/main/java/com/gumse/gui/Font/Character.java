package com.gumse.gui.Font;

import com.gumse.maths.ivec2;
import com.gumse.textures.Texture;

public class Character 
{
    public Texture texture; // ID handle of the glyph texture
    public ivec2 Bearing;          // Offset from baseline to left/top of glyph
    public int Advance;   // Offset to advance to next glyph
};
