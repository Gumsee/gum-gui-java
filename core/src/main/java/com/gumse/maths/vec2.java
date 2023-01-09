package com.gumse.maths;

import com.gumse.tools.Output;

public class vec2 
{
    public float x;
    public float y;

    public vec2()
    {
        this.x = 0;
        this.y = 0;
    }

    public vec2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public vec2(float x)
    {
        this.x = x;
        this.y = x;
    }

    public vec2(vec2 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    public vec2(ivec2 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    public void add(vec2 vec)
    {
        this.x += vec.x;
        this.y += vec.y;
    }

    public vec2 mul(vec2 vec)
    {
        this.x *= vec.x;
        this.y *= vec.y;
        return this;
    }

    public vec2 mul(float f)
    {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public float dot(vec2 vec)
    {
        return vec.x * this.x + vec.y * this.y;
    }

    public void set(vec2 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    public static vec2 mul(vec2 a, float f)  { return new vec2(a.x * f, a.y * f); }
    public static vec2 add(vec2 a, vec2 b)   { return new vec2(a.x + b.x, a.y + b.y); }
    public static vec2 sub(vec2 a, vec2 b)   { return new vec2(a.x - b.x, a.y - b.y); }
    public static vec2 div(vec2 a, vec2 b)   { return new vec2(a.x / b.x, a.y / b.y); }
    public static vec2 div(vec2 a, float f)  { return new vec2(a.x / f, a.y / f); }

    public static float length(vec2 a)           { return (float)Math.sqrt(a.x * a.x + a.y * a.y); }
    public static float distance(vec2 a, vec2 b) { return vec2.length(vec2.sub(a, b)); }

    public String toString()
    {
        return "vec2(" + Float.toString(this.x) + ", " + Float.toString(this.y) + ")";
    }

    public void print()
    {
        Output.info(toString());
    }
}