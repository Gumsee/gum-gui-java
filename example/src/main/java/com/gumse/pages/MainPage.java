package com.gumse.pages;

import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.textures.Texture;


public class MainPage extends RenderGUI
{
    public MainPage()
    {
        this.vSize = new ivec2(100,100);
        FontManager fonts = FontManager.getInstance();
        Box testBox = new Box(new ivec2(30, 30), new ivec2(50, 50));
        testBox.setSizeInPercent(true, true);
        testBox.setColor(new vec4(1.0f,1.0f,0.0f,1.0f));
        
        Texture hehe = new Texture();
        hehe.load("hehe.jpg");
        testBox.setTexture(hehe);
        addElement(testBox);


        TextBox textBox = new TextBox("Some test text", fonts.getDefaultFont(), new ivec2(100, 100), new ivec2(200, 40));
        addElement(textBox);

        this.setSizeInPercent(true, true);
        reposition();
        resize();
    }
}
