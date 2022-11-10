package com.gumse.model;

import com.gumse.maths.vec2;
import com.gumse.maths.vec3;

public class Vertex {
    public vec3 vPosition;
    public vec2 vTexcoord;
    public vec3 vNormal;
    public vec3 vTangent;

    public Vertex(vec3 pos, vec2 tex, vec3 norm)
    {
        this.vPosition = pos;
        this.vTexcoord = tex;
        this.vNormal = norm;
    }

    public Vertex(vec3 pos, vec2 tex, vec3 norm, vec3 tang)
    {
        this.vPosition = pos;
        this.vTexcoord = tex;
        this.vNormal = norm;
        this.vTangent = tang;
    }

    public Vertex()
    {
        vPosition = new vec3();
        vTexcoord = new vec2();
        vNormal = new vec3();
        vTangent = new vec3(0, 1, 0);
    }
}
