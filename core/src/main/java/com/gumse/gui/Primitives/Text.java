package com.gumse.gui.Primitives;

import java.util.ArrayList;

import com.gumse.maths.*;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;

public class Text extends RenderGUI
{
    public class TextChar
    {
        public int index;
        public vec2 pos, scale;
        public int textureID;
    };

    //Font pFont;

    String sText;
    String sRenderText;

    VertexArrayObject pVAO;
    VertexBufferObject pVBO;
    ArrayList<TextChar> vChars;
    bbox2i bRenderBox;

    int uiPointSize;
    long ulMaxLength;

    float fFontWidth;
    float fFontHeight;
    float fPixelSize;
    float fScale;

    boolean bFadeOut, bFadeBothSides;

    /*vec2 getStringSize(const Gum::Unicode& str) const;
    void updateVAO();

    
    protected void updateOnPosChange();
    protected void updateOnSizeChange();


    public Text(const Gum::Unicode& text, Gum::GUI::Font *font, const ivec2& position, const size_t& maxlength = 0UL);
    public ~Text();

    void render();
    void applyStringChanges();


    //Setter
    void setString(const Gum::Unicode& str);
    void setScale(const float& scale);
    void setCharacterHeight(const float& height);
    void setMaxLength(const int& length);
    void setRenderBox(const bbox2i& box);

    //Getter
    Gum::Unicode& getString();
    vec2 getSize() const;
    vec2 getFullTextSize() const;
    vec2 getTextSize(const Gum::Unicode& str, const unsigned int& begin, const unsigned int& end) const;
    bbox2i getRenderBox() const;
    unsigned int getClosestCharacterIndex(const ivec2& point) const;*/
};