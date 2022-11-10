#include "Graph.h"
#include <iomanip>
#include <sstream>
#include "../Font/FontManager.h"
#include "OpenGL/Shader.h"
#include "OpenGL/ShaderProgram.h"
#include <Essentials/FPS.h>

ShaderProgram* GraphShader = nullptr;

void initGraphShader()
{
    if(GraphShader == nullptr)
    {
        Shader vertexShader(R"(
            #version 330 core
            in vec3 vertexPosition;
            out vec2 Texcoord;

            uniform mat4 transformationMatrix;
            uniform mat4 projectionMatrix;
            uniform mat4 viewMatrix;

            void main() 
            {
                vec2 finalpos = vertexPosition.xy;
                if(finalpos.y > 1) { finalpos.y = 1; }
                gl_Position = vec4(finalpos, 0.0f, 1.0f);
                Texcoord = vec2((vertexPosition.x+1.0)/2.0 * 1, 1 - (vertexPosition.y+1.0)/2.0 * 1);
            }
        )", Shader::VERTEX_SHADER);

        Shader fragShader(R"(
            #version 330 core
            in vec2 Texcoord;
            uniform vec4 color;

            void main(void) 
            {
                gl_FragColor = color;
            }
        )", Shader::FRAGMENT_SHADER);

        GraphShader = new ShaderProgram();
        GraphShader->addShader(&vertexShader);
        GraphShader->addShader(&fragShader);
        GraphShader->build("GraphShader", {{"vertexPosition", 0}});
        GraphShader->addUniform("color");
        GraphShader->addUniform("transformationMatrix");
        GraphShader->addUniform("projectionMatrix");
        GraphShader->addUniform("viewMatrix");
    }
}


Graph::Graph(std::string name, vec2 pos, vec2 size, float *data)
{
    this->sType = "Graph";
    this->vPos = pos;
    this->vSize = size;
    this->fBiggestNum = 0.0f;
    this->fSpeed = 200.0f;
    this->v4Color = vec4(0.9f, 0.9f, 0.9f, 1.0f);

    if(data != nullptr)
        this->data = data;


    background = new Box(ivec2(0,0), ivec2(100, 100));
    background->setCornerRadius(vec4(10.0f));
    background->setSizeInPercent(true, true);
    background->setColor(vec4(0,0,0, 0.5));
    addElement(background);

    dataText = new Text("0", Gum::GUI::Fonts->getDefaultFont(), vec2(0, 5));
    dataText->setPositionInPercent(false, false);
    dataText->setCharacterHeight(20);
    dataText->setColor(v4Color);
    addElement(dataText);

    caption = new Text(name, Gum::GUI::Fonts->getDefaultFont(), vec2(5, 5));
    caption->setPositionInPercent(false, false);
    caption->setCharacterHeight(20);
    caption->setColor(v4Color);
    addElement(caption);

    pixelSize = vec2(1.0f) / size;

    pVertexArrayObject = new VertexArrayObject();
    pVertexBuffer = new VertexBufferObject<vec3>();
    pVertexBuffer->setData(vPositions, GL_DYNAMIC_DRAW);
    pVertexArrayObject->addAttribute(pVertexBuffer, 0, 3, GL_FLOAT, sizeof(vec3), offsetof(vec3, x));

    resize();
    reposition();
}

Graph::~Graph() { }

void Graph::render()
{
    renderchildren();

    GraphShader->use();
    GraphShader->LoadUniform("color", v4Color);
    
    glViewport(vActualPos.x, Gum::Window::CurrentlyBoundWindow->getSize().y - background->getSize().y - vActualPos.y, background->getSize().x, background->getSize().y);
	pVertexArrayObject->bind();
    pVertexBuffer->setData(vPositions, GL_DYNAMIC_DRAW);
    
	glDrawArrays(GL_LINE_STRIP, 0, vPositions.size());
	pVertexArrayObject->unbind();
    glViewport(0,0, Gum::Window::CurrentlyBoundWindow->getSize().x, Gum::Window::CurrentlyBoundWindow->getSize().y);
    GraphShader->unuse();
}


void Graph::update()
{
    updatechildren();

    vData.push_back(*data);
    vPositions.push_back(vec3(1,0,0));

    fBiggestNum = 0.0f;
    for(size_t i = 0; i < vData.size(); i++)
    {
        if(vData[i] > fBiggestNum)
            fBiggestNum = vData[i];
    }

    for(size_t i = 0; i < vData.size(); i++)
    {
        float ypos = ((vData[i] / fBiggestNum) * 2.0f -1);
        vPositions[i].y = ypos - 0.3f;
        vPositions[i].x = vPositions[i].x - pixelSize.x * fSpeed * FPS::get();
        if(vPositions[i].x < -1)
        {
            vData.erase(vData.begin() + i);
            vPositions.erase(vPositions.begin() + i);
        }
    }

	std::stringstream sstream;
	sstream << std::fixed << std::setprecision(precision) << *data;
	dataText->setString(sstream.str() + ValueType);
    dataText->setPosition(vec2(background->getSize().x - dataText->getSize().x - 15, 5));
}

void Graph::updateOnColorChange()
{
    dataText->setColor(v4Color);
    caption->setColor(v4Color);
}

void Graph::bindData(float *data)          { this->data = data; }
void Graph::setPrecision(int precision)    { this->precision = precision; }
void Graph::setValueType(std::string type) { this->ValueType = type; }