#pragma once
#include "Maths/bbox.h"
#include "Maths/mat.h"
#include "RenderGUI.h"
#include "../Font/Font.h"
#include <Essentials/Filesystem/XMLReader.h>
#include <Essentials/Unicode.h>
#include <OpenGL/VertexArrayObject.h>

class Text : public RenderGUI
{
private:

    struct TextChar
    {
        unsigned int index;
        vec2 pos, scale;
        unsigned int textureID;
    };

    Gum::GUI::Font *pFont;

    Gum::Unicode sText;
    Gum::Unicode sRenderText;

    VertexArrayObject* pVAO;
    VertexBufferObject<float>* pVBO;
    std::vector<TextChar> vChars;
    bbox2i bRenderBox;

    unsigned int uiPointSize;
    size_t ulMaxLength;

    float fFontWidth;
    float fFontHeight;
    float fPixelSize;
    float fScale;

    bool bFadeOut, bFadeBothSides;

    vec2 getStringSize(const Gum::Unicode& str) const;
    void updateVAO();

protected:
    void updateOnPosChange();
    void updateOnSizeChange();

public:
    Text(const Gum::Unicode& text, Gum::GUI::Font *font, const ivec2& position, const size_t& maxlength = 0UL);
    ~Text();

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
    unsigned int getClosestCharacterIndex(const ivec2& point) const;
};