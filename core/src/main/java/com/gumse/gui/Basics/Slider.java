package com.gumse.gui.Basics;

import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.GumMath;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.system.Window;
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
		pBackground.setColor(new vec4(0.7f, 0.7f, 0.7f, 1.0f));
		pBackground.setCornerRadius(new vec4(12.0f));
		addElement(pBackground);

		pSliderRect = new Box(new ivec2(0,0), new ivec2(0, 100));
		pSliderRect.setSizeInPercent(true, true);
		pSliderRect.setColor(new vec4(0.4f, 0.4f, 0.4f, 1));
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
		if(pBackground.isMouseInside() && !Mouse.isBusy())
		{
			Mouse.setActiveHovering(true);
			Window.CurrentlyBoundWindow.getMouse().setCursor(Mouse.GUM_CURSOR_HORIZONTAL_RESIZE);
			pBackground.setColor(new vec4(0.6f, 0.6f, 0.6f, 1.0f));
			pSliderRect.setColor(new vec4(0.3f, 0.3f, 0.3f, 1.0f));

			if(hasClickedInside())
			{
				bSnapped = true;
				Mouse.setBusiness(true);
			}
		}
		else 
		{
			pBackground.setColor(new vec4(0.7f, 0.7f, 0.7f, 1.0f));
			pSliderRect.setColor(new vec4(0.4f, 0.4f, 0.4f, 1.0f));
		}

		if(bSnapped)
		{
			pBackground.setColor(new vec4(0.6f, 0.6f, 0.6f, 1.0f));
			pSliderRect.setColor(new vec4(0.3f, 0.3f, 0.3f, 1.0f));
			float newvar = fValue;
			if(bInfinite)
			{
				float speed = 0.1f;
				if(Window.CurrentlyBoundWindow.getKeyboard().checkKeyPressed(Keyboard.GUM_KEY_LEFT_CONTROL))
					speed *= 10;
				else if(Window.CurrentlyBoundWindow.getKeyboard().checkKeyPressed(Keyboard.GUM_KEY_LEFT_SHIFT))
					speed *= 0.01;
					
				Window.CurrentlyBoundWindow.getMouse().lock(true);
				newvar += (Window.CurrentlyBoundWindow.getMouse().getPositionDelta().x + Window.CurrentlyBoundWindow.getMouse().getMouseWheelState()) * speed;
			}
			else 
			{
				newvar = (float)(Window.CurrentlyBoundWindow.getMouse().getPosition().x - getPosition().x) / (float)getSize().x;
				newvar = GumMath.clamp(newvar, 0.0f, 1.0f);
			}
			if(newvar != fValue)
			{
				fValue = newvar;
				updateData();
				if(callbackFunction != null) { callbackFunction.run(fValue); }
			}
			if(!Window.CurrentlyBoundWindow.getMouse().hasLeftClick())
			{
				bSnapped = false;
				Window.CurrentlyBoundWindow.getMouse().lock(false);
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



	/*static Slider createFromXMLNode(XMLNode node)
	{
		float length        = node.mAttributes["length"]        != "" ? Tools::StringToFloat(Tools::strExtractNumbers(node.mAttributes["length"]))     : 10.0f;
		int precision       = node.mAttributes["precision"]     != "" ? Tools::StringToInt(Tools::strExtractNumbers(node.mAttributes["precision"]))    : 1;
		float multiplier    = node.mAttributes["multiplier"]    != "" ? Tools::StringToFloat(Tools::strExtractNumbers(node.mAttributes["multiplier"])) : 1.0f;
		bool infiniteslider = node.mAttributes["infinite"] == "true";
		String unit    = node.mAttributes["unit"];
		String name    = node.mAttributes["title"];

		Slider slidergui = new Slider(ivec2(0,0), length, name, precision);
		slidergui.setSizeInPercent(Tools.strContains(node.mAttributes["length"], "%"), false);
		slidergui.setViewMultiplier(multiplier);
		slidergui.setInfinite(infiniteslider);
		slidergui.setUnit(unit);
		return slidergui;
	}*/
};