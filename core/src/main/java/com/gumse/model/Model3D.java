package com.gumse.model;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.*;

import com.gumse.basics.Transformable;
import com.gumse.maths.vec4;
import com.gumse.shader.Shader;
import com.gumse.shader.ShaderProgram;
import com.gumse.textures.Texture;
import com.gumse.tools.Toolbox;

public class Model3D extends Transformable
{
    private static ShaderProgram pDefaultShader = null;
    private ShaderProgram pShader;
    private int iVAO;
    private int iEBO;
    private int iNumIndices;
    private ArrayList<Integer> alAttributes;
    private vec4 v4Color;
    private Texture pTexture;

    public Model3D(ShaderProgram shader)
    {
        initDefaultShader();
        this.pTexture = null;
        this.pShader = shader;
        this.v4Color = new vec4(1.0f);
        if(this.pShader == null)
            this.pShader = pDefaultShader;
        
        alAttributes = new ArrayList<Integer>();
        updateTransformationMat();

        iVAO = GL30.glGenVertexArrays();
        iEBO = GL15.glGenBuffers();
    }


    private void addAttribute(ArrayList<Float> datalist, int index, int dimensions)
    {
        int vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Toolbox.arrayList2FloatBuffer(datalist), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, dimensions, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(index);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        alAttributes.add(index);
    }

    public void load(String filename)
    {
        ModelLoader modelLoader = new ModelLoader();
        modelLoader.load(filename);
        Mesh pMesh = modelLoader.getMesh(0);


        GL30.glBindVertexArray(iVAO);
        addAttribute(pMesh.getVertexPositions(), 0, 3);
        addAttribute(pMesh.getVertexTexcoords(), 1, 2);
        addAttribute(pMesh.getVertexNormals(),   2, 3);


        iNumIndices = pMesh.alIndices.size();
        IntBuffer indexBuffer = Toolbox.arrayList2IntBuffer(pMesh.alIndices);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iEBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }


    public void render()
    {
        pShader.loadUniform("transformationMatrix", mTransformationMatrix);
        pShader.loadUniform("color", v4Color);
        pShader.loadUniform("hasTexture", pTexture != null);
        if(pTexture != null)
            pTexture.bind(0);

        GL30.glBindVertexArray(iVAO);
        for(int i = 0; i < alAttributes.size(); i++)
            GL20.glEnableVertexAttribArray(alAttributes.get(i));
        
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iEBO);
        GL11.glDrawElements(GL11.GL_TRIANGLES, iNumIndices, GL11.GL_UNSIGNED_INT, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        for(int i = 0; i < alAttributes.size(); i++)
            GL20.glDisableVertexAttribArray(alAttributes.get(i));
        GL30.glBindVertexArray(0);

        Texture.unbind();
    }

    public static void initDefaultShader()
    {
        if(pDefaultShader != null)
            return;

        pDefaultShader = new ShaderProgram();
        pDefaultShader.addShader(new Shader(Shader.SHADER_VERSION_STR + Toolbox.loadResourceAsString("shaders/model.vert"), Shader.TYPES.VERTEX_SHADER));
        pDefaultShader.addShader(new Shader(Shader.SHADER_VERSION_STR + Toolbox.loadResourceAsString("shaders/model.frag"), Shader.TYPES.FRAGMENT_SHADER));
        pDefaultShader.build("ModelShader");
        pDefaultShader.addUniform("color");
        pDefaultShader.addUniform("hasTexture");
        pDefaultShader.addTexture("textureSampler", 0);
    }

    public static ShaderProgram getDefaultShader() { return pDefaultShader; }
    public void setColor(vec4 col)                 { this.v4Color = col; }
    public void setTexture(Texture tex)            { this.pTexture = tex; }
}
