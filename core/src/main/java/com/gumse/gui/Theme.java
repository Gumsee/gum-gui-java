package com.gumse.gui;

import com.gumse.maths.vec4;

public class Theme 
{
    public vec4 backgroundColor;
    public vec4 primaryColor;
    public vec4 secondaryColor;
    public vec4 textColor;
    public vec4 accentColor;
    public vec4 accentColorShade1;
    public vec4 accentColorShade2;
    public vec4 accentColorShade3;
    public vec4 cornerRadius;
    public int borderThickness;

    public Theme()
    {
        backgroundColor   = new vec4(1,1,1,1);
        primaryColor      = new vec4(1,1,1,1);
        secondaryColor    = new vec4(1,1,1,1);
        textColor         = new vec4(0,0,0,1);
        accentColor       = new vec4(1,1,1,1);
        accentColorShade1 = new vec4(1,1,1,1);
        accentColorShade2 = new vec4(1,1,1,1);
        accentColorShade3 = new vec4(1,1,1,1);
        cornerRadius      = new vec4(0,0,0,0);
        borderThickness   = 0;
    }
}