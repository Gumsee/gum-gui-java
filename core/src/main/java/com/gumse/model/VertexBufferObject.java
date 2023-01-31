package com.gumse.model;

import java.util.List;

import org.lwjgl.opengl.GL30;

import com.gumse.tools.Toolbox;

public class VertexBufferObject
{
    private int ivboID;
    private int iLength;

    
    public VertexBufferObject()
    {
        ivboID = 0;
        ivboID = GL30.glGenBuffers();
    }

    public void cleanup() 
    {
        GL30.glDeleteBuffers(ivboID);
    }

    public void bind()
    {
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, ivboID); 
    }

    public void unbind()
    {
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }

    public void setData(List<Float> data) { setData(data, GL30.GL_STATIC_DRAW); }
    public void setData(List<Float> data, int usage)
    {
        bind();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, Toolbox.arrayList2FloatBuffer(data), usage);
        iLength = data.size();
        unbind();
    }

    public int getID()     { return this.ivboID; }
    public int getLength() { return this.iLength; }
};