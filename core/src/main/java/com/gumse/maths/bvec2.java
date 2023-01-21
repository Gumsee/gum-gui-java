package com.gumse.maths;

import com.gumse.tools.Output;

public class bvec2 
{
    public boolean x;
    public boolean y;

    public bvec2()
    {
        this.x = false;
        this.y = false;
    }

    public bvec2(boolean x, boolean y)
    {
        this.x = x;
        this.y = y;
    }

    public bvec2(boolean x)
    {
        this.x = x;
        this.y = x;
    }

    public bvec2(bvec2 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }


    public static bvec2 and(bvec2 a, boolean f) { return new bvec2(a.x && f, a.y && f); }
    public static bvec2 and(bvec2 a, bvec2 b) { return new bvec2(a.x && b.x, a.y && b.y); }
    
    public static bvec2 or(bvec2 a, boolean f) { return new bvec2(a.x || f, a.y || f); }
    public static bvec2 or(bvec2 a, bvec2 b) { return new bvec2(a.x || b.x, a.y || b.y); }


    public void print()
    {
        String vecstr = "bvec2(" + Boolean.toString(this.x) + ", " + Boolean.toString(this.y) + ")";
        Output.info(vecstr);
    }
}