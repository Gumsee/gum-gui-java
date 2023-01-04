in vec2 Texcoord;
uniform sampler2D textureSampler;
uniform vec4 color;
uniform vec2 bboxpos;
uniform vec2 bboxsize;
uniform bool fadestart;
uniform bool fade;

out vec4 fragColor;

void main()
{
    float fac = 1.0f;
    if(fade)
    {
        if(fadestart)
        {
            float halfsize = bboxsize.x / 2.0f;
            fac = 1 - (abs(gl_FragCoord.x - bboxpos.x - halfsize) / halfsize);
        }
        else
        {
            fac = 1 - (gl_FragCoord.x - bboxpos.x) / bboxsize.x;
        }
        fac *= 4.0f;
    }

    vec4 sample = vec4(1, 1, 1, texture(textureSampler, Texcoord).r);
    fragColor = color * sample;
    fragColor.a *= fac;
}