package com.gumse.shader;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.*;

//import com.gumse.basics.Camera;
import com.gumse.maths.mat4;
import com.gumse.maths.vec2;
import com.gumse.maths.vec3;
import com.gumse.maths.vec4;
import com.gumse.tools.Debug;
import com.gumse.tools.Toolbox;

public class GLSLShader 
{
    public final static int VERTEX_SHADER   = GL30.GL_VERTEX_SHADER;
    public final static int FRAGMENT_SHADER = GL30.GL_FRAGMENT_SHADER;

    private ArrayList<ShaderProperties> alShaders;
    private int iShaderProgramID;
    private Map<String, Integer> mUniformLocations;

    public GLSLShader()
    {
        mUniformLocations = new HashMap<String, Integer>();
        iShaderProgramID = GL30.glCreateProgram();
        alShaders = new ArrayList<ShaderProperties>();
    }

    public void addShader(String filename, int type)
    {
        ShaderProperties vertexShader = new ShaderProperties(type, filename);
        vertexShader.load();
        vertexShader.compile();
        vertexShader.attachToProgram(iShaderProgramID);
        alShaders.add(vertexShader);
    }

    public void addShaderSrc(String shaderSrc, int type)
    {
        ShaderProperties vertexShader = new ShaderProperties(type, "Inline");
        vertexShader.sSourceCode = shaderSrc;
        vertexShader.compile();
        vertexShader.attachToProgram(iShaderProgramID);
        alShaders.add(vertexShader);
    }

    public void build()
    {
        this.linkProgram();
        for(ShaderProperties shader : alShaders)
            shader.delete();

        //Adding default uniforms
        this.addUniform("mPerspective");
        this.addUniform("mView");
        this.addUniform("mTransformation");
    }

    private void linkProgram()
    {
        GL30.glLinkProgram(iShaderProgramID);
        if(GL30.glGetProgrami(iShaderProgramID, GL30.GL_LINK_STATUS) == 0)
            Debug.error(this.getClass().getName() + " " + GL30.glGetProgramInfoLog(iShaderProgramID, 1024));
        
        GL30.glValidateProgram(iShaderProgramID);
        if(GL30.glGetProgrami(iShaderProgramID, GL30.GL_VALIDATE_STATUS) == 0)
            Debug.error(this.getClass().getName() +  " " + GL30.glGetProgramInfoLog(iShaderProgramID, 1024));
    }

    /*public void update(Camera camera)
    {
        this.bind();
        this.loadUniform("mView", camera.getViewMatrix());
        this.loadUniform("mPerspective", camera.getProjectionMatrix());
        this.unbind();
    }*/

    public void addUniform(String uniformName) { mUniformLocations.put(uniformName, GL30.glGetUniformLocation(this.iShaderProgramID, uniformName)); }
    public void addTexture(String textureName, int index) 
    { 
        this.bind();
        this.addUniform(textureName);
        this.loadUniform(textureName, index);
        this.unbind();
    }

    public void loadUniform(String uniform, int value)   { GL30.glUniform1i(mUniformLocations.get(uniform), value); }
    public void loadUniform(String uniform, float value) { GL30.glUniform1f(mUniformLocations.get(uniform), value); }
    public void loadUniform(String uniform, vec2 value)  { GL30.glUniform2f(mUniformLocations.get(uniform), value.x, value.y); }
    public void loadUniform(String uniform, vec3 value)  { GL30.glUniform3f(mUniformLocations.get(uniform), value.x, value.y, value.z); }
    public void loadUniform(String uniform, vec4 value)  { GL30.glUniform4f(mUniformLocations.get(uniform), value.x, value.y, value.z, value.w); }
    public void loadUniform(String uniform, mat4 value)  { FloatBuffer mat = Toolbox.array2D2FloatBuffer(value.getData()); GL30.glUniformMatrix4fv(mUniformLocations.get(uniform), false, mat); }

    public void bind()   { GL30.glUseProgram(iShaderProgramID); }
    public void unbind() { GL30.glUseProgram(0); }
    public int getProgramID() { return this.iShaderProgramID; }
}