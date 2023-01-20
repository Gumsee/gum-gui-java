package com.gumse.gui.HierarchyList;

import java.util.Arrays;

import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Basics.TextField;
import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Basics.TextField.TextFieldInputCallback;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Shape;
import com.gumse.maths.*;
import com.gumse.system.Window;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Toolbox;

public class HierarchyListEntry extends RenderGUI
{
    private static final int INDENT_SIZE = 10;
    private RenderGUI pTitleBox;
    private boolean bHasChildEntries;
    private GUICallback pCallback;
    private int iIndent = 0;
    private HierarchyList pParentList;

    private Shape pArrowShape;
    private static final Float[] faArrowVertices = new Float[] { 
        0.5f,  1.0f, 0.0f,
        0.5f,  0.0f, 0.0f,
        1.0f,  0.5f, 0.0f,
    };

    private void repositionEntries() { repositionEntries(0); }
    private void repositionEntries(int offset)
    {
        setPosition(new ivec2(INDENT_SIZE, offset));

        int nextoffset = getSize().y;
        for(int i = 0; i < numChildren(); i++)
        {
            HierarchyListEntry entry = (HierarchyListEntry)getChild(i);
            entry.repositionEntries(nextoffset);
            nextoffset += entry.getHeight();
        }
    }


    //
    // List Entry
    //
    public HierarchyListEntry(String name, HierarchyList parentlist, GUICallback callback)
    {
        this.bChildrenHidden = true;
        this.bHasChildEntries = false;
        this.pCallback = callback;
        this.sType = "HierarchyListEntry";
        this.pParentList = parentlist;

        if(parentlist.isEditable())
        {
            TextField titlefield = new TextField(name, FontManager.getInstance().getDefaultFont(), new ivec2(20,0), new ivec2(100, 100));
            titlefield.shouldActivateOnDoubleclick(true);
            titlefield.getBox().getBox().hide(true);
            titlefield.getBox().getText().setCharacterHeight(20);
            titlefield.setCursorShapeOnHover(Mouse.GUM_CURSOR_DEFAULT);
            pTitleBox = titlefield;
        }
        else
        {
            TextBox titlebox = new TextBox(name, FontManager.getInstance().getDefaultFont(), new ivec2(20,0), new ivec2(100, 100));
            titlebox.setAlignment(Alignment.LEFT);
            titlebox.getBox().hide(true);
            titlebox.getText().setCharacterHeight(20);
            //titlebox.setCursorShapeOnHover(Mouse.GUM_CURSOR_DEFAULT);
            pTitleBox = titlebox;
        }

        pTitleBox.setSizeInPercent(true, true);
        addElement(pTitleBox);
        
        pArrowShape = new Shape("hierarchylistentryarrow", new ivec2(4, 0), new ivec2(16), Arrays.asList(faArrowVertices));
        pArrowShape.setRotationOrigin(new ivec2(8));
        addElement(pArrowShape);
        updateOnPosChange();

        setSize(new ivec2(100, 30));
        setSizeInPercent(true, false);
        reposition();
    }

    public void cleanup()
    {
        //_delete(title);
        destroyChildren();
    }

    public void updateextra()
    {
        Mouse mouse = Window.CurrentlyBoundWindow.getMouse();
        if((!pParentList.isEditable() || !((TextField)pTitleBox).isEditing()) && 
           isMouseInside() && !RenderGUI.somethingHasBeenClicked())
        {
            if(bHasChildEntries)
            {
                if(Toolbox.checkPointInBox(mouse.getPosition(), new bbox2i(vActualPos, new ivec2(20, 30))))
                {
                    Mouse.setActiveHovering(true);
                    mouse.setCursor(Mouse.GUM_CURSOR_HAND);
                    if(mouse.hasLeftRelease() && Toolbox.checkPointInBox(mouse.getLeftClickPosition(), new bbox2i(vActualPos, new ivec2(20, 30))))
                    {
                        hiddenState(!bChildrenHidden);
                        pParentList.selectEntry(null);
                    }
                }
            }

            //Call callback
            if(Toolbox.checkPointInBox(mouse.getLeftClickPosition(), new bbox2i(ivec2.add(vActualPos, new ivec2(20, 0)), ivec2.sub(vActualSize, new ivec2(20, 0)))))
            {
                if(mouse.hasLeftRelease())
                {
                    pParentList.selectEntry(this);
                    if(pCallback != null)
                        pCallback.run(this);
                }
                if(mouse.hasLeftDoubleClick())
                {
                    pParentList.selectEntry(null);
                }
            }
        }
    }

    public void renderextra()
    {
        if(bHasChildEntries)
            pArrowShape.render();

        pTitleBox.render();
        if(!bChildrenHidden)
            renderchildren();
    }

    public void addEntry(HierarchyListEntry entry)
    {
        entry.setIndent(iIndent + INDENT_SIZE);
        addGUI(entry);
        repositionEntries();
        bHasChildEntries = true;
    }

    public void updateBoundingBox(boolean override)
    { 
        if(bKeepTrackOfBoundingBox || override)
        {
            bBoundingBox.pos.set(vActualPos);
            bBoundingBox.size.set(getSize());

            if(!bChildrenHidden)
            {
                //bBoundingBox.size.y += getSize().y * (numChildren());
                for(int i = 0; i < numChildren(); i++)
                {
                    HierarchyListEntry child = ((HierarchyListEntry)getChild(i));
                    child.updateBoundingBox(true);
                    bBoundingBox.size.y += child.getBoundingBox().size.y;
                }
            }
        }
    }

    public int getHeight()
    {
        if(!bChildrenHidden)
        {
            int height = getSize().y;
            for(RenderGUI child : vChildren)
                height += ((HierarchyListEntry)child).getHeight();
            return height;
        }

        
        return getSize().y;
    }

    public void hiddenState(boolean hidden)
    {
        bChildrenHidden = hidden;
        if(bChildrenHidden)
            pArrowShape.setRotation(0.0f);
        else
            pArrowShape.setRotation(-90.0f);

        updateOnPosChange();
        pParentList.getRootEntry().repositionEntries();
    }

    public void openAll()
    {
        bChildrenHidden = false;
        for(int i = 0; i < numChildren(); i++)
        {
            HierarchyListEntry entry = (HierarchyListEntry)getChild(i);
            entry.openAll();
        }
        if(pParent != null && pParent.getType() == "HierarchyListEntry")
            ((HierarchyListEntry)pParent).repositionEntries();
    }

    public void setIndent(int indent)
    {
        this.iIndent = indent;
        //this.title.setPosition(ivec2(indent, title.getPosition().y));
    }
    public void setRenameCallback(TextFieldInputCallback callback)
    {
        if(pParentList.isEditable())
            ((TextField)pTitleBox).setCallback(callback);
    }

    public static void cleanupAll()
    {
        //_delete(pArrowVAO);
    }
};