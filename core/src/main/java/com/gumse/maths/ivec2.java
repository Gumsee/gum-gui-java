package com.gumse.maths;

import com.gumse.tools.Debug;

public class ivec2 
{
    public int x;
    public int y;

    public ivec2()
    {
        this.x = 0;
        this.y = 0;
    }

    public ivec2(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public ivec2(int x)
    {
        this.x = x;
        this.y = x;
    }

    public ivec2(ivec2 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    public void add(ivec2 vec)
    {
        this.x += vec.x;
        this.y += vec.y;
    }

    public void sub(ivec2 vec)
    {
        this.x -= vec.x;
        this.y -= vec.y;
    }

    public ivec2 mul(ivec2 vec)
    {
        this.x *= vec.x;
        this.y *= vec.y;
        return this;
    }

    public ivec2 mul(int f)
    {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public void set(ivec2 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }


    public static ivec2 add(ivec2 a, ivec2 b)      { return new ivec2(a.x + b.x, a.y + b.y); }
    public static ivec2 sub(ivec2 a, ivec2 b)      { return new ivec2(a.x - b.x, a.y - b.y); }
    public static ivec2 mul(ivec2 a, int f)        { return new ivec2(a.x * f, a.y * f); }
    public static ivec2 mul(ivec2 a, float f)      { return new ivec2((int)(a.x * f), (int)(a.y * f)); }

    public static float length(ivec2 a)            { return (float)Math.sqrt(a.x * a.x + a.y * a.y); }
    public static float distance(ivec2 a, ivec2 b) { return ivec2.length(ivec2.sub(a, b)); }

    public String toString()
    {
        return "vec2(" + Float.toString(this.x) + ", " + Float.toString(this.y) + ")";
    }

    public void print()
    {
        Debug.info(toString());
    }
}