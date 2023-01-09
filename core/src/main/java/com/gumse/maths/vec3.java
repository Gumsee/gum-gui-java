package com.gumse.maths;

import com.gumse.tools.Output;

public class vec3 {
    public float x;
    public float y;
    public float z;

    public vec3()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    
    public boolean equals(vec3 other)
    {
        if(other.x == x && other.y == y && other.z == z)
            return true;
        return false;
    }
    
    public boolean equalsRound(vec3 other)
    {
        if(Math.round(other.x) == Math.round(x) && 
           Math.round(other.y) == Math.round(y) && 
           Math.round(other.z) == Math.round(z))
            return true;
        return false;
    }

    public vec3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public vec3(float x)
    {
        this.x = x;
        this.y = x;
        this.z = x;
    }

    public vec3(vec2 vec, float z)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = z;
    }


    public vec3(vec3 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public void add(vec3 vec)
    {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }

    public void div(vec3 vec)
    {
        this.x /= vec.x;
        this.y /= vec.y;
        this.z /= vec.z;
    }

    public void div(float f)
    {
        this.x /= f;
        this.y /= f;
        this.z /= f;
    }

    public static vec3 sub(vec3 a, vec3 b)  { return new vec3(a.x - b.x, a.y - b.z, a.z - b.z); }
    public static vec3 mul(vec3 a, float f) { return new vec3(a.x * f, a.y * f, a.z * f); }
    public static vec3 div(vec3 a, float f) { return new vec3(a.x / f, a.y / f, a.z / f); }


    public static vec3 mul(mat3 a, vec3 v) 
    { 
        return new vec3(a.get(0, 0) * v.x + a.get(0, 1) * v.y + a.get(0, 2) * v.z, 
                        a.get(1, 0) * v.x + a.get(1, 1) * v.y + a.get(1, 2) * v.z,
                        a.get(2, 0) * v.x + a.get(2, 1) * v.y + a.get(2, 2) * v.z); 
    }

    public float valueByIndex(int index)
    {
        switch(index)
        {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: return 1;
        }
    }

    public void set(vec3 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public void set(vec2 vec, float z)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = z;
    }

    public static vec3 normalize(vec3 v)
    {
        float length_of_v = (float)Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
        return new vec3(v.x / length_of_v, v.y / length_of_v, v.z / length_of_v);
    }

    public static vec3 cross(vec3 a, vec3 b) { return new vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x); }
    public static float dot(vec3 a, vec3 b)  { return a.x * b.x + a.y * b.y + a.z * b.z; }


    public String toString() { return toString(true, "vec3(", ")", ", "); }
    public String toString(boolean oneline, String prefix, String suffix, String delimiter)
    {
        return prefix + this.x + delimiter + this.y + delimiter + this.z + suffix;
    }  

    public void print()
    {
        Output.info(toString());
    }

    public vec2 xy()
    {
        return new vec2(this.x, this.y);
    }
}
