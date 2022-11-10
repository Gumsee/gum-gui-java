package com.gumse.maths;

public class vec4 {
    public float x;
    public float y;
    public float z;
    public float w;

    public vec4()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public vec4(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public vec4(vec3 vec, float w)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        this.w = w;
    }

    public vec4(float x)
    {
        this.x = x;
        this.y = x;
        this.z = x;
        this.w = x;
    }

    float valueByIndex(int index)
    {
        switch(index)
        {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            case 3: return w;
            default: return 1;
        }
    }
}
