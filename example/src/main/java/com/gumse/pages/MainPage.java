package com.gumse.pages;

import com.gumse.gui.Basics.Scroller;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.textures.Texture;
import com.gumse.tools.Debug;
import com.gumse.tools.FPS;

public class MainPage extends RenderGUI
{
    private TextBox fpsBox;

    public MainPage()
    {
        this.vSize = new ivec2(100,100);
        FontManager fonts = FontManager.getInstance();

        Scroller mainScroller = new Scroller(new ivec2(0,0), new ivec2(100, 100));
        mainScroller.setSizeInPercent(true, true);
        addElement(mainScroller);

        Box testBox = new Box(new ivec2(30, 30), new ivec2(100, 100));       
        testBox.setSizeInPercent(false, false);
        testBox.invertTexcoordY(true);
        testBox.setColor(new vec4(1.0f,1.0f,1.0f,1.0f));
        testBox.setBorderColor(new vec4(1.0f, 0.0f, 0.0f, 1.0f));
        testBox.setCornerRadius(new vec4(10, 5, 0, 40));
        testBox.setBorderThickness(2.0f);
        
        Texture hehe = new Texture();
        hehe.load("hehe.jpg");
        testBox.setTexture(hehe);
        mainScroller.addGUI(testBox);


        fpsBox = new TextBox("FPS: ", fonts.getDefaultFont(), new ivec2(210, 100), new ivec2(200, 40));
        fpsBox.setAlignment(TextBox.Alignment.LEFT);
        mainScroller.addGUI(fpsBox);

        TextBox textBox = new TextBox("Some test text", fonts.getDefaultFont(), new ivec2(100, 650), new ivec2(200, 40));
        mainScroller.addGUI(textBox);

        this.setSizeInPercent(true, true);
        reposition();
        resize();
    }

    public void update()
    {
        fpsBox.setString("FPS: " + FPS.getFPS());
        updatechildren();
    }
}
