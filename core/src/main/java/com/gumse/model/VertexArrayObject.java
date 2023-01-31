package com.gumse.model;

import java.util.ArrayList;
import org.lwjgl.opengl.*;

import com.gumse.tools.Output;
import com.gumse.tools.Toolbox;

public class VertexArrayObject
{
    private int ivaoID;
    private int iIndexBuffer;
    private int iVertexCount;
	private ArrayList<Integer> vAttributes;


    public VertexArrayObject()
    {
        ivaoID = GL30.glGenVertexArrays();
        iIndexBuffer = 0;
        iVertexCount = 0;

        vAttributes = new ArrayList<>();
    }

    public void cleanup() 
    {
        GL30.glDeleteVertexArrays(ivaoID);
        vAttributes.clear();
    }

    public void bind()
    {
        GL30.glBindVertexArray(ivaoID);
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, iIndexBuffer);
        for (int i = 0; i < vAttributes.size(); i++) { GL30.glEnableVertexAttribArray(vAttributes.get(i)); }
    }

    public void unbind()
    {
        for (int i = 0; i < vAttributes.size(); i++) { GL30.glDisableVertexAttribArray(vAttributes.get(i)); }
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }


    public int addElementBuffer(ArrayList<Integer> indices) { return addElementBuffer(indices, GL30.GL_STATIC_DRAW); }
    public int addElementBuffer(ArrayList<Integer> indices, int usage)
    {
        iIndexBuffer = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, iIndexBuffer);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Toolbox.arrayList2IntBuffer(indices), usage);
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
        iVertexCount = indices.size();
        return iIndexBuffer;
    }


    public int addAttribute(VertexBufferObject vbo, int index, int dimension, int type, int stride, long offset) { return addAttribute(vbo, index, dimension, type, stride, offset, 0); }
    public int addAttribute(VertexBufferObject vbo, int index, int dimension, int type, int stride, long offset, int divisor)
    {
        bind();
        vbo.bind();
        
        if(vAttributes.contains(index))
        {
            Output.warn("VertexArrayObject: Attribute " + index + " has already been added! (Not doing anything..)");
        }
        else
        {
            GL30.glEnableVertexAttribArray(index);
            if(type == GL30.GL_BYTE || type ==  GL30.GL_UNSIGNED_BYTE || type == GL30.GL_SHORT || type == GL30.GL_UNSIGNED_SHORT || type == GL30.GL_INT || type == GL30.GL_UNSIGNED_INT)
                GL30.glVertexAttribIPointer(index, dimension, type, stride, offset);
            else
                GL30.glVertexAttribPointer(index, dimension, type, false, stride, offset);
            GL40.glVertexAttribDivisor(index, divisor);
            vAttributes.add(index);
        }
        vbo.unbind();
        unbind();
        return index;
    }


    int addAttributeMat4(VertexBufferObject vbo, int index, int type, int divisor)
    {
        /*GLsizei vec4Size = sizeof(vec4);
        addAttribute(vbo, 3, 4, type, 4 * vec4Size, 0 * vec4Size, divisor);
        addAttribute(vbo, 4, 4, type, 4 * vec4Size, 1 * vec4Size, divisor);
        addAttribute(vbo, 5, 4, type, 4 * vec4Size, 2 * vec4Size, divisor);
        addAttribute(vbo, 6, 4, type, 4 * vec4Size, 3 * vec4Size, divisor);*/
        return index;
    }

    public void setVertexCount(int count) { this.iVertexCount = count; }
    public int numVertices()              { return this.iVertexCount; }
    public int getID()                    { return this.ivaoID; }
};