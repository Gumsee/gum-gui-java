in float Texcoord;
uniform float lineheight;
uniform float patternoffset;

uniform vec4 color1;
uniform vec4 color2;

layout(origin_upper_left) in vec4 gl_FragCoord;

out vec4 fragColor;

void main()
{    
    vec4 col = color1;
    if(mod((gl_FragCoord.y - patternoffset) / lineheight, 2.0f) >= 1.0f)
        col = color2;

    fragColor = col;
}