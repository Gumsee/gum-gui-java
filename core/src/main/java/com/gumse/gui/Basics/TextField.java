package com.gumse.gui.Basics;

import org.lwjgl.opengl.GL11;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.gui.Locale;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Keyboard;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Output;

public class TextField extends RenderGUI
{
    private static TextField pCurrentActiveTextField;
	private TextBox pBackgroundBox;
	private Box pIndicatorBox;
	private Box pSelectionBox;
	private int iIndicatorCharIndex;
	private int iSelectorStartCharIndex, iSelectorEndCharIndex;
	private boolean bIsEditing;
	private boolean bActivateOnDoubleclick;
	//private Clock pClock;
	private int iTextScrollRightOffset;
	
	private String sOrigString;
    private String sHint;
    private String sCurrentText;
	public interface TextFieldInputCallback {
		void enter(String complete);
		void input(String input, String complete);
	}
	private TextFieldInputCallback pCallback;


	public TextField(String str, Font font, ivec2 pos, ivec2 size)
	{
		this.vPos.set(pos);
		this.vSize.set(size);;
		this.sType = "TextField";
        this.sHint = "";
        this.sCurrentText = "";
		this.bIsEditing = false;
		this.bActivateOnDoubleclick = false;
		this.iIndicatorCharIndex = 0;
		this.pCallback = null;
        this.sCurrentText = str;
	
		iTextScrollRightOffset = 30;
	
		pBackgroundBox = new TextBox(str, font, new ivec2(0, 0), new ivec2(100, 100));
		pBackgroundBox.setSizeInPercent(true, true);
		pBackgroundBox.setAlignment(TextBox.Alignment.LEFT);
		pBackgroundBox.setTextSize(30);
        pBackgroundBox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
        pBackgroundBox.getBox().setCornerRadius(GUI.getTheme().cornerRadius);
		//pBackgroundBox.setTextOffset(new ivec2(3, 5));
		addElement(pBackgroundBox);
	
		pIndicatorBox = new Box(new ivec2(0,0), new ivec2(2, 100));
		pIndicatorBox.setSizeInPercent(false, true);
		pIndicatorBox.setColor(GUI.getTheme().textColor);
		pIndicatorBox.hide(true);
		addElement(pIndicatorBox);
	
		pSelectionBox = new Box(new ivec2(0, 0), new ivec2(0, 100));
		pSelectionBox.setSizeInPercent(false, true);
		pSelectionBox.setColor(new vec4(0.34f, 0.5f, 0.76f, 0.3f));
		addElement(pSelectionBox);
	
		/*pClock = new Clock();
		pClock.runEveryNMilliSeconds([this]() {
			if(bIsEditing && iSelectorStartCharIndex == iSelectorEndCharIndex)
				pIndicatorBox.hide(!pIndicatorBox.isHidden());
		}, 1000);*/

        onClick((RenderGUI gui) -> {
            if(!bIsEditing && !bActivateOnDoubleclick)
                startEdit();
        });

        onDoubleClick((RenderGUI gui) -> {
            if(bIsEditing)
                selectAll();
            else if(!bIsEditing || bActivateOnDoubleclick)
                startEdit();
        });

        onHover(null, Mouse.GUM_CURSOR_IBEAM);
	
		resize();
		reposition();
		
		pBackgroundBox.setTextSize((int)(getSize().y * 0.9f));
		//pBackgroundBox.setTextOffset(new ivec2(0, 7));
	}
	
	
	public void cleanup()
	{
		/*Gum::_delete(pBackgroundBox);
		Gum::_delete(pSelectionBox);
		Gum::_delete(pIndicatorBox);
		Gum::_delete(pClock);*/
	}
	
    @Override
	protected void updateOnColorChange()
	{
		pBackgroundBox.setColor(v4Color);
	}
	
    @Override
	protected void updateOnPosChange()
	{
		updateText();
	}
	
    @Override
	protected void updateOnSizeChange()
	{
		pBackgroundBox.setTextSize((int)(getSize().y * 0.9f));
		updateText();
	}

    @Override
    protected void updateOnThemeChange() 
    {
        if(sCurrentText.equals(""))
            pBackgroundBox.setTextColor(GUI.getTheme().secondaryColor);
        else
            pBackgroundBox.setTextColor(GUI.getTheme().textColor);
            
        pBackgroundBox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
        pBackgroundBox.getBox().setCornerRadius(getCornerRadius());
		pIndicatorBox.setColor(GUI.getTheme().textColor);
		
        if(!sLocaleID.isEmpty())
			setHint(Locale.getCurrentLocale().getString(sLocaleID));
    }
	
	
	public void renderextra()
	{
		pBackgroundBox.render();
		if(iSelectorStartCharIndex != iSelectorEndCharIndex)
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
    		GL11.glScissor(vActualPos.x, Framebuffer.CurrentlyBoundFramebuffer.getSize().y - vActualPos.y - vActualSize.y, vActualSize.x, vActualSize.y);
			pSelectionBox.render();
			Window.CurrentlyBoundWindow.resetViewport();
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		pIndicatorBox.render();
	}
	
	public void updateextra()
	{
		if(RenderGUI.somethingHasBeenClicked())
			return;
	
		Mouse mouse = Window.CurrentlyBoundWindow.getMouse();
		Keyboard keyboard = Window.CurrentlyBoundWindow.getKeyboard();
        if(bIsEditing)
        {
            if(mouse.hasLeftClick())
            {
                int point = pBackgroundBox.getText().getClosestCharacterIndex(mouse.getPosition());
                if(!isMouseInside())
                {                    
                    int dir = 0;
                    if(mouse.getPosition().x < pBackgroundBox.getPosition().x)
                        dir = -1;
                    else if(mouse.getPosition().x > pBackgroundBox.getPosition().x + pBackgroundBox.getSize().x)
                        dir = 1;
                    
                    setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex + dir);
                }
                else if(mouse.hasLeftClickStart())
                {
                    setSelection(point, point);
                }
                else if(mouse.hasLeftClick())
                {
                    setSelection(iSelectorStartCharIndex, point);
                }
            }
        
            if(mouse.hasLeftClickStart() && !isMouseInside())
            {
                finishEditing();
            }
            
			//pClock.update();
			if     (keyboard.checkLastPressedKey(Keyboard.GUM_KEY_BACKSPACE))            { int spaces = Math.abs(iSelectorEndCharIndex - iSelectorStartCharIndex); backspaceString(spaces > 0 ? spaces : 1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_DELETE))               { int spaces = Math.abs(iSelectorEndCharIndex - iSelectorStartCharIndex); backspaceString(spaces > 0 ? spaces : -1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_ENTER)) 	             { finishEditing(); if(pCallback != null) pCallback.enter(sCurrentText); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_ESCAPE)) 	             { finishEditing(); setString(sOrigString); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_LEFT)) 	             { moveIndicator(-1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_RIGHT)) 	             { moveIndicator(1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_END)) 	             { setIndicator(sCurrentText.length()); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_HOME)) 	             { setIndicator(0); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_LEFT, Keyboard.GUM_MOD_SHIFT))  { setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex - 1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_RIGHT, Keyboard.GUM_MOD_SHIFT)) { setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex + 1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_END, Keyboard.GUM_MOD_SHIFT))   { setSelection(iSelectorStartCharIndex, sCurrentText.length()); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_HOME, Keyboard.GUM_MOD_SHIFT))  { setSelection(iSelectorStartCharIndex, 0); }
			else if(keyboard.getTextInput() != "") 				   		                 { appendString(keyboard.getTextInput()); }
		}
	}

    private void startEdit()
    {
        if(pCurrentActiveTextField != null)
            pCurrentActiveTextField.finishEditing();
        sOrigString = sCurrentText;
        bIsEditing = true;
        selectAll();

        pCurrentActiveTextField = this;
    }
	
	private void updateText()
	{
        if(sCurrentText.equals(""))
        {
		    pBackgroundBox.getText().setString(sHint);
            pBackgroundBox.setTextColor(GUI.getTheme().secondaryColor);
        }
        else
        {
		    pBackgroundBox.getText().setString(sCurrentText);
            pBackgroundBox.setTextColor(GUI.getTheme().textColor);
        }
		pBackgroundBox.updateText();
		//pBackgroundBox.setTextSize(getSize().y * 0.9f);
		pBackgroundBox.setTextOffset(new ivec2(0, pBackgroundBox.getTextOffset().y));
	}
	
	public void moveIndicator(int direction)
	{
		setIndicator(iIndicatorCharIndex + direction);
	}
	
	public void setIndicator(int pos)
	{
		iIndicatorCharIndex = pos;
		iIndicatorCharIndex = GumMath.clamp(iIndicatorCharIndex, 0, (int)sCurrentText.length());
	
		//Selector reset
		pSelectionBox.hide(true);
		iSelectorStartCharIndex = iIndicatorCharIndex;
		iSelectorEndCharIndex = iIndicatorCharIndex;
	
		//Indicator part
		int boxpos = pBackgroundBox.getText().getTextSize(sCurrentText, 0, iIndicatorCharIndex).x;
		pIndicatorBox.setPosition(new ivec2(boxpos - pBackgroundBox.getText().getOrigin().x, 0)); 
		pIndicatorBox.hide(false);
	
		int rightoffset = iTextScrollRightOffset;
		if(rightoffset > pBackgroundBox.getSize().x * 0.25f)
			rightoffset = (int)(pBackgroundBox.getSize().x * 0.25f);
	
		int distance = pBackgroundBox.getSize().x - boxpos + pBackgroundBox.getText().getOrigin().x;
		if(distance < rightoffset)
		{
			pBackgroundBox.setTextOffset(ivec2.sub(pBackgroundBox.getTextOffset(), new ivec2(distance - rightoffset, 0)));
			pIndicatorBox.setPosition(new ivec2(pBackgroundBox.getSize().x - rightoffset, 0));
		}
		if(distance > pBackgroundBox.getSize().x)
		{
			pBackgroundBox.setTextOffset(ivec2.sub(pBackgroundBox.getTextOffset(), new ivec2(distance - pBackgroundBox.getSize().x, 0)));
			pIndicatorBox.setPosition(new ivec2(0, 0));
		}
	}
	
	public void appendString(String utf8)
	{
		iIndicatorCharIndex = GumMath.clamp(iIndicatorCharIndex, 0, (int)sCurrentText.length());
		backspaceString(Math.abs(iSelectorEndCharIndex - iSelectorStartCharIndex));
		
		StringBuilder sb = new StringBuilder(sCurrentText);
		sb.insert(iIndicatorCharIndex, utf8);
        sCurrentText = sb.toString();
		updateText();
		setIndicator(iIndicatorCharIndex + 1);

        if(pCallback != null)
            pCallback.input(utf8, sCurrentText);
	}
	
	public void backspaceString(int backspaces)
	{
		int startingpoint = iSelectorEndCharIndex;
		if(startingpoint < iSelectorStartCharIndex)
			startingpoint = iSelectorStartCharIndex;
	
		if(backspaces < 0)
		{
			backspaces = -backspaces;
			startingpoint += backspaces;
		}
	
		if(backspaces > (int)startingpoint)
			return;
	
		StringBuilder sb = new StringBuilder(sCurrentText);
		sb.delete(GumMath.clamp(startingpoint - backspaces, 0, sCurrentText.length()), GumMath.clamp(startingpoint, 0, sCurrentText.length()));
		sCurrentText = sb.toString();
		updateText();
		setIndicator(startingpoint - backspaces);
        
        if(pCallback != null)
            pCallback.input("", sCurrentText);
	}
	
	public void setSelection(int from, int to)
	{
		setIndicator(to);
		iSelectorStartCharIndex = from;
		iSelectorEndCharIndex = to;
		if(to < from)
		{
			int tmp = from;
			from = to;
			to = tmp;
		}
	
		iSelectorStartCharIndex = GumMath.clamp(iSelectorStartCharIndex, 0, (int)sCurrentText.length());
		iSelectorEndCharIndex = GumMath.clamp(iSelectorEndCharIndex, 0, (int)sCurrentText.length());
		from = GumMath.clamp(from, 0, (int)sCurrentText.length());
		to = GumMath.clamp(to, 0, (int)sCurrentText.length());
		pSelectionBox.hide(false);
		pSelectionBox.setPosition(new ivec2(pBackgroundBox.getText().getTextSize(sCurrentText, 0, from).x - pBackgroundBox.getTextOffset().x, 0));
		pSelectionBox.setSize(new ivec2(pBackgroundBox.getText().getTextSize(sCurrentText, from, to).x, 100));
	}
	
	public void selectAll()
	{
		setSelection(0, sCurrentText.length());
	}
	
	public void finishEditing()
	{
		bIsEditing = false;
		pIndicatorBox.hide(true);
		pSelectionBox.hide(true);
	}
	
	
	//
	// Setter
	//
	public void setString(String utf8)
	{
		sCurrentText = utf8;
		updateText();
        setIndicator(utf8.length());
        pIndicatorBox.hide(true);
        //finishEditing();
	}
	
	public void shouldActivateOnDoubleclick(boolean activate)  		   { this.bActivateOnDoubleclick = activate; }
	public void setSelectionColor(vec4 color) 						   { this.pSelectionBox.setColor(color); }
	public void setIndicatorColor(vec4 color) 						   { this.pIndicatorBox.setColor(color); }
	public void setTextColor(vec4 color) 	  						   { this.pBackgroundBox.setTextColor(color); }
	public void setCallback(TextFieldInputCallback func)               { this.pCallback = func; }
	public void setCornerRadius(vec4 radius)						   { this.v4CornerRadius = radius; this.pBackgroundBox.setCornerRadius(radius); }
    public void setHint(String hint)                                   { this.sHint = hint; updateText(); }	
	
	//
	// Getter
	//
	public TextBox getBox()    { return this.pBackgroundBox; }
	public boolean isEditing() { return this.bIsEditing; }
    public String getString()  { return this.sCurrentText; }


    public static XMLGUICreator createFromXMLNode() 
    {
        return (XMLNode node) -> { 
			String fontName = node.getAttribute("font");
			Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());
	
			String hint = node.getAttribute("hint");
			int fontsize  = node.getIntAttribute("fontsize", 0);
			//int maxlength = node.getIntAttribute("maxlength", 0);
			
			TextField textfieldgui = new TextField(node.content, font, new ivec2(0,0), new ivec2(0,0));
			textfieldgui.setHint(hint);
			if(fontsize > 0)
				textfieldgui.getBox().setTextSize(fontsize);
					
			return textfieldgui;
        };
    };
};