package com.gumse.gui.TagList;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.gui.GUIShader;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.mat4;
import com.gumse.maths.vec3;
import com.gumse.maths.vec4;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;
import com.gumse.system.Window;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Debug;

public class TagListEntry extends RenderGUI
{
    public interface TagRemoveCallback
    {
        public void run(TagListEntry entry);
    }

    private TextBox pTextBox;
    private mat4 m4CrossMatrix;
    private boolean bHovering;
    private TagRemoveCallback pCallback;
    private String sName;

    private static final vec4 v4CrossColor = new vec4(0.7f, 0.22f, 0.22f, 1.0f);
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

    public TagListEntry(ivec2 pos, String tagstr, Font font, TagRemoveCallback callback)
    {
        this.vPos.set(pos);
        this.vSize.set(new ivec2(0, 100));
        this.bHovering = false;
        this.pCallback = callback;
        this.sName = tagstr;

        initVAO();

        pTextBox = new TextBox(tagstr, font, new ivec2(0, 0), new ivec2(100, 100));
        pTextBox.setSizeInPercent(true, true);
        pTextBox.setColor(GUI.getTheme().secondaryColor);
        pTextBox.setCornerRadius(new vec4(10));
        pTextBox.setAlignment(Alignment.LEFT);
        pTextBox.setTextOffset(new ivec2(-5, 0));
        addElement(pTextBox);

        vActualSize.x = pTextBox.getTextSize().x + 25;

        setSizeInPercent(false, true);
        reposition();
    }

    @Override
    protected void updateOnPosChange()
    {
        vec3 rot = new vec3(0,0,-45);
        float size = vActualSize.y * 0.25f;

        mat4 model = new mat4();
        model.translate(new vec3(
            vActualPos.x + vActualSize.x - size - 3, 
            Framebuffer.CurrentlyBoundFramebuffer.getSize().y - vActualPos.y - size * 2.0f, 
            0));
        model.scale(new vec3(size, size, 1.0f));
        model.rotate(rot);
        model.transpose();
        
        m4CrossMatrix = model;
    }

    @Override
    protected void updateOnSizeChange()
    {
        pTextBox.setTextSize((int)(vActualSize.y * 0.9f));
        vActualSize.x = pTextBox.getTextSize().x + 25;
    }

    @Override
    public void update() 
    {
        if(isMouseInside())
        {
            Mouse.setActiveHovering(true);
            Window.CurrentlyBoundWindow.getMouse().setCursor(Mouse.GUM_CURSOR_HAND);
            pTextBox.setColor(vec4.sub(GUI.getTheme().secondaryColor, 0.02f));
            bHovering = true;


            if(isClicked())
            {
                if(pCallback != null)
                    pCallback.run(this);
            }
        }
        else
        {
            pTextBox.setColor(GUI.getTheme().secondaryColor);
            bHovering = false;
        }

        updatechildren();
    }

    @Override
    public void render() 
    {
        pTextBox.render();
        renderCross();
    }

    private void renderCross()
    {
        GUIShader.getShaderProgram().use();
        GUIShader.getShaderProgram().loadUniform("orthomat", Framebuffer.CurrentlyBoundFramebuffer.getScreenMatrix());
        GUIShader.getShaderProgram().loadUniform("transmat", m4CrossMatrix);
        GUIShader.getShaderProgram().loadUniform("Uppercolor", bHovering ? v4CrossColor : GUI.getTheme().accentColor);
        GUIShader.getShaderProgram().loadUniform("borderThickness", 0.0f);
        GUIShader.getShaderProgram().loadUniform("hasTexture", false);
        pCrossVAO.bind();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 18);
        pCrossVAO.unbind();
        GUIShader.getShaderProgram().unuse();
    }

    public String getName()
    {
        return sName;
    }
}