in vec3 vertexPosition;
out vec2 Texcoord;
out vec2 vertexPos;
out vec2 guisize;
uniform mat4 transmat;
uniform mat4 orthomat;
uniform bool invertY;
uniform ivec2 resolution;

void main()
{
    vec2 finalPos = vertexPosition.xy;
    gl_Position = orthomat * transmat * vec4(finalPos, 0.0f, 1.0f);
    vertexPos = finalPos;
    Texcoord = vertexPosition.xy;
    if(!invertY) { Texcoord.y = 1 - Texcoord.y; }
    guisize = resolution;
}