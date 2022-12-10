in vec2 texcoord;

out vec4 fragColor;
uniform sampler2D textureSampler;
uniform bool hasTexture;
uniform vec4 color;

void main() 
{
    if(hasTexture)
        fragColor = texture(textureSampler, texcoord) * color;
    else
        fragColor = color;
}