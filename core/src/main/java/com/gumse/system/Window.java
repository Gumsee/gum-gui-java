package com.gumse.system;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.gumse.system.io.Keyboard;
import com.gumse.system.io.Mouse;
import com.gumse.textures.Texture;
import com.gumse.tools.Toolbox;
import com.gumse.maths.*;

public class Window
{
    public static final int GUM_WINDOW_DEFAULTS =        0b000000000;
    public static final int GUM_WINDOW_RESIZABLE =       0b000000001;
    public static final int GUM_WINDOW_FULLSCREEN =      0b000000010;
    public static final int GUM_WINDOW_FLOATING =        0b000000100;
    public static final int GUM_WINDOW_BORDERLESS =      0b000001000;
    public static final int GUM_WINDOW_SIZE_IN_PERCENT = 0b000010000;
    public static final int GUM_WINDOW_VERTICAL_SYNC =   0b000100000;
    public static final int GUM_WINDOW_MAXIMIZED =       0b001000000;
    public static final int GUM_WINDOW_SHARECONTEXT =    0b010000000;
    public static final int GUM_WINDOW_TRAP_IN_PARENT =  0b100000000;


    private static long lWindowID;
    private Window pParentWindow;
    private ivec2 v2VisibleAreaSize;
    private ivec2 v2Size, v2Pos;
    private vec2 v2PixelSize;
    private mat4 m4ScreenMatrix;
    private float fAspectRatio, fAspectRatioWidthToHeight;
    private boolean bHasBorder, bHasVerticalSync, bHasScalingSnapped, bIsFloating, bIsMaximized, bIsMinimized, bIsResizable, bIsFullscreen, bIsHidden, bShouldClose, bKeepInsideParent;
    private String sTitle;
    private String sWindowClass = "Gumball";
    
    
    private Keyboard pKeyboard;
    private Mouse pMouse;

    public interface WindowResizePosCallback {
        void run(ivec2 val);
    }

    public interface WindowFocusCallback {
        void run(boolean hasfocus);
    }
    
    //Callbacks
    private WindowResizePosCallback movedCallback = null, resizedCallback = null;
    private WindowFocusCallback focusedCallback = null;

    private static vec4 v4DecorationSize;

    public static Window MainWindow = null;
    public static Window CurrentlyBoundWindow = null;
    public static boolean WINDOW_IS_ACTIVE_SCALING = false;


	public Window(String title, ivec2 windowsize, int properties, Window parentWindow)
	{
		this.pParentWindow = parentWindow;
		this.bKeepInsideParent = (properties & GUM_WINDOW_TRAP_IN_PARENT) > 0;
		this.v2Size = windowsize;
		this.sTitle = title;
		this.bIsHidden = false;
		this.bHasScalingSnapped = false;
		this.bIsMaximized = false;
		this.bShouldClose = false;
		this.bIsMinimized = false;

		if((properties & GUM_WINDOW_SIZE_IN_PERCENT) > 0)
		{
			v2Size.x = (int) (Display.vScreenSize.x * ((float)v2Size.x / 100.0f));
			v2Size.y = (int) (Display.vScreenSize.y * ((float)v2Size.y / 100.0f));
		}
		this.v2PixelSize = vec2.div(new vec2(1.0f), new vec2(v2Size));

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        //Create the window
        lWindowID = glfwCreateWindow(v2Size.x, v2Size.y, title, 0, 0);
        if(lWindowID == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        //glfwSetInputMode(lWindowID, GLFW_STICKY_KEYS, GLFW_TRUE);

		// Center window
        glfwSetWindowPos(lWindowID, (Display.vScreenSize.x - v2Size.x) / 2, (Display.vScreenSize.y - v2Size.y) / 2);
        
        //OpenGL Options
		glfwMakeContextCurrent(lWindowID);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        resetViewport();

		bind();
        glfwSwapInterval(1); // Enable v-sync
	    glfwShowWindow(lWindowID); // Make the window visible
        //glfwSetWindowUserPointer(lWindowID, this);



		setTitle(title);
		setVerticalSync((properties & GUM_WINDOW_VERTICAL_SYNC) > 0);
		showBorder     ((properties & GUM_WINDOW_BORDERLESS) == 0);
		makeResizable  ((properties & GUM_WINDOW_RESIZABLE) > 0);
		makeFloating   ((properties & GUM_WINDOW_FLOATING) > 0);
		makeFullscreen ((properties & GUM_WINDOW_FULLSCREEN) > 0);
		maximize       ((properties & GUM_WINDOW_MAXIMIZED) > 0);

		Texture icon22 = new Texture("icon22");
		icon22.dontCreateGLTexture();
		icon22.load("icons/icon22.png");

		Texture icon32 = new Texture("icon32");
		icon32.dontCreateGLTexture();
		icon32.load("icons/icon32.png");

		Texture icon48 = new Texture("icon48");
		icon48.dontCreateGLTexture();
		icon48.load("icons/icon48.png");

		Texture icon64 = new Texture("icon64");
		icon64.dontCreateGLTexture();
		icon64.load("icons/icon64.png");

		Texture icon128 = new Texture("icon128");
		icon128.dontCreateGLTexture();
		icon128.load("icons/icon128.png");
		
		setIcon(new ArrayList<Texture>(Arrays.asList(new Texture[] { icon22, icon32, icon48, icon64, icon128 })), true, new vec4(0.61f, 0.53f, 1.0f, 1.0f));

		if(MainWindow== null)
			MainWindow = this;


		fAspectRatio = (float)v2Size.y / (float)v2Size.x;
		fAspectRatioWidthToHeight = (float)v2Size.x / (float)v2Size.y;
		m4ScreenMatrix = mat4.ortho((float)v2Size.y, (float)v2Size.x, 0.0f, 0.0f, -100.0f, 100.0f);


		pKeyboard = new Keyboard(this);
		pMouse = new Mouse(this);


        Event.initNative(this);
	}

	public void cleanup()
	{
		//AllWindows.erase(std::find(AllWindows.begin(), AllWindows.end(), this));
		destroyNativeWindow();
		//pContext.cleanup();
		//pKeyboard.cleanup();
		//pMouse.cleanup();
	}


	public void resetViewport()
	{
		glViewport(0, 0, getSize().x, getSize().y);
		glScissor(0, 0, getSize().x, getSize().y);
	}

	public void update()
	{
		if(bIsResizable && !bHasBorder && this.pMouse != null)
		{;
			if(Toolbox.checkPointInBox(pMouse.getPosition(), new bbox2i(ivec2.sub(getSize(), new ivec2(20, 20)), getSize())))
			{
				//pMouse.setCursor(GUM_CURSOR_TOPLEFT_TO_BOTTOMRIGHT_RESIZE);
				if(pMouse.hasLeftClick() && !Mouse.isBusy())
				{
					if(!WINDOW_IS_ACTIVE_SCALING)
					{
						WINDOW_IS_ACTIVE_SCALING = true;
						bHasScalingSnapped = true;
						Mouse.setBusiness(true);
					}
				}
			}

			if(bHasScalingSnapped)
			{
				setSize(ivec2.add(getSize(), Mouse.getDelta()));
				if(getSize().x < 100) { setSize(new ivec2(100, v2Size.y)); }
				if(getSize().y < 100) { setSize(new ivec2(v2Size.x, 100)); }

				if(!pMouse.hasLeftClick())
				{
					WINDOW_IS_ACTIVE_SCALING = false;
					bHasScalingSnapped = false;
					Mouse.setBusiness(false);
				}
			}
		}
	}


	public void close()            	{ bShouldClose = true; }
	public void clear(int clearbits) 	{ glClear(clearbits); }
	public void bind()					
	{ 
		glfwMakeContextCurrent(lWindowID);
		CurrentlyBoundWindow = this;
        resetViewport();
	}
	public void unbind() { MainWindow.bind(); }
    
    public void handleEvent(Event event)
    {
        switch(event.type)
        {
			//case ResizeRequest:    break;
            case Event.GUM_EVENT_FOCUS_IN:         windowFocusedCallback(true); break;
            case Event.GUM_EVENT_FOCUS_OUT:        windowFocusedCallback(false); break;
            case Event.GUM_EVENT_WINDOW_REPOSITION: 
                if(event.data.windowpos != v2Pos)
                    windowMovedCallback(event.data.windowpos);
				break;

			case Event.GUM_EVENT_WINDOW_RESIZE:
                if(event.data.windowsize != v2Size)
                    windowResizedCallback(event.data.windowsize);
                break;

            case Event.GUM_EVENT_WINDOW_CLOSE:
				close();
                break;
        }
    }
    
	void windowResizedCallback(ivec2 size)
	{
		v2Size = size;
		fAspectRatio = (float)v2Size.y / (float)v2Size.x;
		fAspectRatioWidthToHeight = (float)v2Size.x / (float)v2Size.y;
		m4ScreenMatrix = mat4.ortho((float)v2Size.y, (float)v2Size.x, 0.0f, 0.0f, -100.0f, 100.0f);
		v2PixelSize = vec2.div(new vec2(1.0f), new vec2(v2Size));

		resetViewport();

		if(resizedCallback != null)
			resizedCallback.run(size);
	}

	void windowMovedCallback(ivec2 pos)
	{
		v2Pos = pos;
		if(movedCallback != null)
			movedCallback.run(pos);
	}

	void windowFocusedCallback(boolean hasfocus)
	{
		if(focusedCallback != null)
			focusedCallback.run(hasfocus);
	}

	public void onResized(WindowResizePosCallback callback) { resizedCallback = callback; }
	public void onMoved(WindowResizePosCallback callback)   { movedCallback = callback; }
	public void onFocused(WindowFocusCallback callback)     { focusedCallback = callback; }

    
    
	public void maximize(boolean domaximize) 
	{ 
        if(domaximize)
            glfwMaximizeWindow(lWindowID);
        else
            restore();
        bIsMaximized = domaximize;
	}

	public void minimize(boolean dominimize)     
    { 
        if(dominimize)
            glfwIconifyWindow(lWindowID);
        else
            restore();
        bIsMinimized = dominimize;
    }

	public void hide(boolean hiddenstat)
	{ 
        if(hiddenstat)
            glfwHideWindow(lWindowID);
        else
            glfwShowWindow(lWindowID);
		bIsHidden = hiddenstat;
        
	}

	public void finishRender()                     { glfwSwapBuffers(lWindowID); }  
    public void focus()                            { glfwFocusWindow(lWindowID); }
    public void restore()                          { glfwRestoreWindow(lWindowID); }
    public void destroyNativeWindow()              { glfwDestroyWindow(lWindowID); }
    public void makeResizable(boolean isresizable) { glfwSetWindowAttrib(lWindowID, GLFW_RESIZABLE, isresizable ? 1 : 0); bIsResizable = isresizable; }
    public void makeFullscreen(boolean fullscreen) { bIsFullscreen = fullscreen; }
    public void makeFloating(boolean isfloating)   { glfwSetWindowAttrib(lWindowID, GLFW_FLOATING, isfloating ? 1 : 0); this.bIsFloating = isfloating; }
    public void showBorder(boolean show)           { glfwSetWindowAttrib(lWindowID, GLFW_DECORATED, show ? 1 : 0); bHasBorder = show; }



    public void setIcon(ArrayList<Texture> images, boolean isgrayscale, vec4 color)
    {			
		try(GLFWImage.Buffer icons = GLFWImage.malloc(images.size()))
		{
			for(int i = 0; i < images.size(); i++)
			{
				Texture image = images.get(i);

				ByteBuffer newBuffer;
				if(isgrayscale)
				{
					int size = image.getSize().x * image.getSize().y;
					newBuffer = ByteBuffer.allocateDirect(size * 4);
					int comps = image.numComponents();

					for(int j = 0; j < size; j++)
					{
						vec4 col = vec4.mul(new vec4(new vec3(image.getData().get(j * comps + 0) & 0xFF), image.getData().get(j * comps + 1) & 0xFF), color);
						col.print();
	
						newBuffer.put(j * 4 + 0, (byte)col.x); // R
						newBuffer.put(j * 4 + 1, (byte)col.y); // G
						newBuffer.put(j * 4 + 2, (byte)col.z); // B
						newBuffer.put(j * 4 + 3, (byte)col.w); // A
					}
					newBuffer.flip();
				}
				else
				{
					newBuffer = image.getData();
				}

				icons
					.position(i)
					.width(image.getSize().x)
					.height(image.getSize().y)
					.pixels(newBuffer);
			}
			icons.position(0);
			glfwSetWindowIcon(lWindowID, icons);


			for(int i = 0; i < images.size(); i++)
				images.get(i).deleteData();
		}
    }



    //Setter
	public void setVerticalSync(boolean vsync)  { glfwSwapInterval(vsync ? 1 : 0); bHasVerticalSync = vsync; }
    public void setSize(ivec2 size)             { glfwSetWindowSize(lWindowID, size.x, size.y); }
    public void setPosition(ivec2 pos)          { glfwSetWindowPos(lWindowID, pos.x, pos.y); }
	public void setTitle(String title)          { glfwSetWindowTitle(lWindowID, title); this.sTitle = title; }
	public void setClearColor(vec4 color)	    { glClearColor(color.x, color.y, color.z, color.w); }
	public void setResizable(boolean resizable) { this.bIsResizable = resizable; }

	//Getter
	public Keyboard getKeyboard()				{ return this.pKeyboard; }
	public Mouse getMouse()					    { return this.pMouse; }
	public Window getParentWindow()			    { return this.pParentWindow; }
	public vec2 getPixelSize()          		{ return this.v2PixelSize; }
	public ivec2 getSize()              		{ return this.v2Size; }
	public ivec2 getPosition()          		{ return this.v2Pos; }
	public mat4 getScreenMatrix()			    { return this.m4ScreenMatrix; }
	public String getTitle()			        { return this.sTitle; }
	public float getAspectRatio()        	    { return this.fAspectRatio; }
	public float getAspectRatioWidthToHeight()  { return this.fAspectRatioWidthToHeight; }
	public boolean isFullscreen()          	    { return this.bIsFullscreen; }
	public boolean isOpen()                	    { return !this.bShouldClose; }
	public boolean isMaximized()				{ return this.bIsMaximized; }
	public boolean isResizable()				{ return this.bIsResizable; }
    public long getNativeWindow()               { return lWindowID; }
}
