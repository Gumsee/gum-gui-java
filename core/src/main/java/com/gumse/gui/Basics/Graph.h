#pragma once
#include "../Primitives/Box.h"
#include "../Primitives/Text.h"
#include "Slider.h"
#include <OpenGL/VertexArrayObject.h>

class Graph : public RenderGUI
{
private:
    Box *background;
    Text *dataText;
    Text *caption;

    std::vector<vec3> vPositions;
    std::vector<float> vData;

    vec2 pixelSize;
    std::string ValueType;
    
    VertexArrayObject* pVertexArrayObject;
    VertexBufferObject<vec3>* pVertexBuffer;

    int precision = 3;
    float *data;
    float fBiggestNum;
    float fSpeed;

protected:
    void updateOnColorChange();

public:
    Graph(std::string name, vec2 pos, vec2 size, float *data = nullptr);
    ~Graph();

    void render();
    void update();
    void bindData(float *data);
    
    void setPrecision(int precision);
    void setValueType(std::string type);
};