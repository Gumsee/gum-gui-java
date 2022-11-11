package com.gumse.gui.Font;

import com.gumse.maths.ivec2;
import com.gumse.textures.Texture;

public class Character 
{
    public Texture texture = null; // ID handle of the glyph texture
    public ivec2 Bearing = new ivec2(0,0);          // Offset from baseline to left/top of glyph
    public int Advance = 0;   // Offset to advance to next glyph
};
