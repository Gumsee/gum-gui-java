package com.gumse.gui.HierarchyList;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.gumse.PostProcessing.Framebuffer;
import com.gumse.gui.GUI;
import com.gumse.gui.GUIShader;
import com.gumse.gui.Basics.TextField;
import com.gumse.gui.Basics.TextField.TextFieldInputCallback;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.*;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;
import com.gumse.system.Window;
import com.gumse.system.io.Mouse;
import com.gumse.tools.Toolbox;

public class HierarchyListEntry extends RenderGUI
{
    public interface HierarchyListCallback {
        void run();
    }

    private static final int INDENT_SIZE = 10;
    private TextField title;
    private boolean bIsOpen;
    private boolean bHasChildEntries;
    private HierarchyListCallback pCallback;
    private int indent = 0;
    private static VertexArrayObject pArrowVAO; 
    private mat4 m4ArrowMatrix;
    private HierarchyList pParentList;

    private static void initVAO()
    {
        if(pArrowVAO == null)
        {
            pArrowVAO = new VertexArrayObject();
            VertexBufferObject pArrowVBO = new VertexBufferObject();
            pArrowVBO.setData(new ArrayList<Float>(Arrays.asList(new Float[] { 
                 0.5f,  0.0f, 0.0f,
                -0.5f,  0.0f, 0.0f,
                 0.0f, -0.5f, 0.0f,
            })));
            pArrowVAO.addAttribute(pArrowVBO, 0, 3, GL11.GL_FLOAT, 0, 0);
        }
    }

    private void repositionEntries() { repositionEntries(false); }
    private void repositionEntries(boolean checkforselectedentry)
    {
        if(checkforselectedentry)
            pParentList.selectEntry(null);

        for(int i = 0; i < numChildren(); i++)
        {
            HierarchyListEntry entry = (HierarchyListEntry)getChild(i);
            entry.repositionEntries(!bIsOpen);
            int ypos = (i+1) * entry.getSize().y;
            if(i > 0)
            {
                ypos += ((HierarchyListEntry)getChild(i - 1)).getHeight();
            }

            entry.setPosition(new ivec2(INDENT_SIZE, ypos));
        }
    }


    //
    // List Entry
    //
    public HierarchyListEntry(String name, HierarchyList parentlist, HierarchyListCallback callback)
    {
        this.bIsOpen = false;
        this.bHasChildEntries = false;
        this.pCallback = callback;
        this.sType = "HierarchyListEntry";
        this.pParentList = parentlist;

        this.title = new TextField(name, FontManager.getInstance().getDefaultFont(), new ivec2(20,0), new ivec2(100, 100));
        this.title.setSizeInPercent(true, true);
        this.title.shouldActivateOnDoubleclick(true);
        this.title.getBox().getBox().hide(true);
        this.title.getBox().getText().setCharacterHeight(20);
        //this.title.getBox().setTextOffset(new ivec2(0, 3));
        this.title.setCursorShapeOnHover(Mouse.GUM_CURSOR_DEFAULT);

        addElement(title);
        initVAO();
        updateOnPosChange();

        setSize(new ivec2(100, 30));
        setSizeInPercent(true, false);

        resize();
        reposition();
    }

    public void cleanup()
    {
        //_delete(title);
        destroyChildren();
    }

    public void update()
    {
        if(bIsOpen)
            updatechildren();
        
        Mouse mouse = Window.CurrentlyBoundWindow.getMouse();
        if(!title.isEditing() && isMouseInside() && !RenderGUI.somethingHasBeenClicked())
        {
            if(bHasChildEntries)
            {
                if(Toolbox.checkPointInBox(mouse.getPosition(), new bbox2i(vActualPos, new ivec2(20, 30))))
                {
                    Mouse.setActiveHovering(true);
                    mouse.setCursor(Mouse.GUM_CURSOR_HAND);
                    if(mouse.hasLeftRelease() && Toolbox.checkPointInBox(mouse.getLeftClickPosition(), new bbox2i(vActualPos, new ivec2(20, 30))))
                    {
                        openState(!bIsOpen);
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
                        pCallback.run();
                }
                if(mouse.hasLeftDoubleClick())
                {
                    pParentList.selectEntry(null);
                }
            }
        }
        title.update();
    }

    protected void updateOnPosChange()
    {
        vec3 rot = new vec3();
        if(!bIsOpen)
            rot.set(new vec3(0, 0, 90));

        mat4 model = new mat4();
        model.translate(new vec3(vActualPos.x + 5, Framebuffer.CurrentlyBoundFramebuffer.getSize().y - vActualPos.y - 15, 0));
        model.scale(new vec3(15));
        model.rotate(rot);
        model.transpose();
        
        m4ArrowMatrix = model;
    }

    public void render()
    {
        if(bHasChildEntries)
        {
            GUIShader.getShaderProgram().use();
            GUIShader.getShaderProgram().loadUniform("orthomat", Framebuffer.CurrentlyBoundFramebuffer.getScreenMatrix());
            GUIShader.getShaderProgram().loadUniform("transmat", m4ArrowMatrix);
            GUIShader.getShaderProgram().loadUniform("Uppercolor", new vec4(0.76f, 0.76f, 0.76f,1.0f));
            GUIShader.getShaderProgram().loadUniform("borderThickness", 0.0f);
            GUIShader.getShaderProgram().loadUniform("hasTexture", false);
            pArrowVAO.bind();
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
            pArrowVAO.unbind();
            GUIShader.getShaderProgram().unuse();
        }

        title.render();
        
        if(bIsOpen)
            renderchildren();
    }

    public void addEntry(HierarchyListEntry entry)
    {
        entry.setIndent(indent + INDENT_SIZE);
        addGUI(entry);
        repositionEntries();
        bHasChildEntries = true;
    }

    public void updateBoundingBox(boolean override)
    { 
        if(bKeepTrackOfBoundingBox || override)
        {
            bBoundingBox.pos = vActualPos;
            bBoundingBox.size = getSize();

            if(bIsOpen)
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
        if(bIsOpen)
            return getSize().y * (numChildren());
        return 0;
    }

    public void openState(boolean opn)
    {
        bIsOpen = opn;
        updateOnPosChange();
        if(pParent != null && pParent.getType() == "HierarchyListEntry")
            ((HierarchyListEntry)pParent).repositionEntries();
    }

    public void openAll()
    {
        bIsOpen = true;
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
        this.indent = indent;
        //this.title.setPosition(ivec2(indent, title.getPosition().y));
    }
    public void setRenameCallback(TextFieldInputCallback callback)
    {
        this.title.setCallback(callback);
    }

    public static void cleanupAll()
    {
        //_delete(pArrowVAO);
    }
};