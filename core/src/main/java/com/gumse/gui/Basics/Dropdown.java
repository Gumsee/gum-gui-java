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
        public void updateextra()
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

        @Override
        protected void updateOnThemeChange() 
        {
            pBox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
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
	private int iTextSize = 0;

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
        pPreviewTextbox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
		pPreviewTextbox.setSizeInPercent(true, true);
		this.addElement(pPreviewTextbox);

        onHover(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            { 
                pPreviewTextbox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), new vec4(0.02f, 0.02f, 0.02f, 0.0f)));
                if(isHoldingLeftClick())
                    pPreviewTextbox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), new vec4(0.05f, 0.05f, 0.05f, 0.0f)));
            }
        }, Mouse.GUM_CURSOR_HAND);

        onClick(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            { 
                Switch();
            }
        });

        onLeave(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            { 
                pPreviewTextbox.setColor(getColor(GUI.getTheme().primaryColor));
            }
        });
	
		resize();
		reposition();
	}
	
	
	public void cleanup(){}

	private void moveEntries()
	{
		int entriesHeight = vElements.size() * vActualSize.y;
        for(int i = 1; i < vElements.size(); i++)
        {
            RenderGUI entry = vElements.get(i);
            int ypos = i * vActualSize.y + (int)(pSmoothFloat.get() * entriesHeight);
            ypos -= entriesHeight;
            entry.setPosition(new ivec2(0, ypos));
            entry.hide(ypos < 0);
        }
	}
	
	public void updateextra()
	{	
		if(pSmoothFloat.update())
			moveEntries();
	}
	
	public void renderextra()
	{
        for(int i = numElements(); i --> 0;) { vElements.get(i).render();  }
	}
	
	public void addEntry(String title, DropdownEntryCallback OnCLickFunction, boolean active)
	{
		MenuEntry entry = new MenuEntry(title, pFont, vElements.size() * vActualSize.y, OnCLickFunction, this);
        entry.setSelectCallback(pGlobalCallback);
		addElement(entry);
	
		if(active)
			setTitle(title);

		moveEntries();
	}

    public void onSelection(DropdownSelectionCallback callback)
    {
        pGlobalCallback = callback;
        for(int i = 1; i < vElements.size(); i++)
            ((MenuEntry)vElements.get(i)).setSelectCallback(pGlobalCallback);
    }
	
    @Override
	protected void updateOnTitleChange()
	{
		pPreviewTextbox.setString(sTitle);
	}

    @Override
    protected void updateOnThemeChange() 
    {
        pPreviewTextbox.setColor(getColor(GUI.getTheme().primaryColor));
        pPreviewTextbox.getBox().setBorderThickness(GUI.getTheme().borderThickness);
    }
	
	public int numEntries()			  { return vElements.size() - 1; }
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