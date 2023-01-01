package com.gumse.gui.Primitives;

import java.util.ArrayList;
import java.util.Arrays;
import org.lwjgl.opengl.GL30;

import com.gumse.gui.GUI;
import com.gumse.gui.GUIShader;
import com.gumse.maths.*;
import com.gumse.textures.Texture;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;

public class Box extends RenderGUI
{
	private static VertexArrayObject pVAO = null;
	private Texture pTexture;
	private vec4 color2;
	private vec4 bordercolor;

	private boolean bGradient;
	private boolean bRightgradient;
	private boolean bInvertY;
    private boolean bIsCircle;
    
	private int iBorderThickness;


	public Box(ivec2 pos, ivec2 size) 
	{
		this.pTexture = null;
		this.sType = "box";
		this.vPos.set(pos);
		this.vSize.set(size);
		this.iBorderThickness = -1;
		this.bGradient = false;
		this.bRightgradient = false;
		this.bInvertY = false;
		this.color2 = new vec4();
		this.bordercolor = GUI.getTheme().accentColor;
		
		if(pVAO == null)
		{
			pVAO = new VertexArrayObject();
			VertexBufferObject pPositionVBO = new VertexBufferObject();

			
			pPositionVBO.setData(new ArrayList<Float>(Arrays.asList(new Float[] {
				-1.0f, 1.0f, 0.0f,
				-1.0f,-1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,

				1.0f, 1.0f, 0.0f,
				1.0f,-1.0f, 0.0f,
				-1.0f,-1.0f, 0.0f
			})));

			pVAO.addAttribute(pPositionVBO, 0, 3, GL30.GL_FLOAT, 0, 0);

			//Gum::_delete(pPositionVBO);
		}

		resize();
		reposition();
	}

    private void renderInternal()
    {
        vec4 col1 = getColor(GUI.getTheme().primaryColor);
        vec4 col2 = color2;
        vec4 col3 = bordercolor;
        if(fAlphaOverride < 1.0f)
        {
            col1.w = fAlphaOverride;
            col2.w = fAlphaOverride;
            col3.w = fAlphaOverride;
        }

        GUIShader.getShaderProgram().loadUniform("hasTexture", pTexture != null);
        if(pTexture != null)
        {
            //GUIShader.getShaderProgram().LoadUniform("isTextureGrayscale", pTexture.isGrayscale());
            pTexture.bind(0);
        }

        GUIShader.getShaderProgram().loadUniform("circleMode", bIsCircle);
        GUIShader.getShaderProgram().loadUniform("rightgradient", bRightgradient);
        GUIShader.getShaderProgram().loadUniform("gradient", bGradient);
        GUIShader.getShaderProgram().loadUniform("Uppercolor", col1);
        GUIShader.getShaderProgram().loadUniform("Lowercolor", col2);
        GUIShader.getShaderProgram().loadUniform("borderColor", col3);
        GUIShader.getShaderProgram().loadUniform("borderThickness", iBorderThickness);
        GUIShader.getShaderProgram().loadUniform("invertY", bInvertY);
        GUIShader.getShaderProgram().loadUniform("transmat", mTransformationMatrix);
        GUIShader.getShaderProgram().loadUniform("resolution", vActualSize);
        GUIShader.getShaderProgram().loadUniform("radius", v4CornerRadius);
        GUIShader.getShaderProgram().loadUniform("orthomat", Window.CurrentlyBoundWindow.getScreenMatrix());

        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        pVAO.bind();
        GL30.glDrawArrays(GL30.GL_TRIANGLE_STRIP, 0, 6);
        pVAO.unbind();
    }


	public void render()
	{
		if(!bIsHidden)
		{
            GUIShader.getShaderProgram().use();
            renderInternal();
            GUIShader.getShaderProgram().unuse();

            renderchildren();
		}
	}

	public void renderCustom()
	{
		pVAO.bind();
		GL30.glDrawArrays(GL30.GL_TRIANGLE_STRIP, 0, 6);
		pVAO.unbind();
	}


	public void invertTexcoordY(boolean invert)        { this.bInvertY = invert; }
    public void renderAsCircle(boolean iscircle)       { this.bIsCircle = iscircle; }
	public void setTexture(Texture texture)            { this.pTexture = texture; }
	public void setSecondColor(vec4 col)               { this.color2 = col; }
	public void setBorderColor(vec4 col)               { this.bordercolor = col; }
	public void setBorderThickness(int thickness)      { this.iBorderThickness = thickness; }
	public void setHasGradient(boolean val)            { this.bGradient = val; }
	public void setGradientDirectionRight(boolean val) { this.bRightgradient = val; }

	public Texture getTexture()                        { return this.pTexture; }
	public vec4 getSecondColor()                       { return this.color2; }
	public int getBorderThickness()
    { 
        if(iBorderThickness == -1)
            return GUI.getTheme().borderThickness;
        return iBorderThickness; 
    }


	public static Box createFromXMLNode(XMLNode node)
	{
		boolean invert = node.hasAttribute("invert");
		String texture = node.getAttribute("texture");
		Box boxgui = new Box(new ivec2(0,0), new ivec2(100,100));
		boxgui.invertTexcoordY(invert);
		if(!texture.equals(""))
        {
            Texture tex = new Texture();
            tex.load(texture, Box.class);
		    boxgui.setTexture(tex);
        }
		return boxgui;
	}

	static void cleanup()
	{
		//Gum::_delete(pVAO);
	}
};