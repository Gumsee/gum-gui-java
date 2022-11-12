package com.gumse.system.io;

import org.lwjgl.glfw.GLFW;

import com.gumse.maths.*;
import com.gumse.system.Event;
import com.gumse.system.Window;

public class Mouse 
{
    public static final int GUM_MOUSE_BUTTON_LEFT =     				0x00;
    public static final int GUM_MOUSE_BUTTON_RIGHT =    				0x01;
    public static final int GUM_MOUSE_BUTTON_MIDDLE =   				0x03;
    public static final int GUM_MOUSE_BUTTON_PREVIOUS = 				0x04;
    public static final int GUM_MOUSE_BUTTON_NEXT =     				0x05;
    
    public static final int GUM_CURSOR_DEFAULT = 						0x00;
    public static final int GUM_CURSOR_HORIZONTAL_RESIZE = 				0x01;
    public static final int GUM_CURSOR_VERTICAL_RESIZE = 				0x02;
    public static final int GUM_CURSOR_HAND = 							0x03;
    public static final int GUM_CURSOR_CROSSHAIR = 						0x04;
    public static final int GUM_CURSOR_IBEAM = 							0x05;
    public static final int GUM_CURSOR_TOPLEFT_TO_BOTTOMRIGHT_RESIZE = 	0x06;
    public static final int GUM_CURSOR_TOPRIGHT_TO_BOTTOMLEFT_RESIZE = 	0x07;
    public static final int GUM_CURSOR_ALL_SIDES_RESIZE = 				0x08;
    public static final int GUM_CURSOR_NOT_ALLOWED = 					0x09;

    private static boolean bActiveHovering = false;
    private ivec2 v2Position, v2PreviousPosition, v2LeftClickPosition;
    private int iMouseWheelState;
    private int frameSize;
    private int CursorType;
    private vec3 rayDir;
    private int mouseOnID;
    private Window pContextWindow;

    private float lastClickTimeLeft = 0.0f;
    private float lastClickTimeRight = 0.0f;

    private boolean LeftDown = false;
    private boolean RightDown = false;
    private boolean LeftDownStart = false;
    private boolean RightDownStart = false;
    private boolean LeftClickOnce = false;
    private boolean RightClickOnce = false;
    private boolean LeftReleased = false;
    private boolean RightReleased = false;
    private boolean LeftDoubleClick = false;
    private boolean RightDoubleClick = false;

    private boolean defaulHideState;
    private boolean defaulTrapState;
    private boolean updateonclick;
    private boolean bIsTrapped;

    public interface MouseButtonCallback {
        void run(int button, int mods);
    }

    public interface MouseMovedCallback {
        void run(ivec2 position);
    }

    public interface MouseEnteredLeftCallback {
        void run();
    }

    //Callbacks
    private MouseButtonCallback pressCallback = null, releaseCallback = null;
    private MouseMovedCallback scrollCallback = null, movedCallback = null;
    private MouseEnteredLeftCallback enteredCallback = null, leftCallback = null;

    
    static boolean bIsBusy = false, bIsSnapped = false;
    static ivec2 v2PositionDelta = new ivec2(), v2ScreenPosition = new ivec2(), v2PreviousScreenPosition = new ivec2(), v2SnapPoint = new ivec2();

    static private long lDefaultCursor, lHandCursor, lHorizontalCursor, lVerticalCursor, lCrosshairCursor, lIBeamCursor, lTopLeftBottomRightCursor, lTopRightButtomLeftCursor, lAllSidesCursor, lNotAllowedCursor;


    public Mouse(Window context)
    {
        this.v2Position = new ivec2(0,0);
        this.v2PreviousPosition = new ivec2(0,0);
        this.pContextWindow = context;
        this.v2LeftClickPosition = new ivec2(-1, -1);

        updateonclick = false;
        bIsTrapped = false;

        iMouseWheelState = 0;
        CursorType = 0;
        frameSize = 0;
        rayDir = new vec3(0);
        //DragAndDropInfo = "";
        mouseOnID = -1;

        lDefaultCursor            = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        lHandCursor               = GLFW.glfwCreateStandardCursor(GLFW.GLFW_POINTING_HAND_CURSOR);
        lHorizontalCursor         = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_EW_CURSOR);
        lVerticalCursor           = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NS_CURSOR);
        lCrosshairCursor          = GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR);
        lIBeamCursor              = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
        lTopLeftBottomRightCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NWSE_CURSOR);
        lTopRightButtomLeftCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NESW_CURSOR);
        lAllSidesCursor           = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_ALL_CURSOR);
        lNotAllowedCursor         = GLFW.glfwCreateStandardCursor(GLFW.GLFW_NOT_ALLOWED_CURSOR);
        
        //glfwSetInputMode(pContextWindow.getRenderWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        
        /*if (glfwRawMouseMotionSupported())
            glfwSetInputMode(pContextWindow.getRenderWindow(), GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);*/
    }

    
    public void cleanup()
    {
        GLFW.glfwDestroyCursor(lDefaultCursor);
        GLFW.glfwDestroyCursor(lHandCursor);
        GLFW.glfwDestroyCursor(lHorizontalCursor);
        GLFW.glfwDestroyCursor(lVerticalCursor);
        GLFW.glfwDestroyCursor(lCrosshairCursor);
        GLFW.glfwDestroyCursor(lIBeamCursor);
        GLFW.glfwDestroyCursor(lTopLeftBottomRightCursor);
        GLFW.glfwDestroyCursor(lTopRightButtomLeftCursor);
        GLFW.glfwDestroyCursor(lAllSidesCursor);
        GLFW.glfwDestroyCursor(lNotAllowedCursor);
    }

    public void setCursor(int type)
    {
        switch(type)
        {
            case GUM_CURSOR_DEFAULT:                        GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lDefaultCursor); break;
            case GUM_CURSOR_HORIZONTAL_RESIZE:              GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lHorizontalCursor); break;
            case GUM_CURSOR_VERTICAL_RESIZE:                GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lVerticalCursor); break;
            case GUM_CURSOR_HAND:                           GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lHandCursor); break;
            case GUM_CURSOR_CROSSHAIR:                      GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lCrosshairCursor); break;
            case GUM_CURSOR_IBEAM:                          GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lIBeamCursor); break;
            case GUM_CURSOR_TOPLEFT_TO_BOTTOMRIGHT_RESIZE:  GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lTopLeftBottomRightCursor); break;
            case GUM_CURSOR_TOPRIGHT_TO_BOTTOMLEFT_RESIZE:  GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lTopRightButtomLeftCursor); break;
            case GUM_CURSOR_ALL_SIDES_RESIZE:               GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lAllSidesCursor); break;
            case GUM_CURSOR_NOT_ALLOWED:                    GLFW.glfwSetCursor(pContextWindow.getNativeWindow(), lNotAllowedCursor); break;
        }
    }


	public float getDeltaDistanceNorm()
	{		
		vec2 normPos = vec2.div(vec2.sub(new vec2(v2Position), new vec2(pContextWindow.getPosition())), new vec2(pContextWindow.getSize()));
		vec2 normOldPos = vec2.div(vec2.sub(new vec2(v2PreviousPosition), new vec2(pContextWindow.getPosition())), new vec2(pContextWindow.getSize()));

		return vec2.distance(normPos, normOldPos); 
	}


	public void calcRay()
	{
		/*float frameX = (float)renderFrame.x;
        float frameY = (float)renderFrame.y;
        vec4 screen_space;

        //screen space to normalized device space
		screen_space.x = (2 * (getPosition().x / frameX)) - 1;
		screen_space.y = -((2 * (getPosition().y / frameY)) - 1);
        screen_space.z = -1.0f;
		screen_space.z = 1.0f;

		mat4 invertedProjection = mat4::inverse(GumEngine::ActiveCamera.getProjectionMatrix());
		vec4 eye_space = mat4::transpose(invertedProjection) * screen_space;
		eye_space.z = -1.0f;
		eye_space.w = 0.0f;

        //eye space to world space
		vec4 worldspace = GumEngine::ActiveCamera.getViewMatrix() * eye_space;

		rayDir = worldspace;
        rayDir = vec3::normalize(rayDir);*/
	}

    public void reset()
    {
        //Replace with chrono
        lastClickTimeLeft += 0.01; //FPS::get();
        lastClickTimeRight += 0.01; //FPS::get();
        
        //if(LeftReleased)
        //    v2LeftClickPosition.set(new ivec2(-1, -1));
		LeftReleased = false;
		RightReleased = false;
        LeftDoubleClick = false;
        RightDoubleClick = false;
        LeftDownStart = false;
        RightDownStart = false;

		iMouseWheelState = 0;
        CursorType = 0;
        frameSize = 0;

        if(!bActiveHovering)
            setCursor(GUM_CURSOR_DEFAULT);

        bActiveHovering = false;


        //if(bIsTrapped) { setPosition(pContextWindow.getSize() / 2); }
    }

    //Setter
    public void setContextWindow(Window context) { this.pContextWindow = context; }
	public void updateOnClick(boolean bln)    	 { this.updateonclick = bln; }
	public void trap(boolean doTrap) 			 { this.bIsTrapped = doTrap; }
    public void setInstanceIDUnderMouse(int id)  { this.mouseOnID = id; }
    static public void setActiveHovering(boolean hover) { bActiveHovering = hover; }


    //Getter
	public vec3 getRayDirection() 			     { return this.rayDir; }
	public ivec2 getPosition() 				     { return this.v2Position; }
    public ivec2 getLeftClickPosition()          { return this.v2LeftClickPosition; }
	public int getCurrentPickedObjectID() 		 { return this.mouseOnID; }
	public int getMouseWheelState() 			 { return this.iMouseWheelState; }
    public int getCursorType()                   { return this.CursorType; }
    public int getInstanceIDUnderMouse()         { return this.mouseOnID; }
    
    public boolean hasLeftClick()                { return this.LeftDown; }
    public boolean hasRightClick()               { return this.RightDown; }
    public boolean hasLeftClickStart()           { return this.LeftDownStart; }
    public boolean hasRightClickStart()          { return this.RightDownStart; }
    public boolean hasLeftDoubleClick()          { return this.LeftDoubleClick; }
    public boolean hasRightDoubleClick()         { return this.RightDoubleClick; }
    public boolean hasLeftRelease()              { return this.LeftReleased; }
    public boolean hasRightRelease()             { return this.RightReleased; }
    //bool hasMiddleClick()      { return glfwGetMouseButton(pContextWindow.getRenderWindow(), GLFW_MOUSE_BUTTON_MIDDLE) == GLFW_PRESS; }
    //bool hasMiddleRelease()    { return glfwGetMouseButton(pContextWindow.getRenderWindow(), GLFW_MOUSE_BUTTON_MIDDLE) == GLFW_RELEASE; }
    

    public void handleEvent(Event event)
    {
        switch(event.type)
        {
            case Event.GUM_EVENT_MOUSE_PRESSED:  mouseButtonPressCallback(event.data.mousebutton, 0); break;
            case Event.GUM_EVENT_MOUSE_RELEASED: mouseButtonReleaseCallback(event.data.mousebutton, 0); break;
            case Event.GUM_EVENT_MOUSE_SCROLL:   mouseScrollCallback(event.data.mousescroll); break;
            case Event.GUM_EVENT_MOUSE_MOVED:    mouseMovedCallback(event.data.mousepos); break;
            case Event.GUM_EVENT_MOUSE_ENTERED:  mouseEnteredCallback(); break;
            case Event.GUM_EVENT_MOUSE_LEFT:     mouseLeftCallback(); break;
        }
    }

    
    void mouseButtonPressCallback(int button, int mods)
    {
        if(button == GUM_MOUSE_BUTTON_LEFT)
        {
            LeftClickOnce = true;
            LeftDownStart = true;
            LeftDown = true;
            v2LeftClickPosition = getPosition();
        }

        if(button == GUM_MOUSE_BUTTON_RIGHT)
        {
            RightClickOnce = true;
            RightDownStart = true;
            RightDown = true;
        }

        if(pressCallback != null)
            pressCallback.run(button, mods);
    }

    void mouseButtonReleaseCallback(int button, int mods)
    {
        if(button == GUM_MOUSE_BUTTON_LEFT)
        {
            if(LeftClickOnce)
            { 
                if(lastClickTimeLeft < 0.2f)
                {
                    LeftDoubleClick = true;
                    lastClickTimeLeft = 0.2f;
                }
                else
                    lastClickTimeLeft = 0;

                LeftClickOnce = false;
            }
            LeftReleased = true; 
            LeftDown = false;
        }

        if(button == GUM_MOUSE_BUTTON_RIGHT)
        {
            if(RightClickOnce)
            {
                if(lastClickTimeRight < 0.2f)
                {
                    RightDoubleClick = true;
                    lastClickTimeRight = 0.2f;
                }
                else
                    lastClickTimeRight = 0;
            }
            RightReleased = true; 
            RightDown = false;
        }

        if(releaseCallback != null)
            releaseCallback.run(button, mods);
    }
    
    void mouseScrollCallback(ivec2 dir)
    {
        iMouseWheelState = dir.y;

        if(scrollCallback != null)
            scrollCallback.run(dir);
    }
    
    void mouseMovedCallback(ivec2 pos)
    {
        v2PreviousPosition = v2Position;
        v2Position = pos;

        //v2PositionDelta = v2Position - v2PreviousPosition;
        calcRay();

        if(movedCallback != null)
            movedCallback.run(pos);
    }
    
    void mouseEnteredCallback()
    {
        if(enteredCallback != null)
            enteredCallback.run();
    }

    void mouseLeftCallback()
    {
        if(leftCallback != null)
            leftCallback.run();
    }


    public void onPress(MouseButtonCallback callback)        { pressCallback = callback; }
    public void onRelease(MouseButtonCallback callback)      { releaseCallback = callback; }
    public void onScroll(MouseMovedCallback callback)        { scrollCallback = callback; }
    public void onMoved(MouseMovedCallback callback)         { movedCallback = callback; }
    public void onEntered(MouseEnteredLeftCallback callback) { enteredCallback = callback; }
    public void onLeft(MouseEnteredLeftCallback callback)    { leftCallback = callback; }


    //
    // Global static methods
    //
    public static void update()
    {
        /*int x, y, winx, winy;
        unsigned int mask = 0;
        unsigned long childWin, rootWin;
        XQueryPointer(Display::getSystemHandle(), XRootWindow(Display::getSystemHandle(), XDefaultScreen(Display::getSystemHandle())), &childWin, &rootWin, &x, &y, &winx, &winy, &mask);*/
        
        v2ScreenPosition = getScreenPosition();
        v2PositionDelta = ivec2.sub(v2ScreenPosition, v2PreviousScreenPosition);
        v2PreviousScreenPosition = v2ScreenPosition;

        /*if(bIsSnapped) 
        {
            setGlobalPosition(v2SnapPoint);
            v2PreviousScreenPosition = v2SnapPoint;
        }*/
    }

    public static void freeze(boolean state)
    {
        if(bIsSnapped != state)
        {
            v2SnapPoint = v2ScreenPosition;
            bIsSnapped = state;
        }
    }

    public static ivec2 getScreenPosition() 	     { return v2ScreenPosition; }
    public static ivec2 getDelta() 			         { return v2PositionDelta; }
    public static boolean isBusy()                   { return bIsBusy; }

    public static void setBusiness(boolean val)      { bIsBusy = val; }
    public static void setSnapPoint(ivec2 snappoint) { v2SnapPoint = snappoint; }    
}
