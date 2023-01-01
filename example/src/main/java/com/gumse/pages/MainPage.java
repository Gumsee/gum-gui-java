package com.gumse.pages;

import com.gumse.gui.Basics.Button;
import com.gumse.gui.Basics.Dropdown;
import com.gumse.gui.Basics.Graph;
import com.gumse.gui.Basics.Radiobutton;
import com.gumse.gui.Basics.Dropdown.DropdownEntryCallback;
import com.gumse.gui.Basics.TextBox.Alignment;
import com.gumse.gui.Basics.Scroller;
import com.gumse.gui.Basics.Slider;
import com.gumse.gui.Basics.TextBox;
import com.gumse.gui.Font.Font;
import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec4;
import com.gumse.textures.Texture;
import com.gumse.tools.Debug;
import com.gumse.tools.FPS;


/**
 * TODO:
 * 
 * RenderGUI Colortheme override checker (v4Color > theme)
 */

public class MainPage extends RenderGUI
{
    private TextBox fpsBox;
    private Graph testGraph;

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
        testBox.setBorderThickness(2);
        
        Texture hehe = new Texture();
        hehe.load("textures/hehe.jpg", MainPage.class);
        testBox.setTexture(hehe);
        mainScroller.addGUI(testBox);


        fpsBox = new TextBox("FPS: ", fonts.getDefaultFont(), new ivec2(210, 100), new ivec2(200, 40));
        fpsBox.setAlignment(TextBox.Alignment.LEFT);
        mainScroller.addGUI(fpsBox);

        Button themeButton = new Button(new ivec2(210, 150), new ivec2(200, 40), "Switch Theme", fonts.getDefaultFont());
        mainScroller.addGUI(themeButton);

        Slider testSlider = new Slider(new ivec2(30, 200), 200, "Slider1", 0);
        testSlider.setViewMultiplier(666);
        mainScroller.addGUI(testSlider);

        Slider testInfSlider = new Slider(new ivec2(30, 240), 200, "Infinite", 2);
        testInfSlider.setInfinite(true);
        testInfSlider.setUnit("cm");
        mainScroller.addGUI(testInfSlider);

        Text sliderInfoText = new Text("Shift: Slow down\nLControl: Speed up", fonts.getDefaultFont(), new ivec2(30, 270), 0);
        sliderInfoText.setCharacterHeight(20);
        mainScroller.addGUI(sliderInfoText);


        Dropdown testDropdown = new Dropdown("Dropdown", fonts.getDefaultFont(), new ivec2(30, 400), new ivec2(200, 30), 20);
        DropdownEntryCallback dropdowncallback = new DropdownEntryCallback() { 
            @Override public void run(String str) { 
                Debug.info(str); 
            } 
        };
        testDropdown.addEntry("Entry1", dropdowncallback, false);
        testDropdown.addEntry("Entry2", dropdowncallback, false);
        testDropdown.addEntry("Entry3", dropdowncallback, false);
        testDropdown.addEntry("Entry4", dropdowncallback, false);
        mainScroller.addGUI(testDropdown);


        //testGraph = new Graph("Some Graph", new ivec2(30, 500), new ivec2(90, 150));
        //testGraph.setSizeInPercent(true, false);

        String lipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque mauris elit, luctus id mollis a, posuere sed ante. Mauris a aliquet est. Integer egestas massa ac erat finibus iaculis.";

        TextBox textBox = new TextBox(lipsum, fonts.getDefaultFont(), new ivec2(30, 500), new ivec2(90, 150));
        textBox.setTextSize(25);
        textBox.setSizeInPercent(true, false);
        textBox.setAutoInsertLinebreaks(true);
        textBox.setAlignment(Alignment.LEFT);
        mainScroller.addGUI(textBox);

        Radiobutton radiobutton = new Radiobutton(new ivec2(30, 800), 20, 90, fonts.getDefaultFont(), new String[] {"Option 1", "Option 2" + lipsum, "Option 3"});
        radiobutton.setSizeInPercent(true, false);
        mainScroller.addGUI(radiobutton);

        //TextBox textBox = new TextBox("Some test text", fonts.getDefaultFont(), new ivec2(100, 650), new ivec2(200, 40));
        //mainScroller.addGUI(textBox);

        this.setSizeInPercent(true, true);
        reposition();
        resize();
    }

    private float f = 0.0f;

    public void update()
    {
        if(bIsHidden)
            return;
            
        fpsBox.setString("FPS: " + (int)FPS.getFPS());
        //testGraph.addData((float)Math.cos(f += 0.1f));
        //testGraph.addData(FPS.getFPS());
        updatechildren();
    }
}
