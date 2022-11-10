#include "Font.h"
#include <GL/glew.h>

namespace Gum {
namespace GUI {
Font::Font(const std::string& name, const std::string& path) 
{
    this->sName = name;
    this->sPath = path;
    this->fHighestGlyph = 0.0f;
    vCharacters.resize(128);
}

Font::~Font()
{
    for(size_t i = 0; i < vCharacters.size(); i++)
    {
        if(vCharacters[i].TextureID != 0)
            glDeleteTextures(1, &vCharacters[i].TextureID);
    }
}

void Font::calcHighestGlyph()
{
    for(auto ch : vCharacters)
        if(ch.Size.y > fHighestGlyph)
            fHighestGlyph = ch.Size.y;
}


//Setter
void Font::setCharacter(const unsigned int& codepoint, const Character& charStruct)  
{
    if(codepoint >= this->vCharacters.size())
        this->vCharacters.resize(codepoint + 1);
    this->vCharacters[codepoint] = charStruct; 
}

//Getter
std::string Font::getPath() const                                       { return this->sPath; }
std::string Font::getName() const                                       { return this->sName; }
float Font::getHighestGlyphSize() const                                 { return this->fHighestGlyph; }
Font::Character Font::getCharacter(const unsigned int& codepoint)               
{ 
    if(codepoint >= 0 && (size_t)codepoint < vCharacters.size())
        return this->vCharacters[codepoint]; 
    return Character();
}
}}