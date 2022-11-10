package com.gumse.gui;

import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Map;

import com.gumse.shader.Shader;
import com.gumse.shader.ShaderProgram;
import com.gumse.tools.Toolbox;

public class GUIShader
{
    private static ShaderProgram pShaderProgram = null;
    private static ShaderProgram pTextShaderProgram = null;
    private static ShaderProgram pStripesShaderProgram = null;

    public static void initShaderProgram()
    {
        if(pShaderProgram == null)
        {
            pShaderProgram = new ShaderProgram();
            pShaderProgram.addShader(new Shader(Shader.SHADER_VERSION_STR + Toolbox.loadResourceAsString("shaders/GUI.vert"), Shader.TYPES.VERTEX_SHADER));
            pShaderProgram.addShader(new Shader(Shader.SHADER_VERSION_STR + Toolbox.loadResourceAsString("shaders/GUI.frag"), Shader.TYPES.FRAGMENT_SHADER));
            pShaderProgram.build("GUIShader");
            pShaderProgram.addUniform("transmat");
            pShaderProgram.addUniform("orthomat");
            pShaderProgram.addUniform("invertY");
            pShaderProgram.addUniform("Uppercolor");
            pShaderProgram.addUniform("Lowercolor");
            pShaderProgram.addUniform("borderColor");
            pShaderProgram.addUniform("borderThickness");
            pShaderProgram.addUniform("hasTexture");
            pShaderProgram.addUniform("gradient");
            pShaderProgram.addUniform("resolution");
            pShaderProgram.addUniform("rightgradient");
            pShaderProgram.addUniform("radius");
            pShaderProgram.addUniform("isTextureGrayscale");
            pShaderProgram.addTexture("textureSampler", 0);
        }

        if(pTextShaderProgram == null)
        {
            pTextShaderProgram = new ShaderProgram();
            pTextShaderProgram.addShader(new Shader(Shader.SHADER_VERSION_STR + Toolbox.loadResourceAsString("shaders/Text.vert"), Shader.TYPES.VERTEX_SHADER));
            pTextShaderProgram.addShader(new Shader(Shader.SHADER_VERSION_STR + Toolbox.loadResourceAsString("shaders/Text.frag"), Shader.TYPES.FRAGMENT_SHADER));
            Map<String, Integer> textShaderAttributes = new HashMap<String, Integer>();
            textShaderAttributes.put("info", 0);

            pTextShaderProgram.build("TextShader", textShaderAttributes); 
            pTextShaderProgram.addUniform("color");
            pTextShaderProgram.addUniform("projection");
            pTextShaderProgram.addUniform("position");
            pTextShaderProgram.addUniform("scale");
            pTextShaderProgram.addUniform("bboxpos");
            pTextShaderProgram.addUniform("bboxsize");
            pTextShaderProgram.addUniform("fadestart");
            pTextShaderProgram.addUniform("fade");
            pTextShaderProgram.addTexture("textureSampler", 0);
        }

        if(pStripesShaderProgram == null)
        {
            pStripesShaderProgram = new ShaderProgram();
            pStripesShaderProgram.addShader(new Shader(Shader.SHADER_VERSION_STR + Toolbox.loadResourceAsString("shaders/Stripes.vert"), Shader.TYPES.VERTEX_SHADER));
            pStripesShaderProgram.addShader(new Shader(Shader.SHADER_VERSION_STR + Toolbox.loadResourceAsString("shaders/Stripes.frag"), Shader.TYPES.FRAGMENT_SHADER));
            pStripesShaderProgram.build("StripesShader"); 
            pStripesShaderProgram.addUniform("transmat");
            pStripesShaderProgram.addUniform("orthomat");
            pStripesShaderProgram.addUniform("patternoffset");
            pStripesShaderProgram.addUniform("lineheight");
            pStripesShaderProgram.addUniform("color1");
            pStripesShaderProgram.addUniform("color2");
        }
    }


    public static ShaderProgram getShaderProgram()        { return pShaderProgram; }
    public static ShaderProgram getTextShaderProgram()    { return pTextShaderProgram; }
    public static ShaderProgram getStripesShaderProgram() { return pStripesShaderProgram; }

    public static void cleanupShaders()
    {
        /*Gum::_delete(pShaderProgram);
        Gum::_delete(pTextShaderProgram);
        Gum::_delete(pStripesShaderProgram);*/
    }
}