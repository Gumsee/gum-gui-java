package com.gumse.gui.Primitives;

import java.util.ArrayList;
import java.util.Arrays;
import org.lwjgl.opengl.GL30;

import com.gumse.gui.GUIShader;
import com.gumse.maths.*;
import com.gumse.textures.Texture;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;
import com.gumse.system.Window;

public class Box extends RenderGUI
{
	private static VertexArrayObject pVAO = null;
	private Texture pTexture;
	private vec4 color2;
	private vec4 bordercolor;

	private boolean bGradient;
	private boolean bRightgradient;

	private boolean bInvertY;
	private float fBorderThickness;


	public Box(ivec2 pos, ivec2 size) 
	{
		this.pTexture = null;
		this.sType = "box";
		this.vPos = pos;
		this.vSize = size;
		this.fBorderThickness = 0.0f;
		this.bGradient = false;
		this.bRightgradient = false;
		this.bInvertY = false;
		this.color2 = new vec4();
		this.bordercolor = new vec4();
		
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


	public void render()
	{
		if(!bIsHidden)
		{
			GUIShader.getShaderProgram().use();

			float alpha1 = v4Color.w;
			float alpha2 = color2.w;
			float alpha3 = bordercolor.w;
			if(fAlphaOverride < 1.0f)
			{
				alpha1 = fAlphaOverride;
				alpha2 = fAlphaOverride;
				alpha3 = fAlphaOverride;
			}

			GUIShader.getShaderProgram().LoadUniform("hasTexture", pTexture != null);
			if(pTexture != null)
			{
				//GUIShader.getShaderProgram().LoadUniform("isTextureGrayscale", pTexture.isGrayscale());
				pTexture.bind(0);
			}

			GUIShader.getShaderProgram().LoadUniform("rightgradient", bRightgradient);
			GUIShader.getShaderProgram().LoadUniform("gradient", bGradient);
			GUIShader.getShaderProgram().LoadUniform("Uppercolor", new vec4(v4Color.x, v4Color.y, v4Color.z, alpha1));
			GUIShader.getShaderProgram().LoadUniform("Lowercolor", new vec4(color2.x, color2.y, color2.z, alpha2));
			GUIShader.getShaderProgram().LoadUniform("borderColor", new vec4(bordercolor.x, bordercolor.y, bordercolor.z, alpha3));
			GUIShader.getShaderProgram().LoadUniform("borderThickness", fBorderThickness);
			GUIShader.getShaderProgram().LoadUniform("invertY", bInvertY);
			GUIShader.getShaderProgram().LoadUniform("transmat", mTransformationMatrix);
			GUIShader.getShaderProgram().LoadUniform("resolution", vActualSize);
			GUIShader.getShaderProgram().LoadUniform("radius", v4CornerRadius);
			GUIShader.getShaderProgram().LoadUniform("orthomat", Window.CurrentlyBoundWindow.getScreenMatrix());

			//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

			pVAO.bind();
			GL30.glDrawArrays(GL30.GL_TRIANGLE_STRIP, 0, 6);
			pVAO.unbind();

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


	public void invertTexcoordY(boolean invert)          { this.bInvertY = invert; }
	public void setTexture(Texture texture)          { this.pTexture = texture; }
	public void setSecondColor(vec4 col)              { this.color2 = col; }
	public void setBorderColor(vec4 col)              { this.bordercolor = col; }
	public void setBorderThickness(float thickness)   { this.fBorderThickness = thickness; }
	public void setHasGradient(boolean val)              { this.bGradient = val; }
	public void setGradientDirectionRight(boolean val)   { this.bRightgradient = val; }

	public Texture getTexture()                      { return this.pTexture; }
	public vec4 getSecondColor()                      { return this.color2; }


	/*public static Box createFromXMLNode(XMLNode node)
	{
		boolean invert = node.mAttributes["invert"] == "true";
		String texture = node.mAttributes["texture"];
		Box boxgui = new Box(ivec2(0,0), ivec2(100,100));
		boxgui.invertTexcoordY(invert);
		//if(texture != "")
		//    boxgui.setTexture(GumEngine::Textures.getTexture(texture));
		return boxgui;
	}*/

	static void cleanup()
	{
		//Gum::_delete(pVAO);
	}
};