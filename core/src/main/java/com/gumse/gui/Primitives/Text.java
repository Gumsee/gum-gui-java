package com.gumse.gui.Primitives;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.gumse.gui.GUIShader;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Font.Character;
import com.gumse.maths.*;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.tools.Debug;

public class Text extends RenderGUI
{
    public class TextChar
    {
        public int index;
        public vec2 pos, scale;
        public int textureID;

        public TextChar(int index, vec2 pos, vec2 scale, int texID)
        {
            this.index = index;
            this.pos = pos;
            this.scale = scale;
            this.textureID = texID;
        }
    };

    Font pFont;

    String sText;
    String sRenderText;

    VertexArrayObject pVAO;
    VertexBufferObject pVBO;
    ArrayList<TextChar> vChars;
    bbox2i bRenderBox;

    int uiPointSize;
    int iMaxLength;

    float fFontWidth;
    float fFontHeight;
    float fPixelSize;
    float fScale;

    boolean bFadeOut, bFadeBothSides, bLeftFade, bRightFade;

        
    public Text(String text, Font font, ivec2 position, int maxlength)
    {
        this.vPos.set(position);
        //super.v4Color = new vec4(0.19f, 0.2f, 0.42f, 1.0f);
        this.v4Color = new vec4(0.9f, 0.9f, 0.9f, 1.0f);
        //super.sType = "Text";
        this.sType = "Text";

        this.pFont = font;
        this.sText = text;
        this.iMaxLength = maxlength;
        this.uiPointSize = 0;
        this.fScale = 1.0f;
        this.bRenderBox = new bbox2i(new ivec2(0,0), new ivec2(Integer.MAX_VALUE));
        this.vChars = new ArrayList<>();
        this.bFadeOut = false;
        this.bFadeBothSides = false;
        this.bLeftFade = false;
        this.bRightFade = false;

        ArrayList<Float> vertices = new ArrayList<>(Arrays.asList(new Float[] {
            1.0f, 0.0f, 1.0f, 1.0f,  
            0.0f, 1.0f, 0.0f, 0.0f,          
            0.0f, 0.0f, 0.0f, 1.0f,

            0.0f, 1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 0.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
        }));

        pVAO = new VertexArrayObject();
        pVBO = new VertexBufferObject();
        pVBO.setData(vertices);
        pVAO.addAttribute(pVBO, 0, 4, GL11.GL_FLOAT, 0, 0);
        setString(text);

        updateVAO();
    }

    public void cleanup() 
    { 
        //Gum::_delete(pVAO);
        //Gum::_delete(pVBO);
    }


    void updateVAO()
    {
        vChars.clear();

        float x = vActualPos.x;
        float y = vActualPos.y;
        y = Window.CurrentlyBoundWindow.getSize().y - vActualPos.y;
        //y -= this.pFont.getHighestGlyphSize() * fScale;

        // Iterate through all characters
        int line = 0;
        bFadeOut = false;
        bFadeBothSides = false;

        for(int i = 0; i < sRenderText.length(); i++)
        {
            if(sRenderText.charAt(i) == '\n')
            {
                line++;
                x = vActualPos.x;
            }
            else
            {
                Character ch = pFont.getCharacter(sRenderText.codePointAt(i));
                if(ch == null)
                {
                    Debug.warn("Character '" + sRenderText.substring(i, i + 1) + "' ("+sRenderText.codePointAt(i)+") not available in font " + pFont.getName() + "!");
                    continue;
                }

                if(ch.texture == null) continue; //It doesnt exist on purpose

                ivec2 charsize = ch.texture.getSize();
                vec2 scale = new vec2(charsize.x * fScale, charsize.y * fScale);

                float charsizey = (charsize.y - ch.Bearing.y) * fScale;
                vec2 pos = new vec2(x + ch.Bearing.x * fScale,
                                    y - charsizey - this.pFont.getHighestGlyphSize() * fScale * line * 1.1f);
                
                x += (ch.Bearing.x + charsize.x + ch.Advance) * fScale;

                if(!bRightFade && pos.x + scale.x > bRenderBox.pos.x + bRenderBox.size.x)
                {
                    bFadeOut = true;
                }
                if(!bLeftFade && pos.x < bRenderBox.pos.x)
                {
                    bFadeOut = true;
                    bFadeBothSides = true;
                }
                float ycheckpos = Window.CurrentlyBoundWindow.getSize().y - pos.y;
                if(pos.x > bRenderBox.pos.x + bRenderBox.size.x || pos.x + scale.x < bRenderBox.pos.x ||
                   ycheckpos > bRenderBox.pos.y + bRenderBox.size.y || ycheckpos - scale.y < bRenderBox.pos.y)
                    continue;
                
                vChars.add(new TextChar(i, pos, scale, ch.texture.getID()));
            }
        }
    }

    protected void updateOnPosChange()
    {
        updateVAO();
    }

    protected void updateOnSizeChange()
    {
        updateVAO();
    }


    public void render()
    {
        if(bIsHidden)
            return;
        
        GUIShader.getTextShaderProgram().use();
        GUIShader.getTextShaderProgram().loadUniform("color", v4Color);
        GUIShader.getTextShaderProgram().loadUniform("projection", Window.CurrentlyBoundWindow.getScreenMatrix());
        GUIShader.getTextShaderProgram().loadUniform("bboxpos", new vec2(bRenderBox.getPos()));
        GUIShader.getTextShaderProgram().loadUniform("bboxsize", new vec2(bRenderBox.getSize()));
        GUIShader.getTextShaderProgram().loadUniform("fade", bFadeOut);
        GUIShader.getTextShaderProgram().loadUniform("fadestart", bFadeBothSides);

        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        pVAO.bind();

        GL11.glEnable(GL11.GL_BLEND);
        for (TextChar ch : vChars)
        {
            GUIShader.getTextShaderProgram().loadUniform("position", ch.pos);
            GUIShader.getTextShaderProgram().loadUniform("scale", ch.scale);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ch.textureID);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 6);
        }
        pVAO.unbind();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        GUIShader.getTextShaderProgram().unuse();
    }

    public void applyStringChanges()
    {
        this.sRenderText = sText;
        if(iMaxLength > 0)
        {
            if(sText.length() > iMaxLength)
            {
                sRenderText = sText.substring(0, iMaxLength) + "...";
            }
        }
        updateVAO();
    }


    //
    // Setter
    //
    public void setString(String str)
    {
        this.sText = str;
        applyStringChanges();
    }

    public void setScale(float scale)                        { this.fScale = scale; }
    public void setCharacterHeight(float height)             { this.fScale = height / this.pFont.getHighestGlyphSize();  updateVAO(); }
    public void setMaxLength(int length)                     { this.iMaxLength = length; }
    public void setRenderBox(bbox2i box)                     { this.bRenderBox = box; }
    public void setFadeOverride(boolean left, boolean right) { this.bLeftFade = left; this.bRightFade = right; }


    //
    // Getter
    //
    public ivec2 getStringSize(String str)        { return getTextSize(str, 0, str.length()); }
    public ivec2 getSize()                        { return getStringSize(sRenderText); }
    public ivec2 getFullTextSize()                { return getStringSize(sText); }
    public String getString()                     { return this.sText; }
    public bbox2i getRenderBox()                  { return this.bRenderBox; }
    public ivec2 getTextSize(String str, int begin, int end)
    {
        if(begin < 0 || end > str.length())
            return new ivec2(0, 0);
        
        vec2 vTextSize = new vec2(0,0);
        vTextSize.y = this.pFont.getHighestGlyphSize() * fScale * 1.1f;
        float biggestX = 0;
        for(int i = begin; i < end; i++)
        {
            Character ch = pFont.getCharacter(str.codePointAt(i));
            if(ch.texture == null) continue;
            vTextSize.x += ch.texture.getSize().x * fScale;

            if(str.charAt(i) == '\n') 
            {
                vTextSize.x = 0;
                vTextSize.y += this.pFont.getHighestGlyphSize() * fScale * 1.1f;
            }

            if(vTextSize.x > biggestX) 
                biggestX = vTextSize.x;
        }
        vTextSize.x = biggestX;

        return new ivec2((int)vTextSize.x, (int)vTextSize.y);
    }

    public int getClosestCharacterIndex(ivec2 point)
    {
        if(vChars.size() == 0)
            return 0;
        
        int closest = 0;
        int closestDist = Integer.MAX_VALUE;
        for(int i = 0; i < vChars.size(); i++)
        {
            int dist = (int)Math.abs(vChars.get(i).pos.x - point.x);
            if(dist < closestDist)
            {
                closestDist = dist;
                closest = vChars.get(i).index;
            }
        }
        if(vChars.get(vChars.size() - 1).pos.x + vChars.get(vChars.size() - 1).scale.x - point.x < closestDist)
            closest++;

        return closest;
    }

	public static Text createFromXMLNode(XMLNode node)
	{
        String fontName = node.getAttribute("font");
        Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());

        int fontsize  = node.getIntAttribute("fontsize", 12);
        int maxlength = node.getIntAttribute("maxlength", 0);
        
		Text textgui = new Text(node.content, font, new ivec2(0,0), maxlength);
        textgui.setCharacterHeight(fontsize);
		return textgui;
	}
};