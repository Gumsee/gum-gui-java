package com.gumse.gui.Basics;

import com.gumse.gui.GUI;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.GumMath;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Keyboard;
import com.gumse.system.io.Mouse;

public class Slider extends RenderGUI
{
	private Box pBackground;
	private Box pSliderRect;

	private Text pVariableText;
	private Text pNameText;

	private float fViewMultiplier = 1;
	private float fValue;
	private int iPrecision = 1;
    private boolean bSnapped;
	private boolean bInfinite;

	public interface SliderCallbackFunction {
		void run(float val);
	}

	private SliderCallbackFunction callbackFunction = null;
	private String sUnit;

	public Slider(ivec2 pos, int length, String name, int iPrecision)
	{
		this.vSize.set(new ivec2(length, 20));
		this.vPos.set(pos);
		this.sType = "Slider";
		this.iPrecision = iPrecision;
		this.sUnit = "";
		this.bSnapped = false;

		pBackground = new Box(new ivec2(0,0), new ivec2(100, 100));
		pBackground.setSizeInPercent(true, true);
		pBackground.setColor(GUI.getTheme().secondaryColor);
		pBackground.setCornerRadius(new vec4(12.0f));
		addElement(pBackground);

		pSliderRect = new Box(new ivec2(0,0), new ivec2(0, 100));
		pSliderRect.setSizeInPercent(true, true);
		pSliderRect.setColor(GUI.getTheme().accentColor);
		addElement(pSliderRect);

		this.value = "0";

		pVariableText = new Text("0", FontManager.getInstance().getDefaultFont(), new ivec2(0,0), 0);
		pVariableText.setCharacterHeight(15);
		pVariableText.setPosition(new ivec2(getSize().x - pVariableText.getSize().x - 10, 5));
		addElement(pVariableText);

		pNameText = new Text(name, FontManager.getInstance().getDefaultFont(), new ivec2(10, 5), 0);
		pNameText.setCharacterHeight(15.0f);
		addElement(pNameText);

		resize();
		reposition();
	}

	public void cleanup() 
	{
		/*Gum::_delete(pBackground);
		Gum::_delete(pSliderRect);
		Gum::_delete(value);
		Gum::_delete(pValuePtr);
		Gum::_delete(pVariableText);
		Gum::_delete(pNameText);*/
	}

	public void update()
	{
        Mouse mouse = Window.CurrentlyBoundWindow.getMouse();
        Keyboard keyboard = Window.CurrentlyBoundWindow.getKeyboard();
		if(pBackground.isMouseInside() && !Mouse.isBusy())
		{
			Mouse.setActiveHovering(true);
			mouse.setCursor(Mouse.GUM_CURSOR_HORIZONTAL_RESIZE);
			pBackground.setColor(vec4.sub(GUI.getTheme().secondaryColor, 0.02f));
			pSliderRect.setColor(vec4.sub(GUI.getTheme().accentColor, 0.02f));

			if(hasClickedInside())
			{
				bSnapped = true;
				Mouse.setBusiness(true);
			}
		}
		else 
		{
			pBackground.setColor(GUI.getTheme().secondaryColor);
			pSliderRect.setColor(GUI.getTheme().accentColor);
		}

		if(bSnapped)
		{
			pBackground.setColor(vec4.sub(GUI.getTheme().secondaryColor, 0.02f));
			pSliderRect.setColor(vec4.sub(GUI.getTheme().accentColor, 0.02f));
			float newvar = fValue;
			if(bInfinite)
			{
				float speed = 0.1f;
				if(keyboard.checkKeyPressed(Keyboard.GUM_KEY_LEFT_CONTROL))
					speed *= 10;
				else if(keyboard.checkKeyPressed(Keyboard.GUM_KEY_LEFT_SHIFT))
					speed *= 0.01;
					
                    mouse.lock(true);
				newvar += (mouse.getPositionDelta().x + mouse.getMouseWheelState()) * speed;
			}
			else 
			{
				newvar = (float)(mouse.getPosition().x - getPosition().x) / (float)getSize().x;
				newvar = GumMath.clamp(newvar, 0.0f, 1.0f);
			}
			if(newvar != fValue)
			{
				fValue = newvar;
				updateData();
				if(callbackFunction != null) { callbackFunction.run(fValue); }
			}
			if(!mouse.hasLeftClick())
			{
				bSnapped = false;
				mouse.lock(false);
				Mouse.setBusiness(false);
			}
		}
	}

	public void updateData()
	{
		value = Float.toString(fValue);

		pVariableText.setString(String.format("%."+iPrecision+"f", fValue * fViewMultiplier) + sUnit);
		pVariableText.setPosition(new ivec2(getSize().x - pVariableText.getSize().x - 10, 5));

		if(bInfinite)
		{
			pSliderRect.setSize(new ivec2(0,0));
		}
		else 
		{
			pSliderRect.setSize(new ivec2((int)(fValue * 100.0f), 100));
			if(pSliderRect.getSize().x > getSize().x - getSize().y * 0.5f)
			{
				pSliderRect.setCornerRadius(pBackground.getCornerRadius());
			}
			else
			{
				pSliderRect.setCornerRadius(new vec4(pBackground.getCornerRadius().x, 0.0f, 0.0f, pBackground.getCornerRadius().w));
			}
		}
	}

	protected void updateOnSizeChange()
	{
		pVariableText.setPosition(new ivec2(getSize().x - pVariableText.getSize().x - 10, 5));
	}

	protected void updateOnCornerRadiusChange()
	{
		pBackground.setCornerRadius(v4CornerRadius);
	}


	//
	// Getter
	//
	public float getData() 								         { return fValue; }
	public float getLengh() 							         { return this.vActualSize.x; }
	public String getUnit()								         { return sUnit; }


	//
	// Setter
	//
	public void setInfinite(boolean infinite)                    { this.bInfinite = infinite; }
	public void setUnit(String unit)							 { this.sUnit = unit; updateData(); }
	public void setData(float NewData) 							 { this.fValue = NewData; updateData(); }
	public void setLineColor(vec4 col)							 { this.pBackground.setColor(col); }
	public void setSliderColor(vec4 col) 						 { this.pSliderRect.setColor(col); }
	public void setLengh(int NewLengh) 							 { this.pBackground.setSize(new ivec2(NewLengh, pBackground.getSize().y)); }
	public void IncreaseLengh(int NewLengh) 					 { this.pBackground.setSize(new ivec2(pBackground.getSize().x + NewLengh, pBackground.getSize().y)); }
	public void setViewMultiplier(float multiplier) 			 { this.fViewMultiplier = multiplier; }
	public void setCallbackFunction(SliderCallbackFunction func) { this.callbackFunction = func; }



	public static Slider createFromXMLNode(XMLNode node)
	{
		int length             = node.getIntAttribute("length", 100);
		int precision          = node.getIntAttribute("precision", 1);
		float multiplier       = node.getFloatAttribute("multiplier", 1.0f);
		boolean infiniteslider = node.hasAttribute("infinite");
		String unit            = node.getAttribute("unit");
		String name            = node.getAttribute("title");

		Slider slidergui = new Slider(new ivec2(0,0), length, name, precision);
		slidergui.setSizeInPercent(node.getAttribute("length").contains("%"), false);
		slidergui.setViewMultiplier(multiplier);
		slidergui.setInfinite(infiniteslider);
		slidergui.setUnit(unit);
		return slidergui;
	}
};