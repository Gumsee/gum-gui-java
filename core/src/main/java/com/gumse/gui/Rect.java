package com.gumse.gui;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gumse.basics.Transformable;
import com.gumse.maths.vec2;
import com.gumse.maths.vec3;
import com.gumse.shader.GLSLShader;
import com.gumse.tools.Toolbox;

public class Rect extends Transformable
{
    private GLSLShader pShader;

    private int iVAO;
    private int iVBO;
    private int iTexVBO;
    private int iEBO;

    private ArrayList<Float> alVertices;
    private ArrayList<Float> alTexCoords;
    private ArrayList<Integer> alIndices;
    IntBuffer indexBuffer;

    public Rect(GLSLShader shader)
    {
        this.pShader = shader;
        this.vPosition = new vec3();
        this.vRotation = new vec3();

        this.alVertices = new ArrayList<Float>(Arrays.asList(new Float[] {
           -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            1.0f,  1.0f, 0.0f,
           -1.0f,  1.0f, 0.0f 
        }));

        this.alTexCoords = new ArrayList<Float>(Arrays.asList(new Float[] {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
        }));

        this.alIndices = new ArrayList<Integer>(Arrays.asList(new Integer[] {
            0, 3, 2,
            2, 1, 0
        }));

        FloatBuffer vertexBuffer = Toolbox.arrayList2FloatBuffer(this.alVertices);
        FloatBuffer texcoordBuffer = Toolbox.arrayList2FloatBuffer(this.alTexCoords);
        indexBuffer = Toolbox.arrayList2IntBuffer(this.alIndices);

        iVAO = GL30.glGenVertexArrays();
        iVBO = GL15.glGenBuffers();
        iTexVBO = GL15.glGenBuffers();
        iEBO = GL15.glGenBuffers();

        GL30.glBindVertexArray(iVAO);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, iVBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);


        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, iTexVBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texcoordBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iEBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        GL30.glBindVertexArray(0);
        updateTransformationMat();
    }

    public void render()
    {
        pShader.loadUniform("mTransformation", this.mTransformationMatrix);

        GL30.glBindVertexArray(iVAO);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iEBO);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glDrawElements(GL11.GL_TRIANGLES, this.alIndices.size(), GL11.GL_UNSIGNED_INT, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(1); 
        GL20.glDisableVertexAttribArray(0); 
        GL30.glBindVertexArray(0);
    }
 
    public void setPosition(vec2 pos)      { this.vPosition.set(pos, this.vPosition.z);  updateTransformationMat(); }
    public void setRotation(float rot)     { this.vRotation.z = rot;  updateTransformationMat(); }


    public void increasePosition(vec2 pos) { this.vPosition.add(new vec3(pos, 0));    updateTransformationMat(); }
    //public void increaseSize(vec2 size)    { this.vSize.add(size);    updateTransformationMat(); }


    //public vec2 getSize()                  { return this.vSize; }
}