package com.gumse.gui.Primitives;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.gui.GUIShader;
import com.gumse.maths.ivec2;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;

public class Shape extends RenderGUI
{
    private static Map<String, VertexArrayObject> mShapes;
    private static void initVAO(String name, List<Float> vertices)
    {
        if(mShapes == null)
            mShapes = new HashMap<String, VertexArrayObject>();
        
        if(mShapes.get(name) == null)
        {
            VertexArrayObject shapeVAO = new VertexArrayObject();
            VertexBufferObject pCrossVBO = new VertexBufferObject();

            pCrossVBO.setData(vertices);
            shapeVAO.setVertexCount(vertices.size() / 3);
            shapeVAO.addAttribute(pCrossVBO, 0, 3, GL11.GL_FLOAT, 0, 0);

            mShapes.put(name, shapeVAO);
        }
    } 

    private String sShapeName;
    private VertexArrayObject pShapeVAO;

    public Shape(String shapename, ivec2 pos, ivec2 size, List<Float> vertices)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.sShapeName = shapename;
        initVAO(shapename, vertices);
        pShapeVAO = mShapes.get(shapename);
        
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
        pShapeVAO.bind();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, pShapeVAO.numVertices());
        pShapeVAO.unbind();
        GUIShader.getShaderProgram().unuse();
    }

    public String getShapeName()
    {
        return sShapeName;
    }
}
