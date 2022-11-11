in vec2 Texcoord;
in vec2 vertexPos;
in vec2 guisize;
uniform sampler2D textureSampler;
uniform vec4 Uppercolor;
uniform vec4 Lowercolor;
uniform vec4 borderColor;
uniform bool hasTexture;
uniform bool gradient;
uniform bool rightgradient;
uniform bool isTextureGrayscale;
uniform vec4 radius;
uniform float borderThickness;

float roundedBoxSDF(vec2 CenterPosition, vec2 Size, float Radius) 
{
    float r = max(Radius, 0.0f);
    return length(max(abs(CenterPosition) - Size + r, 0.0)) - r;
}

void main(void)
{
    vec4 TextureColor, color;
    TextureColor = texture2D(textureSampler, Texcoord);
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
            gl_FragColor = vec4(TextureColor.r, TextureColor.r, TextureColor.r, TextureColor.g) * color; 
        else
            gl_FragColor = TextureColor * color; 
    }
    else            
    { 
        gl_FragColor = color; 
    }


    vec2 borderSize = guisize - vec2(borderThickness*2);
    float dist, distBorder;
    vec2 borderCenter = Texcoord * guisize - (borderSize/2.0f) - vec2(borderThickness);
    vec2 cornerCenter = Texcoord * guisize - (guisize/2.0f);
    if(Texcoord.x < 0.5)
    {
        if(Texcoord.y > 0.5)
        {
            //Bottom Left
            if(borderThickness > 0)
                distBorder = roundedBoxSDF(borderCenter, borderSize/2.0f, radius.w - borderThickness);
            dist = roundedBoxSDF(cornerCenter, guisize/2.0f, radius.w); 
        }
        else
        {
            //Top Left
            if(borderThickness > 0)
                distBorder = roundedBoxSDF(borderCenter, borderSize/2.0f, radius.x - borderThickness);
            dist = roundedBoxSDF(cornerCenter, guisize/2.0f, radius.x); 
        }
    }
    else
    {
        if(Texcoord.y < 0.5)
        {
            //Bottom Right
            if(borderThickness > 0)
                distBorder = roundedBoxSDF(borderCenter, borderSize/2.0f, radius.y - borderThickness);
            dist = roundedBoxSDF(cornerCenter, guisize/2.0f, radius.y); 
        }
        else
        {
            //Top Right
            if(borderThickness > 0)
                distBorder = roundedBoxSDF(borderCenter, borderSize/2.0f, radius.z - borderThickness);
            dist = roundedBoxSDF(cornerCenter, guisize/2.0f, radius.z); 
        }
    }

    if(borderThickness > 0)
        gl_FragColor = mix(gl_FragColor, borderColor, clamp(distBorder, 0.0f, 1.0f));
        
    float smoothedAlpha = 1.0f - smoothstep(0.0f, 2.0f, dist);
    gl_FragColor.a *= smoothedAlpha;

    gl_FragColor = vec4(1,1,1, texture2D(textureSampler, Texcoord).r);
}