in vec2 Texcoord;
in vec2 vertexPos;
in vec2 guisize;
uniform sampler2D textureSampler;
uniform vec4 Uppercolor;
uniform vec4 Lowercolor;
uniform vec4 borderColor;
uniform bool hasTexture;
uniform bool gradient;
uniform bool circleMode;
uniform bool rightgradient;
uniform bool isTextureGrayscale;
uniform vec4 radius;
uniform int borderThickness;

out vec4 fragColor;

float roundedBoxSDF(vec2 CenterPosition, vec2 Size, float Radius) 
{
    float r = max(Radius, 0.0f);
    return length(max(abs(CenterPosition) - Size + r, 0.0)) - r;
}

void main(void)
{
    vec4 TextureColor, color;
    TextureColor = texture(textureSampler, Texcoord);
    if(gradient)
    {
        float mixValue;
        if(rightgradient)   { mixValue = distance(Texcoord.x, 1); }
        else                { mixValue = distance(Texcoord.y, 1); }
        color = mix(Uppercolor,Lowercolor,mixValue);
    }
    else { color = Uppercolor; }
    if(hasTexture)  
    { 
        if(isTextureGrayscale)
            fragColor = vec4(TextureColor.r, TextureColor.r, TextureColor.r, TextureColor.g) * color; 
        else
            fragColor = TextureColor * color; 
    }
    else            
    { 
        fragColor = color; 
    }

    float borderThicknessf = borderThickness + 0.1;

    vec2 borderSize = guisize - vec2(borderThicknessf*2);


    if(circleMode)
    {
        float dist = distance(Texcoord, vec2(0.5, 0.5));
        float alpha = smoothstep(0.45 - fwidth(dist), 0.45, dist);
        fragColor = vec4(fragColor.rgb, 1 - alpha);
    }
    else
    {
        float dist, distBorder;
        vec2 borderCenter = Texcoord * guisize - (borderSize/2.0f) - vec2(borderThicknessf);
        vec2 cornerCenter = Texcoord * guisize - (guisize/2.0f);
        if(Texcoord.x < 0.5)
        {
            if(Texcoord.y > 0.5)
            {
                //Bottom Left
                if(borderThicknessf > 0)
                    distBorder = roundedBoxSDF(borderCenter, borderSize/2.0f, radius.w - borderThicknessf);
                dist = roundedBoxSDF(cornerCenter, guisize/2.0f, radius.w); 
            }
            else
            {
                //Top Left
                if(borderThicknessf > 0)
                    distBorder = roundedBoxSDF(borderCenter, borderSize/2.0f, radius.x - borderThicknessf);
                dist = roundedBoxSDF(cornerCenter, guisize/2.0f, radius.x); 
            }
        }
        else
        {
            if(Texcoord.y < 0.5)
            {
                //Bottom Right
                if(borderThicknessf > 0)
                    distBorder = roundedBoxSDF(borderCenter, borderSize/2.0f, radius.y - borderThicknessf);
                dist = roundedBoxSDF(cornerCenter, guisize/2.0f, radius.y); 
            }
            else
            {
                //Top Right
                if(borderThicknessf > 0)
                    distBorder = roundedBoxSDF(borderCenter, borderSize/2.0f, radius.z - borderThicknessf);
                dist = roundedBoxSDF(cornerCenter, guisize/2.0f, radius.z); 
            }
        }

        if(borderThickness > 0)
            fragColor = mix(fragColor, borderColor, clamp(distBorder, 0.0f, 1.0f));
            
        float smoothedAlpha = 1.0f - smoothstep(0.0f, 2.0f, dist);
        fragColor.a *= smoothedAlpha;
    }
}