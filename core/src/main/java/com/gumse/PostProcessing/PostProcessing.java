/*package com.gumse.PostProcessing;

import com.gumse.gui.Rect;
import com.gumse.maths.vec2;
import com.gumse.shader.GLSLShader;
import com.gumse.system.Window;
import com.gumse.textures.Texture;
import com.gumse.tools.FPS;

public class PostProcessing {

    private Rect pRect;
    private GLSLShader pShader;
    private Framebuffer pFramebuffer;

    private final static String sVertexShaderCode = 
        "#version 330 core \n" +
        "layout (location = 0) in vec3 vPositions; \n" +
        "out vec2 texcoord; \n" +
        
        "void main() \n" +
        "{ \n" +
        "    gl_Position = vec4(vPositions, 1.0); \n" +
        "    texcoord = (vPositions.xy + vec2(1.0)) / 2.0; \n" +
        "}\n";

    public PostProcessing(String fragmentShaderFile)
    {
        pFramebuffer = new Framebuffer();
        pFramebuffer.addTextureAttachment();

        pShader = new GLSLShader();
        pShader.addShaderSrc(sVertexShaderCode,  GLSLShader.VERTEX_SHADER);
        pShader.addShader(fragmentShaderFile, GLSLShader.FRAGMENT_SHADER);
        pShader.build();
        pShader.addTexture("texture0", 0);
        pShader.addUniform("time");
        pShader.addUniform("aspectRatio");
        pShader.addUniform("offset");
        pShader.bind();
        pShader.loadUniform("aspectRatio", Window.getAspectRatio());
        pShader.unbind();
        pRect = new Rect(pShader);
    }

    public void render(Texture renderTexture, vec2 offset, boolean toFramebuffer)
    {
        if(toFramebuffer)
            pFramebuffer.bind();
        pShader.bind();
        pShader.loadUniform("time", FPS.getRuntime());
        pShader.loadUniform("offset", offset);
        renderTexture.bind(0);
        pRect.render();
        renderTexture.unbind();
        pShader.unbind();
        if(toFramebuffer)
            pFramebuffer.unbind();
    }

    public GLSLShader getShader()
    {
        return this.pShader;
    }

    public Texture getResultTexture()
    {
        return this.pFramebuffer.getTexture();
    }
}
*/