package com.gumse.gui.Basics;

import com.gumse.basics.SmoothFloat;
import com.gumse.gui.GUI;
import com.gumse.gui.Locale;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
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

	public class MenuEntry extends TextBox
	{
		private DropdownEntryCallback pCallback;
        private DropdownSelectionCallback pGlobalCallback;
        private Dropdown pParent;

		public MenuEntry(String name, Font font, int offset, DropdownEntryCallback callback, Dropdown parent)
		{
			super(name, font, new ivec2(0, offset), new ivec2(100, 100));
            this.pParent = parent;
			this.pCallback = callback;
            
            setColor(getColor(GUI.getTheme().primaryColor));
			setSizeInPercent(true, true);
			setTextSize(iTextSize);
            getBox().setBorderThickness(GUI.getTheme().borderThickness);
            setCornerRadius(new vec4(0,0,0,0));
		}

        @Override
        public void updateextra()
        {
            if(isMouseInsideSkipChildren())
            {
                Mouse.setActiveHovering(true);
                Window.CurrentlyBoundWindow.getMouse().setCursor(Mouse.GUM_CURSOR_HAND);
                setColor(vec4.sub(GUI.getTheme().primaryColor, new vec4(0.02f, 0.02f, 0.02f, 0.0f)));
                if(isHoldingLeftClick())
                    setColor(vec4.sub(GUI.getTheme().primaryColor, new vec4(0.05f, 0.05f, 0.05f, 0.0f)));

                if(isClicked())
                {
                    pParent.setTitle(getString());
                    pParent.close();

                    if(pCallback != null)
                        pCallback.run(getString());
                    if(pGlobalCallback != null)
                        pGlobalCallback.run(getString());
                }
            }
            else
            {
                setColor(GUI.getTheme().primaryColor);
            }
        }

        @Override
        protected void updateOnThemeChange() 
        {
            if(!sLocaleID.isEmpty())
               setString(Locale.getCurrentLocale().getString(sLocaleID));
            getBox().setBorderThickness(GUI.getTheme().borderThickness);
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

        onHover((RenderGUI gui) -> { 
                pPreviewTextbox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), new vec4(0.02f, 0.02f, 0.02f, 0.0f)));
                if(isHoldingLeftClick())
                    pPreviewTextbox.setColor(vec4.sub(getColor(GUI.getTheme().primaryColor), new vec4(0.05f, 0.05f, 0.05f, 0.0f)));
        }, Mouse.GUM_CURSOR_HAND);

        onClick((RenderGUI gui) -> { Switch(); });
        onLeave((RenderGUI gui) -> { pPreviewTextbox.setColor(getColor(GUI.getTheme().primaryColor)); });
	
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
        if(Window.CurrentlyBoundWindow.getMouse().hasLeftRelease() && !isMouseInside())
            close();
        
		if(pSmoothFloat.update())
			moveEntries();
	}
	
	public void renderextra()
	{
        for(int i = numElements(); i --> 0;) { vElements.get(i).render();  }
	}
	
	public TextBox addEntry(String title, DropdownEntryCallback OnCLickFunction, boolean active)
	{
		MenuEntry entry = new MenuEntry(title, pFont, vElements.size() * vActualSize.y, OnCLickFunction, this);
        entry.setSelectCallback(pGlobalCallback);
		addElement(entry);
	
		if(active)
			setTitle(title);

		moveEntries();

        return entry;
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
        setCornerRadius();
    }
	
	public int numEntries()			  { return vElements.size() - 1; }
	public boolean isCurrentClicked() { return bIsClicked; }
	public boolean isOpen()			  { return pSmoothFloat.getTarget() > 0; }
	
	public void clearMenu() 		  { this.destroyChildren(); }
	public void close() 			  { bIsOpen = false;    pSmoothFloat.setTarget(0); setCornerRadius(); }
	public void open() 				  { bIsOpen = true;     pSmoothFloat.setTarget(1); setCornerRadius(); }
	public void Switch() 			  { bIsOpen = !bIsOpen; pSmoothFloat.setTarget(bIsOpen ? 1 : 0); setCornerRadius(); }

    public void setCornerRadius()
    {
        if(bIsOpen)
        {
            pPreviewTextbox.setCornerRadius(new vec4(GUI.getTheme().cornerRadius.x, GUI.getTheme().cornerRadius.y, 0, 0));
            vElements.get(numElements() - 1).setCornerRadius(new vec4(0, 0, GUI.getTheme().cornerRadius.z, GUI.getTheme().cornerRadius.w));
        }
        else
        {
            pPreviewTextbox.setCornerRadius(GUI.getTheme().cornerRadius);
        }
    }

    public static XMLGUICreator createFromXMLNode() 
    {
        return (XMLNode node) -> { 
            String fontName = node.getAttribute("font");
            Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());
            int fontsize     = node.getIntAttribute("fontsize", 12);
            return new Dropdown("", font, new ivec2(0,0), new ivec2(0,0), fontsize);
        };
    };
};