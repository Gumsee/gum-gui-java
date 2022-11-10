#include "Text.h"
#include <GL/glew.h>

#include "../GUIShader.h"
#include "Essentials/Unicode.h"
#include "Maths/bbox.h"
#include <System/Output.h>
#include <OpenGL/VertexArrayObject.h>
#include <OpenGL/VertexBufferObject.h>
#include <System/MemoryManagement.h>
#include <limits>

Text::Text(const Gum::Unicode& text, Gum::GUI::Font *font, const ivec2& position, const size_t& maxlength)
{
    this->vPos = position;
    this->pFont = font;
    this->sText = text;
    this->ulMaxLength = maxlength;
    this->uiPointSize = 0;
    this->fScale = 1.0f;
    this->v4Color = vec4(0.19, 0.2, 0.42, 1);
    this->sType = "Text";
    this->bRenderBox = bbox2i(ivec2(0,0), ivec2(std::numeric_limits<int>::max()));
    this->bFadeOut = false;
    this->bFadeBothSides = false;

    std::vector<float> vertices = {
        1.0f, 0.0f, 1.0f, 1.0f,  
        0.0f, 1.0f, 0.0f, 0.0f,          
        0.0f, 0.0f, 0.0f, 1.0f,

        0.0f, 1.0f, 0.0f, 0.0f,
        1.0f, 1.0f, 1.0f, 0.0f,
        1.0f, 0.0f, 1.0f, 1.0f,
    };

    pVAO = new VertexArrayObject();
    pVBO = new VertexBufferObject<float>;
    pVBO->setData(vertices);
    pVAO->addAttribute(pVBO, 0, 4, GL_FLOAT, 0, 0);
    setString(text);

    updateVAO();
}

Text::~Text() 
{ 
    Gum::_delete(pVAO);
    Gum::_delete(pVBO);
}


void Text::updateVAO()
{
    vChars.clear();

    float x = vActualPos.x;
    float y = vActualPos.y;
    y = Gum::Window::CurrentlyBoundWindow->getSize().y - vActualPos.y;
    y -= this->pFont->getHighestGlyphSize() * fScale;

    // Iterate through all characters
    int line = 0;
    float bearing = 0.0f;
    bFadeOut = false;
    bFadeBothSides = false;

    for (size_t i = 0; i < sRenderText.length(); i++)
    {
        if(sRenderText[i] == "\n")
        {
            line++;
            x -= bearing;
            bearing = 0;
        }
        else
        {
            Gum::GUI::Font::Character ch = pFont->getCharacter(sRenderText.getCodepoint(i));
            vec2 scale = vec2(ch.Size.x * fScale, ch.Size.y * fScale);

            float charsizey = (ch.Size.y - ch.Bearing.y) * fScale;
            vec2 pos = vec2(x + ch.Bearing.x * fScale,
                            y - charsizey - this->pFont->getHighestGlyphSize() * fScale * line * 1.5);
                         
            
            bearing += (ch.Bearing.x + ch.Size.x + (ch.Advance >> 6)) * fScale;
            // Now advance cursors for next glyph (note that advance is number of 1/64 pixels)
            x += (ch.Advance >> 6) * fScale; // Bitshift by 6 to get value in pixels (2^6 = 64)

               

            if(pos.x + scale.x > bRenderBox.pos.x + bRenderBox.size.x)
            {
                bFadeOut = true;
            }
            if(pos.x < bRenderBox.pos.x)
            {
                bFadeOut = true;
                bFadeBothSides = true;
            }
            if(pos.x > bRenderBox.pos.x + bRenderBox.size.x || pos.x + scale.x < bRenderBox.pos.x)
                continue;
            
            vChars.push_back({ (unsigned int)i, pos, scale, ch.TextureID });
        }
    }
}

void Text::updateOnPosChange()
{
    updateVAO();
}

void Text::updateOnSizeChange()
{
    updateVAO();
}


void Text::render()
{
    Gum::GUI::getTextShaderProgram()->use();
    Gum::GUI::getTextShaderProgram()->LoadUniform("color", v4Color);
    Gum::GUI::getTextShaderProgram()->LoadUniform("projection", Gum::Window::CurrentlyBoundWindow->getScreenMatrix());
    Gum::GUI::getTextShaderProgram()->LoadUniform("bboxpos", (vec2)bRenderBox.getPos());
    Gum::GUI::getTextShaderProgram()->LoadUniform("bboxsize", (vec2)bRenderBox.getSize());
    Gum::GUI::getTextShaderProgram()->LoadUniform("fade", bFadeOut);
    Gum::GUI::getTextShaderProgram()->LoadUniform("fadestart", bFadeBothSides);

    glActiveTexture(GL_TEXTURE0);
    pVAO->bind();

    glEnable(GL_BLEND);
    for (size_t i = 0; i < vChars.size(); i++)
    {
        Gum::GUI::getTextShaderProgram()->LoadUniform("position", vChars[i].pos);
        Gum::GUI::getTextShaderProgram()->LoadUniform("scale", vChars[i].scale);
        glBindTexture(GL_TEXTURE_2D, vChars[i].textureID);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 6);
    }
    pVAO->unbind();
    glBindTexture(GL_TEXTURE_2D, 0);
    
    Gum::GUI::getTextShaderProgram()->unuse();
}

void Text::applyStringChanges()
{
    this->sRenderText = sText;
    if(ulMaxLength > 0L)
    {
        if(sText.length() > ulMaxLength)
        {
            sRenderText = sText.substr(0, ulMaxLength);
            sRenderText.append(u8"...");
        }
    }
    updateVAO();
}


//
// Setter
//
void Text::setString(const Gum::Unicode& str)
{
    this->sText = str;
    applyStringChanges();
}

void Text::setScale(const float& scale)            { this->fScale = scale; }
void Text::setCharacterHeight(const float& height) { this->fScale = height / this->pFont->getHighestGlyphSize();  updateVAO(); }
void Text::setMaxLength(const int& length)         { this->ulMaxLength = length; }
void Text::setRenderBox(const bbox2i& box)         { this->bRenderBox = box; }


//
// Getter
//
vec2 Text::getStringSize(const Gum::Unicode& str) const { return getTextSize(str, 0, str.length()); }
vec2 Text::getSize() const                              { return getStringSize(sRenderText); }
vec2 Text::getFullTextSize() const                      { return getStringSize(sText); }
Gum::Unicode& Text::getString()                         { return this->sText; }
bbox2i Text::getRenderBox() const                       { return this->bRenderBox; }
vec2 Text::getTextSize(const Gum::Unicode& str, const unsigned int& begin, const unsigned int& end) const
{
    if(begin < 0 || end > str.length())
        return vec2();
    
    vec2 vTextSize(0,0);
    vTextSize.y = this->pFont->getHighestGlyphSize() * fScale;
    float biggestX = 0;
    for(size_t i = begin; i < end; i++)
    {
        Gum::GUI::Font::Character ch = pFont->getCharacter(str.getCodepoint(i));
        vTextSize.x += (ch.Advance >> 6) * fScale;

        if(str[i] == "\n") 
        {
            vTextSize.x = 0;
            vTextSize.y += this->pFont->getHighestGlyphSize() * fScale;
        }

        if(vTextSize.x > biggestX) 
            biggestX = vTextSize.x;
    }
    vTextSize.x = biggestX;

    return vTextSize;
}

unsigned int Text::getClosestCharacterIndex(const ivec2& point) const
{
    unsigned int closest = 0;
    int closestDist = std::numeric_limits<int>::max();
    for(size_t i = 0; i < vChars.size(); i++)
    {
        int dist = std::abs(vChars[i].pos.x - point.x);
        if(dist < closestDist)
        {
            closestDist = dist;
            closest = vChars[i].index;
        }
    }
    if(vChars[vChars.size()-1].pos.x + vChars[vChars.size()-1].scale.x - point.x < closestDist)
        closest++;

    return closest;
}