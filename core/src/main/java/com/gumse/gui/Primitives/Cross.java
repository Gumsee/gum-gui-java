package com.gumse.gui.Primitives;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.gui.GUIShader;
import com.gumse.maths.ivec2;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;

public class Cross extends RenderGUI
{
    private static VertexArrayObject pCrossVAO;
    static void initVAO()
    {
        if(pCrossVAO == null)
        {
            pCrossVAO = new VertexArrayObject();
            VertexBufferObject pCrossVBO = new VertexBufferObject();

            float thickness = 0.12f;
            pCrossVBO.setData(new ArrayList<Float>(Arrays.asList(new Float[] { 
                 1.0f, -thickness, 0.0f,
                -1.0f,  thickness, 0.0f,
                 1.0f,  thickness, 0.0f, 
                 1.0f, -thickness, 0.0f,
                -1.0f,  thickness, 0.0f,
                -1.0f, -thickness, 0.0f, 

                -thickness,  1.0f, 0.0f,
                 thickness, -1.0f, 0.0f,
                 thickness,  1.0f, 0.0f, 
                -thickness,  1.0f, 0.0f,
                 thickness, -1.0f, 0.0f,
                -thickness, -1.0f, 0.0f, 
            })));
            pCrossVAO.addAttribute(pCrossVBO, 0, 3, GL11.GL_FLOAT, 0, 0);
        }
    } 

    public Cross(ivec2 pos, ivec2 size)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.fRotation = 45.0f;
        
        initVAO();

        
        resize();
        reposition();
    }

    @Override
    public void renderextra()
    {
        GUIShader.getShaderProgram().use();
        GUIShader.getShaderProgram().loadUniform("orthomat", Framebuffer.CurrentlyBoundFramebuffer.getScreenMatrix());
        GUIShader.getShaderProgram().loadUniform("transmat", mTransformationMatrix);
        GUIShader.getShaderProgram().loadUniform("Uppercolor", getColor(GUI.getTheme().accentColor));
        GUIShader.getShaderProgram().loadUniform("borderThickness", 0.0f);
        GUIShader.getShaderProgram().loadUniform("hasTexture", false);
        pCrossVAO.bind();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 18);
        pCrossVAO.unbind();
        GUIShader.getShaderProgram().unuse();
    }
}