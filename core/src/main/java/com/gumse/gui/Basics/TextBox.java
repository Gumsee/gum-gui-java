package com.gumse.gui.Basics;

import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.*;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.textures.Texture;
import com.gumse.tools.Debug;

public class TextBox extends RenderGUI
{
	public enum Alignment {
		LEFT,
		CENTER,
		RIGHT,
	};

	
    private Box	pBackgroundBox;
	private Text pText;
    private String sActualText, sFinalText;
	
	private Alignment iAlignment;
	private ivec2 v2TextOffset;
    private boolean bAutoInsertLinebreaks;


	public TextBox(String str, Font font, ivec2 pos, ivec2 size)
	{
		this.sType = "TextBox";
		this.vPos.set(pos);
		this.vSize.set(size);
		this.sTitle = str;
		this.iAlignment = Alignment.CENTER;
		this.v2TextOffset = new ivec2(0,0);
        this.bAutoInsertLinebreaks = false;

		pBackgroundBox = new Box(new ivec2(0,0), new ivec2(100,100));
		pBackgroundBox.setSizeInPercent(true, true);
		pBackgroundBox.setColor(new vec4(0.3f, 0.3f, 0.3f, 1.0f));
		addElement(pBackgroundBox);

		pText = new Text(str, font, new ivec2(50, 50), 0);
		pText.setPositionInPercent(true, true);
		pText.setOrigin(ivec2.div(pText.getSize(), 2.0f));
		pText.setCharacterHeight((int)(size.y * 0.9));
		pText.setColor(new vec4(0.9f, 0.9f, 0.9f, 1.0f));
		addElement(pText);

        setString(str);

		resize();
		reposition();
	}

	public void cleanup() 
	{
		/*Gum::_delete(pBackgroundBox);
		Gum::_delete(pText);
		Gum::_delete(value);*/
	}

	public void update()
	{
		updatechildren();
	}

	public void updateText()
	{
		updateOnSizeChange();
	}


	protected void updateOnPosChange()
	{
		pText.setRenderBox(new bbox2i(vActualPos, vActualSize));
	}

	protected void updateOnSizeChange()
	{
        if(bAutoInsertLinebreaks)
        {
            insertLinebreaks();
            pText.setString(sFinalText);
        }

		ivec2 textSize = pText.getSize();
		switch(iAlignment)
		{
			case LEFT:
				pText.setOrigin(ivec2.add(new ivec2(0, (int)(textSize.y / 2.0f)), v2TextOffset));
				pText.setPosition(new ivec2(0,50));
				break;

			case CENTER:
				pText.setOrigin(ivec2.add(ivec2.div(textSize, 2.0f), v2TextOffset));
				pText.setPosition(new ivec2(50,50));
				break;

			case RIGHT:
				pText.setOrigin(ivec2.add(ivec2.mul(textSize, new vec2(1.0f, 0.5f)), v2TextOffset));
				pText.setPosition(new ivec2(100,50));
				break;
		};
		//pText.setOrigin(new ivec2(0, 0));
		//pText.getOrigin().print();
		pText.setRenderBox(new bbox2i(vActualPos, vActualSize));
		pText.reposition();
		//ivec2.sub(getPosition(), pText.getPosition()).print();
	}

	protected void updateOnColorChange()
	{
		this.pBackgroundBox.setColor(v4Color);
	}

	public void setString(String str)
	{
        sActualText = str;
        if(bAutoInsertLinebreaks)
            insertLinebreaks();
        else
            sFinalText = sActualText;

		pText.setString(sFinalText);
		updateText();
	}

    private void insertLinebreaks()
    {
        //ArrayList<String> words
        int wordStart = 0, wordEnd = 0;
        int currentOffset = 0;

        char[] chars = sActualText.toCharArray();
        for(int i = 0; i < sActualText.length(); i++)
        {
            if(sActualText.charAt(i) == ' ')
            {
                wordStart = wordEnd;
                wordEnd = i;

                int wordWidth = pText.getTextSize(sActualText, wordStart, wordEnd).x;
                currentOffset += wordWidth;

                if(currentOffset > vActualSize.x * 0.9f)
                {
                    chars[wordStart] = '\n';
                    currentOffset = wordWidth;
                }
            }
        }
        sFinalText = String.valueOf(chars);
    }

	public void setAlignment(Alignment alignment)    { this.iAlignment = alignment; updateOnSizeChange(); }
	public void setTextOffset(ivec2 offset)		     { this.v2TextOffset = offset; updateOnSizeChange(); }
	public void setTexture(Texture tex) 		     { this.pBackgroundBox.setTexture(tex); }
	public void setTextColor(vec4 color)      	     { this.pText.setColor(color); }
	public void setTextSize(int size)			     { this.pText.setCharacterHeight(size); }
	public void setMaxTextlength(int length)	     { this.pText.setMaxLength(length); }
	public void setCornerRadius(vec4 radius)	     { this.pBackgroundBox.setCornerRadius(radius); }
    public void setAutoInsertLinebreaks(boolean ins) { this.bAutoInsertLinebreaks = ins; pText.setFadeOverride(ins, ins); }

	public ivec2 getTextSize()					  { return this.pText.getSize(); }
	public boolean isMouseInside() 				  { return this.pBackgroundBox.isMouseInside(); }
	public Texture getTexture() 				  { return this.pBackgroundBox.getTexture(); }
	public Text getText()						  { return this.pText; }
	public ivec2 getTextOffset()				  { return this.v2TextOffset; }
	public Box getBox()							  { return this.pBackgroundBox; }
	public String getString()				      { return this.pText.getString(); }

	public static TextBox createFromXMLNode(XMLNode node)
	{
        String fontName = node.getAttribute("font");
        Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());

        int fontsize  = node.getIntAttribute("fontsize", 0);
        int maxlength = node.getIntAttribute("maxlength", 0);
        
		TextBox textboxgui = new TextBox(node.content, font, new ivec2(0,0), new ivec2(0,0));
        if(fontsize > 0)
            textboxgui.setTextSize(fontsize);
        
        textboxgui.getText().setMaxLength(maxlength);
		return textboxgui;
	}
};