layout (location = 0) in vec4 info;
out vec2 Texcoord;
uniform mat4 projection;
uniform vec2 position;
uniform vec2 scale;

void main()
{
    gl_Position = projection * vec4(info.xy * scale + position, 0, 1);
    Texcoord = vec2(info.z, info.w);
}