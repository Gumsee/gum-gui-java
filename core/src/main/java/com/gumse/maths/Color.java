package com.gumse.maths;

public class Color {
    public static vec3 HSVToRGB(vec3 hsv) 
    {
        float h = hsv.x % 360, s = hsv.y, v = hsv.z;
        s /= 100;
        v /= 100;
    
        float c = v * s;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = v - c;
    
        float r = 0, g = 0, b = 0;
        if      (  0 <= h && h <  60)   { r = c; g = x; b = 0; }
        else if ( 60 <= h && h < 120)   { r = x; g = c; b = 0; }
        else if (120 <= h && h < 180)   { r = 0; g = c; b = x; }
        else if (180 <= h && h < 240)   { r = 0; g = x; b = c; }
        else if (240 <= h && h < 300)   { r = x; g = 0; b = c; }
        else if (300 <= h && h < 360)   { r = c; g = 0; b = x; }
    
        r = Math.round((r + m) * 255);
        g = Math.round((g + m) * 255);
        b = Math.round((b + m) * 255);
    
        return new vec3(r, g, b);
    }
}
