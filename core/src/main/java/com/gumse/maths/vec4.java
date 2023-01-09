package com.gumse.maths;

import com.gumse.tools.Output;

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


    public static vec4 add(vec4 a, vec4 b)          { return new vec4(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w); }
    public static vec4 sub(vec4 a, vec4 b)          { return new vec4(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w); }
    public static vec4 sub(vec4 a, float f)         { return new vec4(a.x - f, a.y - f, a.z - f, a.w - f); }
    public static vec4 mul(vec4 a, vec4 b)          { return new vec4(a.x * b.x, a.y * b.y, a.z * b.z, a.w * b.w); }
    public static vec4 mul(vec4 a, float f)         { return new vec4(a.x * f, a.y * f, a.z * f, a.w * f); }
    public static vec4 div(vec4 a, float f)         { return new vec4(a.x / f, a.y / f, a.z / f, a.w / f); }
    public static vec4 mix(vec4 a, vec4 b, float f) { return add(mul(a, (1 - f)), mul(b, f)); }

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

    public String toString()
    {
        return "vec4(" + Float.toString(this.x) + ", " + Float.toString(this.y) + ", " + Float.toString(this.z) + ", " + Float.toString(this.w) + ")";
    }

    public void print()
    {
        Output.info(toString());
    }
}
