#pragma once
#include <string>
#include <map>
#include <gum-maths.h>

namespace Gum {
namespace GUI {
class Font
{
public:
    struct Character 
    {
        unsigned int TextureID; // ID handle of the glyph texture
        ivec2 Size;             // Size of glyph
        ivec2 Bearing;          // Offset from baseline to left/top of glyph
        unsigned int Advance;   // Offset to advance to next glyph
    };

private:
    std::vector<Character> vCharacters;

    std::string sPath;
    std::string sName;
    float fHighestGlyph;

public:
    Font(const std::string& name, const std::string& path);
    ~Font();

    void calcHighestGlyph();

    //Setter
    void setCharacter(const unsigned int& codepoint, const Character& charStruct);

    //Getter
    std::string getPath() const;
    std::string getName() const;
    Character getCharacter(const unsigned int& codepoint);
    float getHighestGlyphSize() const;
};
}}