package com.gumse.maths;

public class bbox2i
{
    public ivec2 pos, size;
    public bbox2i()
    {
        this.pos = new ivec2(0);
        this.size = new ivec2(0,0);
    }

    public bbox2i(ivec2 pos, ivec2 size)
    {
        this.pos = pos;
        this.size = size;
    }

    public ivec2 getPos()  { return pos; }
    public ivec2 getSize() { return size; }


    public void set(bbox2i other)
    {
        this.pos = other.pos;
        this.size = other.size;
    }
};