package com.gumse.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import com.gumse.tools.*;

public class ShaderProperties {
    public int iType;
    public int id;
    public String sSourceCode;
    public String sFilename;

    public ShaderProperties(int type) 
    {
        this.iType = type;
        this.sSourceCode = "";
        this.sFilename = "";
        this.id = GL30.glCreateShader(this.iType);
    }

    public ShaderProperties(int type, String filename) 
    {
        this.iType = type;
        this.sFilename = filename;
        this.id = GL30.glCreateShader(this.iType);
    }

    public void load()
    {
        this.sSourceCode = Toolbox.loadResourceAsString(this.sFilename);
        if(sSourceCode.equals(""))
        {
            Debug.error("Failed to read file " + this.sFilename);
        }
    }

    public void compile()
    {
        GL30.glShaderSource(this.id, this.sSourceCode);
        GL30.glCompileShader(this.id);

        if (GL30.glGetShaderi(this.id, GL30.GL_COMPILE_STATUS) == GL11.GL_FALSE) 
        {
            Debug.error("Shader ("+this.sFilename+") wasn't able to be compiled correctly. Error log:\n" + 
                GL30.glGetShaderInfoLog(this.id, 1024));
        }
    }

    public void attachToProgram(int programID)
    {
        GL30.glAttachShader(programID, this.id);
    }

    public void delete()
    {
        GL30.glDeleteShader(this.id);
    }
}
