package com.gumse.shader;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import com.gumse.tools.Debug;

public class Shader
{
    private String sSource;
    private String sTypeName;
    private int iType;
    private int iShaderID;
    private boolean bIsCompiled = false;

    public class TYPES {
        public static final int VERTEX_SHADER                  = GL40.GL_VERTEX_SHADER;
        public static final int FRAGMENT_SHADER                = GL40.GL_FRAGMENT_SHADER;
        public static final int TESSELLATION_CONTROL_SHADER    = GL40.GL_TESS_CONTROL_SHADER;
        public static final int TESSELLATION_EVALUATION_SHADER = GL40.GL_TESS_EVALUATION_SHADER;
        public static final int GEOMETRY_SHADER                = GL40.GL_GEOMETRY_SHADER;
    };

    public static final String SHADER_VERSION_STR = "#version 330 core \n";

    public Shader(String sourcecode, int shadertype)
    {
        this.sSource = sourcecode;
        this.iType = shadertype;
        switch(this.iType)
        {
            case TYPES.VERTEX_SHADER:                  this.sTypeName = "Vertex Shader"; break;
            case TYPES.FRAGMENT_SHADER:                this.sTypeName = "Fragment Shader"; break;
            case TYPES.TESSELLATION_CONTROL_SHADER:    this.sTypeName = "Tessellation Control Shader"; break;
            case TYPES.TESSELLATION_EVALUATION_SHADER: this.sTypeName = "Tessellation Evaluation Shader"; break;
            case TYPES.GEOMETRY_SHADER:                this.sTypeName = "Geometry Shader"; break;
        }
        this.iShaderID = GL30.glCreateShader(this.iType);
    }

    public void cleanup()
    {
        if(iShaderID != 0)
            GL30.glDeleteShader(iShaderID);
    }

    public boolean compile()
    {
        if(!bIsCompiled)
        {
            Debug.debug("Shader: Compiling " + sTypeName);
            GL30.glShaderSource(iShaderID, sSource); //Pass sourceCode to OpenGL
            GL30.glCompileShader(iShaderID); //compile the shader

            //Error checking
            if (GL30.glGetShaderi(iShaderID, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE)
            {
                //The maxLength includes the NULL character
                String errorLog = GL30.glGetShaderInfoLog(iShaderID, 1024);
                GL30.glDeleteShader(iShaderID); //Don't leak the shader.
                Debug.error("Shader Failed to compile: " + errorLog);

                return false;
            }
            bIsCompiled = true;
        }
        return true;
    }

    //Getter
    public String getSourceCode() { return this.sSource; }
    public int getShaderType()    { return this.iType; }
    public int getShaderID()      { return this.iShaderID; }
};