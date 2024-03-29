package com.gumse.gui.TagList;

import java.util.ArrayList;
import java.util.List;

import com.gumse.gui.Basics.TextField;
import com.gumse.gui.Basics.TextField.TextFieldInputCallback;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
import com.gumse.maths.ivec2;
import com.gumse.system.filesystem.XML.XMLNode;

public class TagList <T> extends RenderGUI
{
    public interface TagCallback
    {
        public void added(String tag);
        public void removed(String tag);
    }

    public interface TagCreateFunction <T>
    {
        T create(String str);
    }

    private TextField pTextField;
    private RenderGUI pTagContainer;
    private Font pFont;
    private GUICallback pRemoveCallback;
    private TagCallback pTagCallback;
    private boolean bOnlyWords;
    private static final int TAG_GAP = 5;

    public TagList(ivec2 pos, ivec2 size, Font font, TagCreateFunction<T> createfunc)
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
                addTag(str, createfunc.create(str));
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
                            addTag(word, createfunc.create(word));
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

        pRemoveCallback = new GUICallback() {
            @Override public void run(RenderGUI gui) 
            {
                TagListEntry<T> entry = (TagListEntry<T>)gui;
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

    public void addTag(String str, T userptr)
    {
        //Skip duplicates
        for(RenderGUI child : pTagContainer.getChildren())
        {
            TagListEntry<T> entry = (TagListEntry<T>)child;
            if(entry.getName().equals(str))
                return;
        }
        
        ivec2 pos = new ivec2(0, 0);
        TagListEntry<T> entry = new TagListEntry<>(pos, str, pFont, pRemoveCallback, userptr);
        pTagContainer.addGUI(entry);

        updateOnSizeChange();

        if(pTagCallback != null)
            pTagCallback.added(str);
    }

    public void reset()
    {
        pTagContainer.destroyChildren();
    }

    //
    // Getter
    //
    public List<String> getTags()
    {
        List<String> retList = new ArrayList<>();

        for(RenderGUI child : pTagContainer.getChildren())
        {
            TagListEntry<T> entry = (TagListEntry<T>)child;
            retList.add(entry.getName());
        }

        return retList;
    }

    public List<T> getTagUserptrs()
    {
        List<T> retList = new ArrayList<>();

        for(RenderGUI child : pTagContainer.getChildren())
        {
            TagListEntry<T> entry = (TagListEntry<T>)child;
            retList.add(entry.getUserptr());
        }

        return retList;
    }

    //
    // Setter
    //
    public void setHint(String hint)                   { this.pTextField.setHint(hint); }
    public void setMaximumWordLength(int len)          { this.pTextField.getBox().setMaxTextlength(len); }
    public void setTagCallback(TagCallback callback)   { this.pTagCallback = callback; }
    public void onlyAllowWords(boolean allow)          { this.bOnlyWords = allow; }
    @Override public void setLocaleID(String localeid) { this.pTextField.setLocaleID(localeid); }


    public static XMLGUICreator createFromXMLNode() 
    {
        return (XMLNode node) -> { 
            String fontName = node.getAttribute("font");
            Font font = (!fontName.equals("") ? FontManager.getInstance().getFont(fontName) : FontManager.getInstance().getDefaultFont());
    
            TagList<Object> taglistgui = new TagList<>(new ivec2(0,0), new ivec2(100,30), font, (String str) -> { return new Object(); });
            return taglistgui;
        };
    };
}