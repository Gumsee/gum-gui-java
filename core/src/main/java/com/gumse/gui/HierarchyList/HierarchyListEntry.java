package com.gumse.gui.HierarchyList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gumse.gui.Basics.Switch;
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
import com.gumse.tools.Output;
import com.gumse.tools.Toolbox;

public class HierarchyListEntry <T> extends RenderGUI
{
    private static final int INDENT_SIZE = 10;
    private RenderGUI pTitleBox;
    private HierarchyList<T> pParentList;
    private Switch pSelectSwitch;
    private T pUserPtr;

    private Shape pArrowShape;
    private static final Float[] faArrowVertices = new Float[] { 
        0.5f,  1.0f, 0.0f,
        0.5f,  0.0f, 0.0f,
        1.0f,  0.5f, 0.0f,
    };

    protected void repositionEntries() { repositionEntries(0, false); }
    private void repositionEntries(int offset, boolean untick)
    {
        setPosition(new ivec2(INDENT_SIZE, offset));
        if(pSelectSwitch != null && untick)
            pSelectSwitch.tick(false);

        int nextoffset = getSize().y;
        for(int i = 0; i < numChildren(); i++)
        {
            HierarchyListEntry<T> entry = (HierarchyListEntry<T>)getChild(i);
            entry.repositionEntries(nextoffset, untick || bChildrenHidden);
            nextoffset += entry.getHeight();
        }
    }


    //
    // List Entry
    //
    public HierarchyListEntry(String name, HierarchyList<T> parentlist, T userptr)
    {
        this.bChildrenHidden = true;
        this.sType = "HierarchyListEntry";
        this.pParentList = parentlist;
        this.pSelectSwitch = null;
        this.pUserPtr = userptr;
        this.sTitle = name;

        if(parentlist.isEditable())
        {
            TextField titlefield = new TextField(name, FontManager.getInstance().getDefaultFont(), new ivec2(20,0), new ivec2(100, 100));
            titlefield.shouldActivateOnDoubleclick(true);
            titlefield.getBox().getBox().hide(true);
            titlefield.getBox().getText().setCharacterHeight(20);
            //titlefield.setCursorShapeOnHover(Mouse.GUM_CURSOR_DEFAULT);
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

        if(parentlist.isSelectable())
        {
            pSelectSwitch = new Switch(new ivec2(100, 5), new ivec2(20, 20), 0);
            pSelectSwitch.setPositionInPercent(true, false);
            pSelectSwitch.setOrigin(new ivec2(25, 0));
            pSelectSwitch.onTick(pParentList.getTickCallback());
            pTitleBox.addGUI(pSelectSwitch);
        }

        updateOnPosChange();

        setMargin(new ivec2(-INDENT_SIZE, 0));
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
            if(numChildren() > 0)
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
                    if(pParentList.getClickCallback() != null)
                        pParentList.getClickCallback().run(this);
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
        if(numChildren() > 0)
            pArrowShape.render();

        pTitleBox.render();
        if(!bChildrenHidden)
        {
            for(RenderGUI child : getChildren())
                child.render();
        }
    }

    public List<HierarchyListEntry<T> > getTickedEntries()
    {
        List<HierarchyListEntry<T> > retList = new ArrayList<HierarchyListEntry<T> >();
        if(pSelectSwitch.isTicked())
            retList.add(this);

        for(RenderGUI child : getChildren())
            retList.addAll(((HierarchyListEntry<T>)child).getTickedEntries());

        return retList;
    }

    @Override
    protected void updateOnAddGUI(RenderGUI gui) 
    {
        if(!gui.getType().equals("HierarchyListEntry"))
        {
            Output.error("Cannot add GUI of type " + gui.getType() + " to HierarchyList");
            removeChild(gui);
            return;
        }
        repositionEntries();
    }

    public void addEntry(HierarchyListEntry<T> entry)
    {
        addGUI(entry);
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
                    HierarchyListEntry<T> child = ((HierarchyListEntry<T>)getChild(i));
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
                height += ((HierarchyListEntry<T>)child).getHeight();
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
            HierarchyListEntry<T> entry = (HierarchyListEntry<T>)getChild(i);
            entry.openAll();
        }
        if(pParent != null && pParent.getType() == "HierarchyListEntry")
            ((HierarchyListEntry<T>)pParent).repositionEntries();
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

    public void tick(boolean tick) 
    {
        if(pSelectSwitch != null)
            pSelectSwitch.tick(tick);
    }

    public T getUserPtr()     { return pUserPtr; }
    public boolean isTicked() { return pSelectSwitch != null && pSelectSwitch.isTicked(); }
};