in vec3 vertexPosition;

uniform mat4 transmat;
uniform mat4 orthomat;

void main()
{
    gl_Position = orthomat * transmat * vec4(vertexPosition.x, vertexPosition.y, 0.0f, 1.0f);
}