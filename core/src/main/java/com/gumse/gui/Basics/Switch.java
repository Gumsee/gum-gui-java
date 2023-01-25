package com.gumse.gui.Basics;

import java.util.Arrays;

import com.gumse.gui.GUI;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
<<<<<<< HEAD
import com.gumse.gui.XML.XMLGUI.XMLGUICreator;
=======
import com.gumse.gui.Primitives.Shape;
>>>>>>> 706c4fd4c1829b0e9beacfa0f6afede3d6403133
import com.gumse.maths.*;
import com.gumse.system.filesystem.XML.XMLNode;
import com.gumse.system.io.Mouse;

public class Switch extends RenderGUI
{
    public enum SwitchShape
    {
        SQUARE,
        CIRCLE,
        CHECK
    };

    public interface OnSwitchTicked { public void run(boolean ticked); }


    private Box pBackground;
    private Box pTickbox;
    private SwitchShape iShape;
    private Shape pTickShape;
    private boolean bIsTicked;
    private OnSwitchTicked pOnTickedCallback;

    private static final float thickness = 0.3f;
    private static final Float[] afTickVertices = new Float[] { 
        (1.0f             + 1.0f) / 2.0f, (- 1.0f              + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (- 1.0f              + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f,
        (1.0f             + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f,
        (1.0f             + 1.0f) / 2.0f, (- 1.0f              + 1.0f) / 2.0f, 0.0f,
        (     - thickness + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f,
        (     - thickness + 1.0f) / 2.0f, (- 1.0f              + 1.0f) / 2.0f, 0.0f,
        (     - thickness + 1.0f) / 2.0f, (- 1.0f              + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (- 1.0f              + 1.0f) / 2.0f, 0.0f,
        (1.0f             + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (  1.0f              + 1.0f) / 2.0f, 0.0f,
        (1.0f - thickness + 1.0f) / 2.0f, (  1.0f              + 1.0f) / 2.0f, 0.0f,
        (1.0f             + 1.0f) / 2.0f, (  1.0f              + 1.0f) / 2.0f, 0.0f,
        (1.0f             + 1.0f) / 2.0f, (-(1.0f - thickness) + 1.0f) / 2.0f, 0.0f
    };

    public Switch(ivec2 pos, ivec2 size, float radius) 
    {
        this.sType = "Switch";
        this.vPos.set(pos);
        this.vSize.set(size);
        this.iShape = SwitchShape.CHECK;
        this.bIsTicked = false;
        this.pOnTickedCallback = null;

        pBackground = new Box(new ivec2(0,0), new ivec2(100, 100));
        pBackground.setCornerRadius(new vec4(radius));
        pBackground.setSizeInPercent(true, true);
        pBackground.setColor(getColor(GUI.getTheme().secondaryColor));
        addElement(pBackground);


        pTickbox = new Box(new ivec2(50,50), new ivec2(55, 55));
        pTickbox.setCornerRadius(new vec4(radius * 0.8f));
        pTickbox.setPositionInPercent(true, true);
        pTickbox.setSizeInPercent(true, true);
        pTickbox.setOrigin(new ivec2(50, 50));
        pTickbox.setOriginInPercent(true, true);
        pTickbox.setColor(GUI.getTheme().accentColor);
        //pTickbox.setColor(vec4(Gum::Maths::HSVToRGB(vec3(rand() % 360, 100, 70)),1.0));
        addElement(pTickbox);

        pTickShape = new Shape("switchtick", new ivec2(0, 5), new ivec2(75, 75), Arrays.asList(afTickVertices));
        pTickShape.setSizeInPercent(true, true);
        pTickShape.setOriginInPercent(true, true);
        pTickShape.setOrigin(new ivec2(5, 100));
        pTickShape.setRotation(-45.0f);
        addElement(pTickShape);

        onHover(null, Mouse.GUM_CURSOR_HAND);
        onClick(new GUICallback() {
            @Override public void run(RenderGUI gui) 
            {
                bIsTicked = !bIsTicked;
                if(pOnTickedCallback != null)
                    pOnTickedCallback.run(bIsTicked);
            }
        });

        resize();
        reposition();
    }

    public void cleanup() {};

    @Override
    protected void updateOnThemeChange() 
    {
        pBackground.setColor(getColor(GUI.getTheme().secondaryColor));
        pTickbox.setColor(GUI.getTheme().accentColor);
    }

    @Override
    public void renderextra() 
    {
        pBackground.render();
        if(bIsTicked)
        {
            switch(iShape)
            {
                case CHECK: pTickShape.render(); break;
                default:    pTickbox.render();   break;
            }
        }
    }


    //
    // Setter
    //
    public void tick(boolean state)   { bIsTicked = state; }
    public void setShape(SwitchShape shape) 
    { 
        this.iShape = shape; 
        switch(iShape)
        {
            case CIRCLE: pBackground.renderAsCircle(true);  pTickbox.renderAsCircle(true); break;
            case SQUARE: pBackground.renderAsCircle(false); pTickbox.renderAsCircle(false); break;
            default: break;
        }
    }
    public void onTick(OnSwitchTicked callback)
    {
        this.pOnTickedCallback = callback;
    }


    //
    // Getter
    //
    public boolean isTicked()       { return bIsTicked; }


    public static XMLGUICreator createFromXMLNode() 
    {
        return (XMLNode node) -> { 
            float borderRadius = node.getFloatAttribute("border-radius", 0.0f);
            return new Switch(new ivec2(0,0), new ivec2(1,1), borderRadius);
        };
    };
};