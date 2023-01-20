package com.gumse.gui.Primitives;

import java.util.Arrays;

import com.gumse.maths.ivec2;

public class Cross extends RenderGUI
{
    private final static float thickness = 0.12f;
    private final static Float[] crossVertices = new Float[] { 
       ( 1.0f + 1.0f) / 2.0f, (-thickness + 1.0f) / 2.0f, 0.0f,
       (-1.0f + 1.0f) / 2.0f, ( thickness + 1.0f) / 2.0f, 0.0f,
       ( 1.0f + 1.0f) / 2.0f, ( thickness + 1.0f) / 2.0f, 0.0f, 
       ( 1.0f + 1.0f) / 2.0f, (-thickness + 1.0f) / 2.0f, 0.0f,
       (-1.0f + 1.0f) / 2.0f, ( thickness + 1.0f) / 2.0f, 0.0f,
       (-1.0f + 1.0f) / 2.0f, (-thickness + 1.0f) / 2.0f, 0.0f, 

       (-thickness + 1.0f) / 2.0f, ( 1.0f + 1.0f) / 2.0f, 0.0f,
       ( thickness + 1.0f) / 2.0f, (-1.0f + 1.0f) / 2.0f, 0.0f,
       ( thickness + 1.0f) / 2.0f, ( 1.0f + 1.0f) / 2.0f, 0.0f, 
       (-thickness + 1.0f) / 2.0f, ( 1.0f + 1.0f) / 2.0f, 0.0f,
       ( thickness + 1.0f) / 2.0f, (-1.0f + 1.0f) / 2.0f, 0.0f,
       (-thickness + 1.0f) / 2.0f, (-1.0f + 1.0f) / 2.0f, 0.0f, 
    };

    private Shape pShape;


    public Cross(ivec2 pos, ivec2 size)
    {
        this.vPos.set(pos);
        this.vSize.set(size);
        this.pShape = new Shape("cross", new ivec2(0,0), new ivec2(100, 100), Arrays.asList(crossVertices));
        pShape.setSizeInPercent(true, true);
        pShape.setRotation(45.0f);
        addElement(pShape);

        
        resize();
        reposition();
    }
}