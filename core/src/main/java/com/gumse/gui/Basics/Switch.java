package com.gumse.gui.Basics;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.gumse.gui.GUIShader;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Mouse;

public class Switch extends RenderGUI
{
    public enum Shape
    {
        SQUARE,
        CIRCLE,
        CHECK
    };


    private Box pBackground;
    private Box pTickbox;
    private Shape iShape;
    private mat4 m4CheckMatrix;
    private static VertexArrayObject pVAO;
    private boolean bIsTicked;

    static void initVAO()
    {
        if(pVAO == null)
        {
            float thickness = 0.3f;
            pVAO = new VertexArrayObject();
            VertexBufferObject pArrowVBO = new VertexBufferObject();

            // TODO
            pArrowVBO.setData(new ArrayList<Float>(Arrays.asList(new Float[] { 
                1.0f,             -1.0f,               0.0f,
                1.0f - thickness, -1.0f,               0.0f,
                1.0f - thickness, -(1.0f - thickness), 0.0f,

                1.0f - thickness, -(1.0f - thickness), 0.0f,
                1.0f,             -(1.0f - thickness), 0.0f,
                1.0f,             -1.0f,               0.0f,

                -thickness,       -(1.0f - thickness), 0.0f,
                1.0f - thickness, -(1.0f - thickness), 0.0f,
                -thickness,       -1.0f,               0.0f,

                -thickness,       -1.0f,               0.0f,
                1.0f - thickness, -(1.0f - thickness), 0.0f,
                1.0f - thickness, -1.0f,               0.0f,

                1.0f,             -(1.0f - thickness), 0.0f,
                1.0f - thickness, -(1.0f - thickness), 0.0f,
                1.0f - thickness,  1.0f,               0.0f,

                1.0f - thickness,  1.0f,               0.0f,
                1.0f,              1.0f,               0.0f,
                1.0f,             -(1.0f - thickness), 0.0f
            })));
            pVAO.addAttribute(pArrowVBO, 0, 3, GL11.GL_FLOAT, 0, 0);
        }
    }   

    public Switch(ivec2 pos, ivec2 size, float radius) 
    {
        this.sType = "Switch";
        this.vPos.set(pos);
        this.vSize.set(size);
        this.iShape = Shape.CHECK;
        this.bIsTicked = false;
        initVAO();

        pBackground = new Box(new ivec2(0,0), new ivec2(100, 100));
        pBackground.setCornerRadius(new vec4(radius));
        pBackground.setSizeInPercent(true, true);
        pBackground.setColor(new vec4(0.3f, 0.3f, 0.3f, 1.0f));
        addElement(pBackground);


        pTickbox = new Box(new ivec2(50,50), new ivec2(55, 55));
        pTickbox.setCornerRadius(new vec4(radius * 0.8f));
        pTickbox.setPositionInPercent(true, true);
        pTickbox.setSizeInPercent(true, true);
        pTickbox.setOrigin(new ivec2(50, 50));
        pTickbox.setOriginInPercent(true, true);
        pTickbox.setColor(new vec4(0.3f,0.6f,0.3f,1.0f));
        //pTickbox.setColor(vec4(Gum::Maths::HSVToRGB(vec3(rand() % 360, 100, 70)),1.0));
        addElement(pTickbox);

        resize();
        reposition();
    }

    public void cleanup() {};

    public void update()
    {
        if(isMouseInside())
        {
            Mouse.setActiveHovering(true);
            Window.CurrentlyBoundWindow.getMouse().setCursor(Mouse.GUM_CURSOR_HAND);
        }
        if(isClicked())
        {
            bIsTicked = !bIsTicked;
        }


        updatechildren();
    }

    protected void updateOnPosChange()
    {
        vec3 rot = new vec3(0,0,-45);

        mat4 model = new mat4();
        model.translate(new vec3(
            vActualPos.x + vActualSize.x * 0.5f - 1, 
            Window.CurrentlyBoundWindow.getSize().y - vActualPos.y - vActualSize.y * 0.25f, 
            0));
        model.scale(new vec3(vActualSize.x * 0.35f, vActualSize.y * 0.35f, 1.0f));
        model.rotate(rot);
        model.transpose();
        
        m4CheckMatrix = model;
    }

    @Override
    public void render() 
    {
        pBackground.render();
        if(bIsTicked)
        {
            switch(iShape)
            {
                case CHECK: renderCheck();     break;
                default:    pTickbox.render(); break;
            }
        }
    }

    private void renderCheck()
    {
        GUIShader.getShaderProgram().use();
        GUIShader.getShaderProgram().loadUniform("orthomat", Window.CurrentlyBoundWindow.getScreenMatrix());
        GUIShader.getShaderProgram().loadUniform("transmat", m4CheckMatrix);
        GUIShader.getShaderProgram().loadUniform("Uppercolor", new vec4(0.3f,0.6f,0.3f,1.0f));
        GUIShader.getShaderProgram().loadUniform("borderThickness", 0.0f);
        GUIShader.getShaderProgram().loadUniform("hasTexture", false);
        pVAO.bind();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 18);
        pVAO.unbind();
        GUIShader.getShaderProgram().unuse();
    }


    //
    // Setter
    //
    public void tick(boolean state)   { bIsTicked = state; }
    public void setShape(Shape shape) 
    { 
        this.iShape = shape; 
        switch(iShape)
        {
            case CIRCLE: pBackground.renderAsCircle(true);  pTickbox.renderAsCircle(true); break;
            case SQUARE: pBackground.renderAsCircle(false); pTickbox.renderAsCircle(false); break;
            default: break;
        }
    }


    //
    // Getter
    //
    public boolean isTicked()       { return bIsTicked; }

    public static Switch createFromXMLNode(XMLNode node)
    {
        float borderRadius = node.getFloatAttribute("border-radius", 0.0f);
        return new Switch(new ivec2(0,0), new ivec2(1,1), borderRadius);
    }
};