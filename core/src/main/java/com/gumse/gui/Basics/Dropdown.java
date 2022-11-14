package com.gumse.gui.Basics;

import com.gumse.basics.SmoothFloat;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Debug;

public class Dropdown extends RenderGUI
{
	private TextBox pPreviewTextbox;
	private SmoothFloat pSmoothFloat;
	private Font pFont;

	private boolean bIsClicked = false;
	private boolean bIsOpen = false;
	private float fScrollOffset = 0.0f;
	private int iTextSize = 0;
	private int iNumEntries;
	private String sCurrentString;


	public interface DropdownEntryCallback {
		void run(String str);
	}

	public class MenuEntry extends RenderGUI
	{
		private TextBox pBox;
		private DropdownEntryCallback pCallback;

		public MenuEntry(String name, Font font, int offset, DropdownEntryCallback callback)
		{
			vPos = new ivec2(0, offset);
			vSize = new ivec2(100, 100);
			v4Color = new vec4(0.7f, 0.7f, 0.7f, 1.0f);
			setSizeInPercent(true, true);

			pBox = new TextBox(name, font, new ivec2(0, 0), new ivec2(100, 100));
			pBox.setSizeInPercent(true, true);
			pBox.setTextSize(iTextSize);
			pBox.setColor(v4Color);
			addElement(pBox);
			this.pCallback = callback;
		}
	};


	public Dropdown(String text, Font pFont, ivec2 pos, ivec2 size, int textsize)
	{
		this.vPos.set(pos);
		this.vSize.set(size);
		this.sType = "Dropdown";
		this.iTextSize = textsize;
		this.pFont = pFont;
	
		pSmoothFloat = new SmoothFloat(0, 10, 0);
		pPreviewTextbox = new TextBox(text, pFont, new ivec2(0,0), new ivec2(100, 100));
		pPreviewTextbox.setTextSize(textsize);
		pPreviewTextbox.setSizeInPercent(true, true);
		this.addElement(pPreviewTextbox);
	
		sCurrentString = "";
		iNumEntries = 0;
	
		resize();
		reposition();
	}
	
	
	public void cleanup(){}

	private void moveEntries()
	{
		int entriesHeight = (iNumEntries) * vActualSize.y;
		getChild(0).setPosition(new ivec2(0, (int)(pSmoothFloat.get() * entriesHeight) - entriesHeight + vActualSize.y));
	}
	
	public void update()
	{
		if(pPreviewTextbox.isMouseInside())
		{
			Mouse.setActiveHovering(true);
			Window.CurrentlyBoundWindow.getMouse().setCursor(Mouse.GUM_CURSOR_HAND);
			pPreviewTextbox.setColor(vec4.sub(v4Color, new vec4(0.02f, 0.02f, 0.02f, 0.0f)));
			if(Window.CurrentlyBoundWindow.getMouse().hasLeftClick())
			{
				pPreviewTextbox.setColor(vec4.sub(v4Color, new vec4(0.05f, 0.05f, 0.05f, 0.0f)));
			}
			if(Window.CurrentlyBoundWindow.getMouse().hasLeftRelease())
			{
				this.Switch();
			}
		}
		else
		{
			pPreviewTextbox.setColor(v4Color);
		}
	
		/*bIsClicked = false;
		if(bIsOpen && bDone)
		{
			for(int i = 0; i < vMenuEntries.size(); i++)
			{
				if(vMenuEntries.get(i).box.getPosition().y > pPreviewTextbox.getPosition().y)  //Only render if entry is underneath the pPreviewTextbox
				{
					if(vMenuEntries.get(i).box.isMouseInside())
					{
						vMenuEntries.get(i).box.setColor(new vec4(0.5f, 0.5f, 0.5f, 1.0f));
						if(Window.CurrentlyBoundWindow.getMouse().hasLeftRelease())
						{
							sCurrentString = vMenuEntries.get(i).box.getString();
							pPreviewTextbox.setString(sCurrentString);
							iCurrentIndex = i;
							bIsClicked = true;
							close();
							if(vMenuEntries.get(i).function != null)
							{
								vMenuEntries.get(i).function.run(vMenuEntries.get(i).box.getString());
							}
						}
					}
					else
					{
						vMenuEntries.get(i).box.setColor(new vec4(0.7f, 0.7f, 0.7f, 1.0f));
					}
				}
			}
		}*/

		if(numChildren() <= 0) 
			return;

		if(pSmoothFloat.update())
		{
			moveEntries();
		}
		
	
		/*if(bIsOpen)
		{
			//If all entries together are bigger (longer) than then screen
			if(vMenuEntries.get(0).box.getSize().y * (int)vMenuEntries.size() > Window.CurrentlyBoundWindow.getSize().y - pPreviewTextbox.getSize().y)
			{
				//If Mouse is on the dropdown
				if(Toolbox.checkPointInBox(Window.CurrentlyBoundWindow.getMouse().getPosition(), new bbox2i(vMenuEntries.get(0).box.getPosition(), new ivec2(vMenuEntries.get(0).box.getSize().x, vMenuEntries.get(0).box.getSize().y * vMenuEntries.size()))))
				{
					fScrollOffset += Window.CurrentlyBoundWindow.getMouse().getMouseWheelState() * vMenuEntries.get(0).box.getSize().y;
					if(fScrollOffset > 0) { fScrollOffset = 0; } //Do not move below the dropdown menu
				}
			}
		}*/
	}
	
	public void render()
	{
		if(numChildren() > 0)
		{
			if(getChild(0).getPosition().y > vActualPos.y)
				getChild(0).render();
			else
			{
				for(RenderGUI child : getChild(0).getChildren())
				{
					if(child.getPosition().y > vActualPos.y)  //Only render if entry is underneath the pPreviewTextbox
					{
						child.render();
					}
				}
			}
		}

		pPreviewTextbox.render();
	}
	
	public void addEntry(String title, DropdownEntryCallback OnCLickFunction, boolean active)
	{
		MenuEntry entry = new MenuEntry(title, pFont, iNumEntries * vActualSize.y, OnCLickFunction);
		if(iNumEntries++ == 0) { addGUI(entry); }
		else                   { getChild(0).addGUI(entry); }
	
		if(active)
			this.pPreviewTextbox.setString(title);

		moveEntries();
	}
	
	protected void updateOnTitleChange()
	{
		this.pPreviewTextbox.setString(sTitle);
	}
	
	public int numEntries()			  { return iNumEntries; }
	public String getCurrentTitle()	  { return sCurrentString; }
	public boolean isCurrentClicked() { return bIsClicked; }
	public boolean isOpen()			  { return pSmoothFloat.getTarget() > 0; }
	
	public void clearMenu() 		  { this.destroyChildren(); }
	public void close() 			  { bIsOpen = false;    pSmoothFloat.setTarget(0); }
	public void open() 				  { bIsOpen = true;     pSmoothFloat.setTarget(1); }
	public void Switch() 			  { bIsOpen = !bIsOpen; pSmoothFloat.setTarget(bIsOpen ? 1 : 0); }
	
	/*public static Dropdown createFromXMLNode(XMLNode node)
	{
		Font font = FontManager.getInstance().getDefaultFont();
		float fontsize     = node.mAttributes["fontsize"] != "" ? Tools::StringToFloat(node.mAttributes["fontsize"]) : 12.0f;
		return new Dropdown("", font, new ivec2(0,0), new ivec2(0,0), fontsize);
	}*/
};