package com.gumse.gui.Primitives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.maths.*;
import com.gumse.maths.vec4;
import com.gumse.system.Window;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Output;
import com.gumse.tools.Toolbox;

public class RenderGUI
{
    static boolean bHasClickedSomething = false;

    public interface GUICallback
    {
        void run(RenderGUI gui);
    }

    //private ArrayList<ContextMenuEntry> vContextMenuEntries;
    private bvec2 sizeInPercent, posInPercent, originInPercent, maxSizeInPercent, minSizeInPercent;


    protected ArrayList<RenderGUI> vElements;
    protected ArrayList<RenderGUI> vChildren;
    protected mat4 mTransformationMatrix;
    protected RenderGUI pParent;
    protected Map<String, String> mAttributes;

    protected void updateOnPosChange()  {};
    protected void updateOnSizeChange() {};
    protected void updateOnTitleChange() {};
    protected void updateOnColorChange() {};
    protected void updateOnCornerRadiusChange() {};
    protected void updateOnAddGUI(RenderGUI gui) {};

    protected ivec2 vPos, vActualPos, vOrigin;
    protected ivec2 vSize, vActualSize, vMargin, vMinSize, vMaxSize;
    protected float fRotation;
    protected float fAlphaOverride;
    protected boolean bIsHidden, bChildrenHidden, bKeepTrackOfBoundingBox;

    protected bbox2i bBoundingBox;

    protected vec4 v4Color;
    protected vec4 v4CornerRadius;
    protected String sType = "unset";
    protected String sID;
    protected String sTitle;
    protected String value;
    protected String sToolTip;
    protected int iHoverCursorShape;
    protected GUICallback pClickCallback;
    protected GUICallback pHoverCallback;
    protected GUICallback pEnterCallback;
    protected GUICallback pLeaveCallback;
    protected boolean bIsHovering;
    protected boolean bUpdateFromFirstToLast;


    public RenderGUI() 
    {
        this.v4Color = null;
        this.v4CornerRadius = new vec4(0,0,0,0);
        this.pParent = null;
        this.pClickCallback = null;
        this.pHoverCallback = null;
        this.pEnterCallback = null;
        this.pLeaveCallback = null;
        this.iHoverCursorShape = Mouse.GUM_CURSOR_DEFAULT;
        this.sTitle = "";
        this.sID = "";
        this.fRotation = 0.0f;
        this.fAlphaOverride = 1.0f;
        this.bIsHidden = false;
        this.bChildrenHidden = false;
        this.bKeepTrackOfBoundingBox = false;
        this.bIsHovering = false;
        this.bUpdateFromFirstToLast = false;
        this.vMinSize = new ivec2(0,0);
        this.vMaxSize = new ivec2(0,0);

        
        this.vPos = new ivec2();
        this.vActualPos = new ivec2();
        this.vOrigin = new ivec2();
        this.vSize = new ivec2();
        this.vActualSize = new ivec2();
        this.vMargin = new ivec2();
        this.vMinSize = new ivec2();
        this.vMaxSize = new ivec2();
        this.bBoundingBox = new bbox2i();

        this.vElements = new ArrayList<>();
        this.vChildren = new ArrayList<>();
        this.mTransformationMatrix = new mat4();
        this.mAttributes = new HashMap<String, String>();
        this.sizeInPercent    = new bvec2(false, false);
        this.posInPercent     = new bvec2(false, false);
        this.originInPercent  = new bvec2(false, false);
        this.maxSizeInPercent = new bvec2(false, false);
        this.minSizeInPercent = new bvec2(false, false);
    }

    public void clear() 
    {
        vElements.clear();
        vChildren.clear();
    }


    public void overrideAlpha(float alpha)
    {
        this.fAlphaOverride = alpha;
        for(int i = 0; i < this.numElements(); i++) { this.vElements.get(i).overrideAlpha(alpha); }
        for(int i = 0; i < this.numChildren(); i++) { this.vChildren.get(i).overrideAlpha(alpha); }
    }


    public void addElement(RenderGUI gui)
    {
        this.vElements.add(gui);
        gui.setParent(this);
        gui.reposition();
        gui.resize();
    }

    public void addGUI(RenderGUI gui)
    {
        this.vChildren.add(gui);
        gui.setParent(this);
        gui.reposition();
        gui.resize();

        updateBoundingBox();
        updateOnAddGUI(gui);
    }

    /*public void addContextMenuEntry(String entryname, std::function<void()> callback)
    {
        ContextMenuEntry entry;
        entry.callback = callback;
        entry.name = entryname;
        this.vContextMenuEntries.add(entry);
    }*/

    public void reposition()
    {
        vActualPos.set(vPos);
        if(pParent != null)
        {
            vActualPos = ivec2.add(pParent.getPosition(), vPos);
            if(posInPercent.x) { vActualPos.x = (int)(pParent.getPosition().x + pParent.getSize().x * ((float)vPos.x / 100.0f)); }
            if(posInPercent.y) { vActualPos.y = (int)(pParent.getPosition().y + pParent.getSize().y * ((float)vPos.y / 100.0f)); }
        }
        if(originInPercent.x)  { vActualPos.x -= (vActualSize.x * vOrigin.x) / 100; }
        else                   { vActualPos.x -= vOrigin.x; }

        if(originInPercent.y)  { vActualPos.y -= (vActualSize.y * vOrigin.y) / 100; }
        else                   { vActualPos.y -= vOrigin.y; }
        

        updateOnPosChange();
        updateMatrix();
        updateBoundingBox();
        for(int i = 0; i < numElements(); i++) { vElements.get(i).reposition(); }
        for(int i = 0; i < numChildren(); i++) { vChildren.get(i).reposition(); }
    }

    public void resize()
    {
        vActualSize.set(vSize);
        if(pParent != null)
        {
            if(sizeInPercent.x) { vActualSize.x = (int)((float)(pParent.getSize().x) * ((float)vSize.x / 100.0f)); }
            if(sizeInPercent.y) { vActualSize.y = (int)((float)(pParent.getSize().y) * ((float)vSize.y / 100.0f)); }

            if(vMinSize.x != 0)
            {
                float minsizex = minSizeInPercent.x ? pParent.getSize().x * ((float)vMinSize.x / 100.0f) : vMinSize.x;
                if(vMinSize.x < 0)
                    minsizex = pParent.getSize().x + minsizex;

                if(vActualSize.x < minsizex) { vActualSize.x = (int)minsizex; }
            }
            if(vMinSize.y != 0)
            {
                float minsizey = minSizeInPercent.y ? pParent.getSize().y * ((float)vMinSize.y / 100.0f) : vMinSize.y;
                if(vMinSize.y < 0)
                    minsizey = pParent.getSize().y + minsizey;

                if(vActualSize.y < minsizey) { vActualSize.y = (int)minsizey; }
            }

            if(vMaxSize.x != 0)
            {
                float maxsizex = maxSizeInPercent.x ? pParent.getSize().x * ((float)vMaxSize.x / 100.0f) : vMaxSize.x;
                if(vMaxSize.x < 0)
                    maxsizex = pParent.getSize().x + maxsizex;

                if(vActualSize.x > maxsizex) { vActualSize.x = (int)maxsizex; }            
            }
            if(vMaxSize.y != 0)
            {
                float maxsizey = maxSizeInPercent.y ? pParent.getSize().y * ((float)vMaxSize.y / 100.0f) : vMaxSize.y;
                if(vMaxSize.y < 0)
                    maxsizey = pParent.getSize().y + maxsizey;

                if(vActualSize.y > maxsizey) { vActualSize.y = (int)maxsizey; }
            }
        }
        vActualSize.add(vMargin);

        updateOnSizeChange(); 
        updateMatrix();
        updateBoundingBox();
        for(int i = 0; i < numElements(); i++) { vElements.get(i).resize(); }
        for(int i = 0; i < numChildren(); i++) { vChildren.get(i).resize(); }
    }


    public void destroyChildren()
    {
        for(int i = 0; i < this.numChildren(); i++)
        {
            this.vChildren.get(i).destroyChildren();
            //Gum::_delete(this.vChildren[i]);
        }
        this.vChildren.clear();
    }

    public void renderextra() 
    {
        renderchildren();
    };
    public void updateextra() {};


    public final void render()
    { 
        if(bIsHidden)
            return;
        
        renderextra(); 
    }

    public final void update()
    { 
        if(bIsHidden) 
            return;

        updatechildren(); 
        if(!bHasClickedSomething)
        {
            if(pHoverCallback != null || iHoverCursorShape != Mouse.GUM_CURSOR_DEFAULT || pEnterCallback != null || pLeaveCallback != null)
            {
                if(/*!Mouse.isActiveHovering() &&*/ isMouseInside())
                {
                    Mouse.setActiveHovering(true);
                    Window.CurrentlyBoundWindow.getMouse().setCursor(iHoverCursorShape);

                    if(pEnterCallback != null && bIsHovering == false)
                        pEnterCallback.run(this);
                    bIsHovering = true;

                    if(pHoverCallback != null)
                        pHoverCallback.run(this);
                }
                else
                {
                    if(pLeaveCallback != null && bIsHovering == true)
                        pLeaveCallback.run(this);

                    bIsHovering = false;
                }
            }
            if(pClickCallback != null && isClicked())
                pClickCallback.run(this);
        }
        updateextra();
    }
    public final void renderchildren() 
    { 
        for(int i = 0; i < numElements(); i++) { vElements.get(i).render(); }
        if(!bChildrenHidden)
        {
            for(int i = 0; i < numChildren(); i++) { vChildren.get(i).render(); } 
        }
    }

    public final void updatechildren() 
    { 
        if(bUpdateFromFirstToLast)
        {
            if(!bChildrenHidden)
            {
                for(int i = 0; i < numChildren(); i++) { vChildren.get(i).update(); } 
            }
            for(int i = 0; i < numElements(); i++) { vElements.get(i).update(); }
        }
        else
        {
            for(int i = numElements(); i --> 0;) { vElements.get(i).update();  }
            if(!bChildrenHidden)
            {
                for(int i = numChildren(); i --> 0;) { vChildren.get(i).update(); } 
            }
        }
    }

    public void updateBoundingBox() { updateBoundingBox(false); }
    public void updateBoundingBox(boolean override)
    {
        if(bKeepTrackOfBoundingBox || override)
        {
            bbox2i bbox = new bbox2i(getRelativePosition(), vActualSize);

            for(int i = 0; i < numChildren(); i++)
            {
                RenderGUI child = vChildren.get(i);
                child.updateBoundingBox(true);
                bbox2i childBbox = child.getBoundingBox();
                
                if(child.getRelativePosition().x + child.getSize().x > bbox.size.x) { bbox.size.x = child.getRelativePosition().x + child.getSize().x; }
                if(child.getRelativePosition().y + child.getSize().y > bbox.size.y) { bbox.size.y = child.getRelativePosition().y + child.getSize().y; }
                if(childBbox.size.x > bbox.size.x) { bbox.size.x = childBbox.size.x; }
                if(childBbox.size.y > bbox.size.y) { bbox.size.y = childBbox.size.y; }


                if(child.getRelativePosition().x < bbox.pos.x) { bbox.pos.x = child.getRelativePosition().x; }
                if(child.getRelativePosition().y < bbox.pos.y) { bbox.pos.y = child.getRelativePosition().y; }
                if(childBbox.pos.x < bbox.pos.x) { bbox.pos.x = childBbox.pos.x; }
                if(childBbox.pos.y < bbox.pos.y) { bbox.pos.y = childBbox.pos.y; }
            }

            for(int i = 0; i < numElements(); i++)
            {
                RenderGUI child = vElements.get(i);
                child.updateBoundingBox(true);
                bbox2i childBbox = child.getBoundingBox();

                if(child.getRelativePosition().x + child.getSize().x > bbox.size.x) { bbox.size.x = child.getRelativePosition().x + child.getSize().x; }
                if(child.getRelativePosition().y + child.getSize().y > bbox.size.y) { bbox.size.y = child.getRelativePosition().y + child.getSize().y; }
                if(childBbox.size.x > bbox.size.x) { bbox.size.x = childBbox.size.x; }
                if(childBbox.size.y > bbox.size.y) { bbox.size.y = childBbox.size.y; }


                if(child.getRelativePosition().x < bbox.pos.x) { bbox.pos.x = child.getRelativePosition().x; }
                if(child.getRelativePosition().y < bbox.pos.y) { bbox.pos.y = child.getRelativePosition().y; }
                if(childBbox.pos.x < bbox.pos.x) { bbox.pos.x = childBbox.pos.x; }
                if(childBbox.pos.y < bbox.pos.y) { bbox.pos.y = childBbox.pos.y; }
            }

            this.bBoundingBox = bbox;
        }
    }

    public void updateMatrix()
    {
        mat4 model = new mat4();
        ivec2 modelPos = ivec2.add(vActualPos, ivec2.mul(vActualSize, 0.5f));
        modelPos.y = Framebuffer.CurrentlyBoundFramebuffer.getSize().y - modelPos.y;
        model.translate(new vec3(modelPos.x, modelPos.y, 0.0f));
        model.scale(new vec3(vActualSize.x * 0.5f, vActualSize.y * 0.5f, 1.0f));
        model.rotate(new vec3(0, 0, fRotation));
        model.transpose();
        
        mTransformationMatrix = model;
    }


    public void increasePosition(ivec2 pos)             { this.setPosition(ivec2.add(this.vPos, pos)); }
    public void increaseMargin(ivec2 mar)               { this.setMargin(ivec2.add(this.vMargin, mar)); }
    public void removeChild(int index)                  { vChildren.remove(index); }
    public void removeChild(RenderGUI child)            { vChildren.remove(child); }
    public void addAttribute(String attr, String value) { this.mAttributes.put(attr, value); }
    public boolean collidesWith(RenderGUI gui)          { return Toolbox.checkBoxIntersection(getBoundingBox(), gui.getBoundingBox()); }
    public boolean collidesWithSimple(RenderGUI gui)    { return Toolbox.checkBoxIntersection(new bbox2i(getPosition(), getSize()), new bbox2i(gui.getPosition(), gui.getSize())); }


    public boolean isMouseInsideSkipChildren() { return isMouseInsideSkipChildren(new ivec2(0,0)); }
    public boolean isMouseInsideSkipChildren(ivec2 offset) 
    {
        return Toolbox.checkPointInBox(Window.CurrentlyBoundWindow.getMouse().getPosition(), new bbox2i(ivec2.add(getPosition(), offset), getSize()));
    }

    public boolean isMouseInside() { return isMouseInside(new ivec2(0,0)); }
    public boolean isMouseInside(ivec2 offset) 
    {
        if(bIsHidden)
            return false;
        boolean isinside = isMouseInsideSkipChildren(offset);
        if(!isinside && !bChildrenHidden)
            for(int i = 0; i < numChildren(); i++)
                if(vChildren.get(i).isMouseInside())
                    return true;
        
        return isinside; 
    }

    public ivec2 getUserDefinedPosition() { return this.vPos; }
    public bbox2i getBoundingBox()
    {
        return this.bBoundingBox;
    }

    public ivec2 getRelativePosition()
    {
        ivec2 retPos = new ivec2(vPos);
        if(pParent != null)
        {
            if(posInPercent.x) { retPos.x = (int)(pParent.getSize().x * ((float)vPos.x / 100.0f)); }
            if(posInPercent.y) { retPos.y = (int)(pParent.getSize().y * ((float)vPos.y / 100.0f)); }
        }
        retPos.sub(vOrigin);
        return retPos;
    }

    public RenderGUI findChildByID(String id)
    {
        if(getID().equals(id))
            return this;
        
        for(RenderGUI child : vChildren)
        {
            RenderGUI res = child.findChildByID(id);
            if(res != null)
                return res;
        }

        return null;
    }


    //
    // Setter
    //
    public void onEnter(GUICallback callback)                   { this.pEnterCallback = callback; }
    public void onLeave(GUICallback callback)                   { this.pLeaveCallback = callback; }
    public void onClick(GUICallback callback)                   { this.pClickCallback = callback; }
    public void onHover(GUICallback callback, int shape)        { this.pHoverCallback = callback; this.iHoverCursorShape = shape; }
    public void setOrigin(ivec2 orig)                           { this.vOrigin.set(orig); reposition(); }
    public void setPosition(ivec2 pos)                          { this.vPos.set(pos); reposition(); }
    public void setPositionX(int pos)                           { this.vPos.x = pos; reposition(); }
    public void setPositionY(int pos)                           { this.vPos.y = pos; reposition(); }
    public void setSize(ivec2 size)                             { this.vSize.set(size); resize(); }
    public void setMaxSize(ivec2 size)                          { this.vMaxSize.set(size); resize(); }
    public void setMinSize(ivec2 size)                          { this.vMinSize.set(size); resize(); }
    public void setMargin(ivec2 margin)                         { this.vMargin.set(margin); resize(); }
    public void setRotation(float rot)                          { this.fRotation = rot; updateMatrix(); }
    public void setPositionInPercent(boolean x, boolean y)      { this.posInPercent = new bvec2(x, y); reposition(); }
    public void setOriginInPercent(boolean x, boolean y)        { this.originInPercent = new bvec2(x, y); reposition(); }
    public void setSizeInPercent(boolean x, boolean y)          { this.sizeInPercent = new bvec2(x, y); resize(); }
    public void setMaxSizeInPercent(boolean x, boolean y)       { this.maxSizeInPercent = new bvec2(x, y); resize(); }
    public void setMinSizeInPercent(boolean x, boolean y)       { this.minSizeInPercent = new bvec2(x, y); resize(); }
    public void setParent(RenderGUI parent)                     { this.pParent = parent; }
    public void setType(String type)                            { this.sType = type; }
    public void setID(String id)                                { this.sID = id; }
    public void setTitle(String title)                          { this.sTitle = title; updateOnTitleChange(); }
    public void setValuePtr(String value)                       { this.value = value; }
    public void setColor(vec4 col)                              { this.v4Color = col; updateOnColorChange(); }
    public void hide(boolean hidden)                            { this.bIsHidden = hidden; }
    public void hideChildren(boolean hidden)                    { this.bChildrenHidden = hidden; }
    public void setToolTip(String tooltip)                      { this.sToolTip = tooltip; }
    public void setCornerRadius(vec4 radius)                    { this.v4CornerRadius = radius; updateOnCornerRadiusChange(); }
    public void shouldKeepTrackOfBoundingbox(boolean keeptrack) { this.bKeepTrackOfBoundingBox = keeptrack; }
    public static void clickedSomething(boolean hasclicked)     { bHasClickedSomething = hasclicked; }


    //
    // Getter
    //
    public ivec2 getRelativeMousePosition()                   { return ivec2.sub(vActualPos, Window.CurrentlyBoundWindow.getMouse().getPosition()); }
    public ivec2 getOrigin()                                  { return this.vOrigin; }
    public ivec2 getMargin()                                  { return this.vMargin; }
    public ivec2 getPosition()                                { return this.vActualPos; }
    public ivec2 getSize()                                    { return this.vActualSize; }
    public float getRotation()                                { return this.fRotation; }
    public mat4 getTransformation()                           { return this.mTransformationMatrix; }
    public bvec2 isPositionInPercent()                        { return this.posInPercent; }
    public bvec2 isSizeInPercent()                            { return this.sizeInPercent; }
    public bvec2 isMaxSizeInPercent()                         { return this.maxSizeInPercent; }
    public bvec2 isMinSizeInPercent()                         { return this.minSizeInPercent; }
    public RenderGUI getChild(int index)                      { return this.vChildren.get(index); }
    //public ContextMenuEntry getContextMenuEntry(int& index) { return this.vContextMenuEntries[index]; }
    public int numChildren()                                  { return this.vChildren.size(); }
    public int numElements()                                  { return this.vElements.size(); }
    //public int numContextMenuEntries()                      { return this.vContextMenuEntries.size(); }
    public RenderGUI getParent()                              { return this.pParent; }
    public String getType()                                   { return this.sType; }
    public String getID()                                     { return this.sID; }
    public String getTitle()                                  { return this.sTitle; }
    public String getValuePtr()                               { return this.value; }
    public String getAttribute(String attr)                   { return this.mAttributes.get(attr); }
    public float getAlphaOverride()                           { return this.fAlphaOverride; }
    public boolean isHidden()                                 { return this.bIsHidden; }
    public boolean areChildrenHidden()                        { return this.bChildrenHidden; }
    public boolean hasChild(RenderGUI gui)                    { for(RenderGUI child : vChildren) { if(child == gui) return true; } return false;}
    public ArrayList<RenderGUI> getChildren()                 { return vChildren; }

    public vec4 getCornerRadius()                             
    { 
        if(this.v4CornerRadius == null)
            return GUI.getTheme().cornerRadius;
        return this.v4CornerRadius; 
    }
    public vec4 getColor(vec4 fallback)
    { 
        if(this.v4Color == null)
            return fallback;
        return this.v4Color; 
    }

    public boolean isClicked()                                   
    { 
        if(bHasClickedSomething)
            return false;
        boolean isclicked = Window.CurrentlyBoundWindow.getMouse().hasLeftRelease() && 
                            Toolbox.checkPointInBox(Window.CurrentlyBoundWindow.getMouse().getLeftClickPosition(), new bbox2i(getPosition(), getSize())) &&
                            Toolbox.checkPointInBox(Window.CurrentlyBoundWindow.getMouse().getPosition(), new bbox2i(getPosition(), getSize()));
        if(isclicked)
            bHasClickedSomething = true;
        return isclicked;
    }

    public boolean hasClickedInside()                            
    { 
        if(bHasClickedSomething)
            return false;
        boolean isclicked = Window.CurrentlyBoundWindow.getMouse().hasLeftClickStart() && 
                                Toolbox.checkPointInBox(Window.CurrentlyBoundWindow.getMouse().getLeftClickPosition(), new bbox2i(getPosition(), getSize())); 
        if(isclicked)
            bHasClickedSomething = true;
        return isclicked;
    }

    public boolean isHoldingLeftClick()
    {
        if(bHasClickedSomething)
            return false;
        boolean isclicked = Window.CurrentlyBoundWindow.getMouse().hasLeftClick() && 
                                Toolbox.checkPointInBox(Window.CurrentlyBoundWindow.getMouse().getLeftClickPosition(), new bbox2i(getPosition(), getSize())); 
        if(isclicked)
            bHasClickedSomething = true;
        return isclicked;
    }

    public static boolean somethingHasBeenClicked()                           { return bHasClickedSomething; }

}