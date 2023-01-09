package com.gumse.gui.TagList;

import java.util.ArrayList;
import java.util.List;

import com.gumse.gui.Basics.TextField;
import com.gumse.gui.Basics.TextField.TextFieldInputCallback;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.TagList.TagListEntry.TagRemoveCallback;
import com.gumse.maths.ivec2;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.tools.Output;

public class TagList extends RenderGUI
{
    public interface TagCallback
    {
        public void added(String tag);
        public void removed(String tag);
    }

    private TextField pTextField;
    private RenderGUI pTagContainer;
    private Font pFont;
    private TagRemoveCallback pRemoveCallback;
    private TagCallback pTagCallback;
    private boolean bOnlyWords;
    private static final int TAG_GAP = 5;

    public TagList(ivec2 pos, ivec2 size, Font font)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.pFont = font;
        this.bOnlyWords = true;
        this.pTagCallback = null;

        pTextField = new TextField("", font, new ivec2(0,0), new ivec2(100, size.y));
        pTextField.setHint("Add Tag");
        pTextField.setSizeInPercent(true, false);
        pTextField.setCallback(new TextFieldInputCallback() {
            @Override public void enter(String str) 
            {
                addTag(str);
                pTextField.setString("");
            } 
            
            @Override public void input(String input, String complete) 
            {
                if(bOnlyWords)
                {
                    if(input.contains(" "))
                    {
                        String[] words = complete.split(" ");
                        for(String word : words)
                        {
                            addTag(word);
                        }
                        pTextField.setString("");
                    }
                }
            } 
        });
        addElement(pTextField);


        pTagContainer = new RenderGUI();
        pTagContainer.setSize(new ivec2(100, size.y));
        pTagContainer.setPosition(new ivec2(0, size.y + 5));
        pTagContainer.setSizeInPercent(true, false);
        addElement(pTagContainer);

        pRemoveCallback = new TagRemoveCallback() {
            @Override public void run(TagListEntry entry) 
            {
                pTagContainer.removeChild(entry);
                updateOnSizeChange();
                if(pTagCallback != null)
                    pTagCallback.removed(entry.getName());
            }
        };

        resize();
        reposition();
    }

    @Override
    protected void updateOnSizeChange()
    {
        ivec2 offset = new ivec2(0,0);
        int extraoffset = 0;
        for(RenderGUI child : pTagContainer.getChildren())
        {
            if(offset.x + child.getSize().x > vActualSize.x)
            {
                offset.x = 0;
                offset.y += child.getSize().y + TAG_GAP;
            }
            child.setPosition(offset);
            offset.x += child.getSize().x + TAG_GAP;
            extraoffset = child.getSize().y + TAG_GAP;
        }

        vSize.y = pTagContainer.getPosition().y + offset.y + extraoffset;
    }

    public void addTag(String str)
    {
        //Skip duplicates
        for(RenderGUI child : pTagContainer.getChildren())
        {
            TagListEntry entry = (TagListEntry)child;
            if(entry.getName().equals(str))
                return;
        }
        
        ivec2 pos = new ivec2(0, 0);
        TagListEntry entry = new TagListEntry(pos, str, pFont, pRemoveCallback);
        pTagContainer.addGUI(entry);

        updateOnSizeChange();

        if(pTagCallback != null)
            pTagCallback.added(str);
    }

    //
    // Getter
    //
    public List<String> getTags()
    {
        List<String> retList = new ArrayList<>();

        for(RenderGUI child : pTagContainer.getChildren())
        {
            TagListEntry entry = (TagListEntry)child;
            retList.add(entry.getName());
        }

        return retList;
    }

    //
    // Setter
    //
    public void setHint(String hint)                 { this.pTextField.setHint(hint); }
    public void setMaximumWordLength(int len)        { this.pTextField.getBox().setMaxTextlength(len); }
    public void setTagCallback(TagCallback callback) { this.pTagCallback = callback; }
    public void onlyAllowWords(boolean allow)        { this.bOnlyWords = allow; }


	public static TagList createFromXMLNode(XMLNode node)
	{
        String fontName = node.getAttribute("font");
        Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());

		TagList taglistgui = new TagList(new ivec2(0,0), new ivec2(100,30), font);
		return taglistgui;
	}
}