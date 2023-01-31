package com.gumse.gui.Basics;

import com.gumse.gui.Locale;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
import com.gumse.maths.*;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.textures.Texture;
import com.gumse.tools.Output;

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
    private int iNewlineInsertOffset;


	public TextBox(String str, Font font, ivec2 pos, ivec2 size)
	{
		this.sType = "TextBox";
		this.vPos.set(pos);
		this.vSize.set(size);
		this.sTitle = str;
		this.iAlignment = Alignment.CENTER;
		this.v2TextOffset = new ivec2(0,0);
        this.bAutoInsertLinebreaks = false;
        this.iNewlineInsertOffset = 0;

		pBackgroundBox = new Box(new ivec2(0,0), new ivec2(100,100));
		pBackgroundBox.setSizeInPercent(true, true);
		addElement(pBackgroundBox);

		pText = new Text(str, font, new ivec2(50, 50), 0);
		pText.setPositionInPercent(true, true);
		pText.setOrigin(ivec2.div(pText.getSize(), 2.0f));
		pText.setCharacterHeight((int)(size.y * 0.9));
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

	@Override
	protected void updateOnColorChange()
	{
		this.pBackgroundBox.setColor(v4Color);
	}

	@Override
	protected void updateOnThemeChange() {
        if(!sLocaleID.isEmpty())
			setString(Locale.getCurrentLocale().getString(sLocaleID));
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
        int maxwidth = (int)(vActualSize.x * 0.85);
        int halfwidth = maxwidth / 2;
        int spacewidth = pText.getStringSize(" ").x * 3;
        String retString = "";
        this.iNewlineInsertOffset = 0;

        String[] words = sActualText.split(" ");
        for(int i = 0; i < words.length; i++)
        {
            String word = words[i];
            int wordwidth = pText.getStringSize(word).x + spacewidth;
            iNewlineInsertOffset += wordwidth;

            //Output.info(word + " " + wordwidth + " " + iNewlineInsertOffset + " " + maxwidth);
            if(wordwidth >= halfwidth)
            {
                retString += splitWord(word, maxwidth) + " ";
                continue;
            }
            else if(iNewlineInsertOffset >= maxwidth)
            {
                retString += "\n";
                iNewlineInsertOffset = 0;
            }

            retString += word + " ";
        }
        sFinalText = retString.toString();
    }

    private String splitWord(String word, int maxwidth)
    {
        StringBuilder retStr = new StringBuilder(word);
        //int offset = startoffset;
        for(int i = 0; i < word.length(); i++)
        {
            char currentChar = word.charAt(i);
            iNewlineInsertOffset += pText.getCharSize(currentChar).x;

            if(iNewlineInsertOffset >= maxwidth)
            {
                retStr.setCharAt(i, '\n');
                retStr.insert(i, currentChar);
                    
                iNewlineInsertOffset = 0;
            }
        }

        return retStr.toString();
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
	public Texture getTexture() 				  { return this.pBackgroundBox.getTexture(); }
	public Text getText()						  { return this.pText; }
	public ivec2 getTextOffset()				  { return this.v2TextOffset; }
	public Box getBox()							  { return this.pBackgroundBox; }
	public String getString()				      { return this.pText.getString(); }

    public static XMLGUICreator createFromXMLNode() 
    {
        return (XMLNode node) -> { 
			String fontName = node.getAttribute("font");
			Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());
	
			int fontsize  = node.getIntAttribute("fontsize", 0);
			int maxlength = node.getIntAttribute("maxlength", 0);
			
			TextBox textboxgui = new TextBox(node.content, font, new ivec2(0,0), new ivec2(0,0));
			if(fontsize > 0)
				textboxgui.setTextSize(fontsize);
			
			textboxgui.getText().setMaxLength(maxlength);
			return textboxgui;
        };
    };
};