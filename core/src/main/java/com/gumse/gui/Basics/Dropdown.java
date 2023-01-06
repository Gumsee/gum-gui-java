package com.gumse.gui.Basics;

import com.gumse.basics.SmoothFloat;
import com.gumse.gui.GUI;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Mouse;

public class Dropdown extends RenderGUI
{
    public interface DropdownSelectionCallback {
        void run(String str);
    }

	public interface DropdownEntryCallback {
		void run(String str);
	}

	public class MenuEntry extends RenderGUI
	{
		private TextBox pBox;
		private DropdownEntryCallback pCallback;
        private DropdownSelectionCallback pGlobalCallback;
        private Dropdown pParent;

		public MenuEntry(String name, Font font, int offset, DropdownEntryCallback callback, Dropdown parent)
		{
			this.vPos = new ivec2(0, offset);
			this.vSize = new ivec2(100, 100);
            this.pParent = parent;
			setSizeInPercent(true, true);

			pBox = new TextBox(name, font, new ivec2(0, 0), new ivec2(100, 100));
			pBox.setSizeInPercent(true, true);
			pBox.setTextSize(iTextSize);
            pBox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
			addElement(pBox);
			this.pCallback = callback;
		}

        @Override
        public void update()
        {
            if(isMouseInsideSkipChildren())
            {
                Mouse.setActiveHovering(true);
                Window.CurrentlyBoundWindow.getMouse().setCursor(Mouse.GUM_CURSOR_HAND);
                pBox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), new vec4(0.02f, 0.02f, 0.02f, 0.0f)));
                if(isHoldingLeftClick())
                    pBox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), new vec4(0.05f, 0.05f, 0.05f, 0.0f)));

                if(isClicked())
                {
                    pParent.setTitle(pBox.getTitle());
                    pParent.close();

                    if(pCallback != null)
                        pCallback.run(pBox.getTitle());
                    if(pGlobalCallback != null)
                        pGlobalCallback.run(pBox.getTitle());
                }
            }
            else
            {
                pBox.setColor(getColor(GUI.getTheme().primaryColor));
            }
        }

        public void setSelectCallback(DropdownSelectionCallback callback)
        {
            pGlobalCallback = callback;
        }
	};

	private TextBox pPreviewTextbox;
	private SmoothFloat pSmoothFloat;
    private DropdownSelectionCallback pGlobalCallback;
	private Font pFont;

	private boolean bIsClicked = false;
	private boolean bIsOpen = false;
	private float fScrollOffset = 0.0f;
	private int iTextSize = 0;
	private int iNumEntries;

	public Dropdown(String text, Font pFont, ivec2 pos, ivec2 size, int textsize)
	{
		this.vPos.set(pos);
		this.vSize.set(size);
		this.sType = "Dropdown";
		this.iTextSize = textsize;
		this.pFont = pFont;
		this.iNumEntries = 0;
	
		pSmoothFloat = new SmoothFloat(0, 10, 0);
		pPreviewTextbox = new TextBox(text, pFont, new ivec2(0,0), new ivec2(100, 100));
		pPreviewTextbox.setTextSize(textsize);
		pPreviewTextbox.setTextColor(GUI.getTheme().textColor);
        pPreviewTextbox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
		pPreviewTextbox.setSizeInPercent(true, true);
		this.addElement(pPreviewTextbox);

	
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
			pPreviewTextbox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), new vec4(0.02f, 0.02f, 0.02f, 0.0f)));
			if(Window.CurrentlyBoundWindow.getMouse().hasLeftClick())
			{
				pPreviewTextbox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), new vec4(0.05f, 0.05f, 0.05f, 0.0f)));
			}
			if(Window.CurrentlyBoundWindow.getMouse().hasLeftRelease())
			{
				this.Switch();
			}
		}
		else
		{
			pPreviewTextbox.setColor(getColor(GUI.getTheme().primaryColor));
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
        
        //Only update if entry is underneath the pPreviewTextbox
        if(getChild(0).getPosition().y > vActualPos.y)
        {
            getChild(0).update();
            for(RenderGUI child : getChild(0).getChildren())
                child.update();
        }
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
		MenuEntry entry = new MenuEntry(title, pFont, iNumEntries * vActualSize.y, OnCLickFunction, this);
		if(iNumEntries++ == 0) { addGUI(entry); }
		else                   { getChild(0).addGUI(entry); }
        entry.setSelectCallback(pGlobalCallback);
	
		if(active)
			setTitle(title);

		moveEntries();
	}

    public void onSelection(DropdownSelectionCallback callback)
    {
        pGlobalCallback = callback;
        if(numChildren() > 0)
        {
            ((MenuEntry)getChild(0)).setSelectCallback(pGlobalCallback);
            for(RenderGUI child : getChild(0).getChildren())
                ((MenuEntry)child).setSelectCallback(pGlobalCallback);
        }
    }
	
    @Override
	protected void updateOnTitleChange()
	{
		this.pPreviewTextbox.setString(sTitle);
	}
	
	public int numEntries()			  { return iNumEntries; }
	public boolean isCurrentClicked() { return bIsClicked; }
	public boolean isOpen()			  { return pSmoothFloat.getTarget() > 0; }
	
	public void clearMenu() 		  { this.destroyChildren(); }
	public void close() 			  { bIsOpen = false;    pSmoothFloat.setTarget(0); }
	public void open() 				  { bIsOpen = true;     pSmoothFloat.setTarget(1); }
	public void Switch() 			  { bIsOpen = !bIsOpen; pSmoothFloat.setTarget(bIsOpen ? 1 : 0); }
	
	public static Dropdown createFromXMLNode(XMLNode node)
	{
        String fontName = node.getAttribute("font");
        Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());
		int fontsize     = node.getIntAttribute("fontsize", 12);
		return new Dropdown("", font, new ivec2(0,0), new ivec2(0,0), fontsize);
	}
};