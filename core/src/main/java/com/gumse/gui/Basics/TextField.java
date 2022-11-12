package com.gumse.gui.Basics;

import org.lwjgl.opengl.GL11;

import com.gumse.gui.Font.Font;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.io.Keyboard;
import com.gumse.system.io.Mouse;

public class TextField extends RenderGUI
{
	private TextBox pBackgroundBox;
	private Box pIndicatorBox;
	private Box pSelectionBox;
	private int iIndicatorCharIndex;
	private int iSelectorStartCharIndex, iSelectorEndCharIndex;
	private boolean bIsEditing;
	private boolean bActivateOnDoubleclick;
	//private Clock pClock;
	private int iTextScrollRightOffset;
	private int uiCursorShape;
	
	private String sOrigString;
	public interface TextFieldFinishedInputCallback {
		void run(String str);
	}
	private TextFieldFinishedInputCallback pReturnFunc;


	public TextField(String str, Font font, ivec2 pos, ivec2 size)
	{
		this.vPos.set(pos);
		this.vSize.set(size);;
		this.sType = "TextField";
		this.bIsEditing = false;
		this.bActivateOnDoubleclick = false;
		this.iIndicatorCharIndex = 0;
		this.pReturnFunc = null;
		this.uiCursorShape = Mouse.GUM_CURSOR_IBEAM;
	
		iTextScrollRightOffset = 30;
	
		pBackgroundBox = new TextBox(str, font, new ivec2(0, 0), new ivec2(100, 100));
		pBackgroundBox.setSizeInPercent(true, true);
		pBackgroundBox.setTextColor(new vec4(0.76f, 0.76f, 0.76f, 1.0f));
		pBackgroundBox.setAlignment(TextBox.Alignment.LEFT);
		pBackgroundBox.setTextSize(30);
		//pBackgroundBox.setTextOffset(new ivec2(3, 5));
		addElement(pBackgroundBox);
	
		pIndicatorBox = new Box(new ivec2(0,0), new ivec2(2, 100));
		pIndicatorBox.setSizeInPercent(false, true);
		pIndicatorBox.setColor(new vec4(0.76f, 0.76f, 0.76f, 1.0f));
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
	
	protected void updateOnColorChange()
	{
		pBackgroundBox.setColor(v4Color);
	}
	
	protected void updateOnPosChange()
	{
		updateText();
	}
	
	protected void updateOnSizeChange()
	{
		updateText();
	}
	
	
	public void render()
	{
		pBackgroundBox.render();
		if(iSelectorStartCharIndex != iSelectorEndCharIndex)
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
    		GL11.glScissor(vActualPos.x, Window.CurrentlyBoundWindow.getSize().y - vActualPos.y - vActualSize.y, vActualSize.x, vActualSize.y);
			pSelectionBox.render();
			Window.CurrentlyBoundWindow.resetViewport();
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		pIndicatorBox.render();
	}
	
	public void update()
	{
		if(RenderGUI.somethingHasBeenClicked())
			return;
	
		Mouse mouse = Window.CurrentlyBoundWindow.getMouse();
		Keyboard keyboard = Window.CurrentlyBoundWindow.getKeyboard();
		
		if(isMouseInside())
		{
			Mouse.setActiveHovering(true);
			mouse.setCursor(uiCursorShape);
			if(bIsEditing)
			{
				mouse.setCursor(uiCursorShape);
				if(mouse.hasLeftDoubleClick())
				{
					selectAll();
				}
				else if(mouse.hasLeftClickStart())
				{
					Mouse.setBusiness(true);
					int point = pBackgroundBox.getText().getClosestCharacterIndex(mouse.getPosition());
					setSelection(point, point);
				}
				else if(mouse.hasLeftClick())
				{
					int point = pBackgroundBox.getText().getClosestCharacterIndex(mouse.getPosition());
					setSelection(iSelectorStartCharIndex, point);
				}
				else if(mouse.hasLeftRelease())
				{
					Mouse.setBusiness(false);
				}
			}
			
			if(!Mouse.isBusy())
			{
				if((!bActivateOnDoubleclick && isClicked()) || 
				   ( bActivateOnDoubleclick && mouse.hasLeftDoubleClick()))
				{
					if(!bIsEditing)
					{
						bIsEditing = true;
						sOrigString = pBackgroundBox.getString();
						selectAll();
					}
				}
			}
		}
		else
		{
			if(bIsEditing && Mouse.isBusy() && mouse.hasLeftClick())
			{
				int dir = 0;
				if(mouse.getPosition().x < pBackgroundBox.getPosition().x)
					dir = -1;
				else if(mouse.getPosition().x > pBackgroundBox.getPosition().x + pBackgroundBox.getSize().x)
					dir = 1;
				
				setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex + dir);
			}
			else
			{
				if(mouse.hasLeftClick())
				{
					finishEditing();
				}
			}
		}
	
		if(bIsEditing)
		{
			//pClock.update();
			if     (keyboard.checkLastPressedKey(Keyboard.GUM_KEY_BACKSPACE))            { int spaces = Math.abs(iSelectorEndCharIndex - iSelectorStartCharIndex); backspaceString(spaces > 0 ? spaces : 1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_DELETE))               { int spaces = Math.abs(iSelectorEndCharIndex - iSelectorStartCharIndex); backspaceString(spaces > 0 ? spaces : -1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_ENTER)) 	             { finishEditing(); if(pReturnFunc != null) pReturnFunc.run(pBackgroundBox.getString()); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_ESCAPE)) 	             { finishEditing(); setString(sOrigString); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_LEFT)) 	             { moveIndicator(-1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_RIGHT)) 	             { moveIndicator(1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_END)) 	             { setIndicator(pBackgroundBox.getString().length()); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_HOME)) 	             { setIndicator(0); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_LEFT, Keyboard.GUM_MOD_SHIFT))  { setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex - 1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_RIGHT, Keyboard.GUM_MOD_SHIFT)) { setSelection(iSelectorStartCharIndex, iSelectorEndCharIndex + 1); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_END, Keyboard.GUM_MOD_SHIFT)) 	 { setSelection(iSelectorStartCharIndex, pBackgroundBox.getString().length()); }
			else if(keyboard.checkLastPressedKey(Keyboard.GUM_KEY_HOME, Keyboard.GUM_MOD_SHIFT))  { setSelection(iSelectorStartCharIndex, 0); }
			else if(keyboard.getTextInput() != "") 				   		     { appendString(keyboard.getTextInput()); }
		}
	
		updatechildren();
	}
	
	private void updateText()
	{
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
		iIndicatorCharIndex = GumMath.clamp(iIndicatorCharIndex, 0, (int)pBackgroundBox.getString().length());
	
		//Selector reset
		pSelectionBox.hide(true);
		iSelectorStartCharIndex = iIndicatorCharIndex;
		iSelectorEndCharIndex = iIndicatorCharIndex;
	
		//Indicator part
		int boxpos = pBackgroundBox.getText().getTextSize(pBackgroundBox.getString(), 0, iIndicatorCharIndex).x;
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
		iIndicatorCharIndex = GumMath.clamp(iIndicatorCharIndex, 0, (int)pBackgroundBox.getString().length());
		backspaceString(Math.abs(iSelectorEndCharIndex - iSelectorStartCharIndex));
		
		StringBuilder sb = new StringBuilder(pBackgroundBox.getText().getString());
		sb.insert(iIndicatorCharIndex, utf8);
		pBackgroundBox.getText().setString(sb.toString());
		updateText();
		setIndicator(iIndicatorCharIndex + 1);
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
	
		StringBuilder sb = new StringBuilder(pBackgroundBox.getText().getString());
		sb.delete(startingpoint - backspaces, startingpoint);
		pBackgroundBox.getText().setString(sb.toString());
		updateText();
		setIndicator(startingpoint - backspaces);
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
	
		iSelectorStartCharIndex = GumMath.clamp(iSelectorStartCharIndex, 0, (int)pBackgroundBox.getString().length());
		iSelectorEndCharIndex = GumMath.clamp(iSelectorEndCharIndex, 0, (int)pBackgroundBox.getString().length());
		from = GumMath.clamp(from, 0, (int)pBackgroundBox.getString().length());
		to = GumMath.clamp(to, 0, (int)pBackgroundBox.getString().length());
		pSelectionBox.hide(false);
		pSelectionBox.setPosition(new ivec2(pBackgroundBox.getText().getTextSize(pBackgroundBox.getString(), 0, from).x - pBackgroundBox.getTextOffset().x, 0));
		pSelectionBox.setSize(new ivec2(pBackgroundBox.getText().getTextSize(pBackgroundBox.getString(), from, to).x, 100));
	}
	
	public void selectAll()
	{
		setSelection(0, pBackgroundBox.getString().length());
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
		pBackgroundBox.setString(utf8);
		updateText();
	}
	
	public void setCursorShapeOnHover(int shape) 				   	   { this.uiCursorShape = shape; }
	public void shouldActivateOnDoubleclick(boolean activate)  		   { this.bActivateOnDoubleclick = activate; }
	public void setSelectionColor(vec4 color) 						   { this.pSelectionBox.setColor(color); }
	public void setIndicatorColor(vec4 color) 						   { this.pIndicatorBox.setColor(color); }
	public void setTextColor(vec4 color) 	  						   { this.pBackgroundBox.setTextColor(color); }
	public void setReturnCallback(TextFieldFinishedInputCallback func) { this.pReturnFunc = func; }
	public void setCornerRadius(vec4 radius)						   { this.pBackgroundBox.setCornerRadius(radius); }
	
	
	//
	// Getter
	//
	public TextBox getBox()    { return this.pBackgroundBox; }
	public boolean isEditing() { return this.bIsEditing; }
};