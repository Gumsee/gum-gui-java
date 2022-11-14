package com.gumse.system.io;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFW;

import com.gumse.system.Event;
import com.gumse.system.Window;

public class Keyboard  
{
    public static final int GUM_KEY_UNKNOWN =             0;
    public static final int GUM_KEY_SPACE =               GLFW_KEY_SPACE;
    public static final int GUM_KEY_APOSTROPHE =          GLFW_KEY_APOSTROPHE;
    public static final int GUM_KEY_COMMA =               GLFW_KEY_COMMA;
    public static final int GUM_KEY_MINUS =               GLFW_KEY_MINUS;
    public static final int GUM_KEY_PERIOD =              GLFW_KEY_PERIOD;
    public static final int GUM_KEY_SLASH =               GLFW_KEY_SLASH;
    public static final int GUM_KEY_0 =                   GLFW_KEY_0;
    public static final int GUM_KEY_1 =                   GLFW_KEY_1;
    public static final int GUM_KEY_2 =                   GLFW_KEY_2;
    public static final int GUM_KEY_3 =                   GLFW_KEY_3;
    public static final int GUM_KEY_4 =                   GLFW_KEY_4;
    public static final int GUM_KEY_5 =                   GLFW_KEY_5;
    public static final int GUM_KEY_6 =                   GLFW_KEY_6;
    public static final int GUM_KEY_7 =                   GLFW_KEY_7;
    public static final int GUM_KEY_8 =                   GLFW_KEY_8;
    public static final int GUM_KEY_9 =                   GLFW_KEY_9;
    public static final int GUM_KEY_SEMICOLON =           GLFW_KEY_SEMICOLON;
    public static final int GUM_KEY_EQUAL =               GLFW_KEY_EQUAL;
    public static final int GUM_KEY_A =                   GLFW_KEY_A;
    public static final int GUM_KEY_B =                   GLFW_KEY_B;
    public static final int GUM_KEY_C =                   GLFW_KEY_C;
    public static final int GUM_KEY_D =                   GLFW_KEY_D;
    public static final int GUM_KEY_E =                   GLFW_KEY_E;
    public static final int GUM_KEY_F =                   GLFW_KEY_F;
    public static final int GUM_KEY_G =                   GLFW_KEY_G;
    public static final int GUM_KEY_H =                   GLFW_KEY_H;
    public static final int GUM_KEY_I =                   GLFW_KEY_I;
    public static final int GUM_KEY_J =                   GLFW_KEY_J;
    public static final int GUM_KEY_K =                   GLFW_KEY_K;
    public static final int GUM_KEY_L =                   GLFW_KEY_L;
    public static final int GUM_KEY_M =                   GLFW_KEY_M;
    public static final int GUM_KEY_N =                   GLFW_KEY_N;
    public static final int GUM_KEY_O =                   GLFW_KEY_O;
    public static final int GUM_KEY_P =                   GLFW_KEY_P;
    public static final int GUM_KEY_Q =                   GLFW_KEY_Q;
    public static final int GUM_KEY_R =                   GLFW_KEY_R;
    public static final int GUM_KEY_S =                   GLFW_KEY_S;
    public static final int GUM_KEY_T =                   GLFW_KEY_T;
    public static final int GUM_KEY_U =                   GLFW_KEY_U;
    public static final int GUM_KEY_V =                   GLFW_KEY_V;
    public static final int GUM_KEY_W =                   GLFW_KEY_W;
    public static final int GUM_KEY_X =                   GLFW_KEY_X;
    public static final int GUM_KEY_Y =                   GLFW_KEY_Y;
    public static final int GUM_KEY_Z =                   GLFW_KEY_Z;
    public static final int GUM_KEY_LEFT_PARENTHESIS =    0;
    public static final int GUM_KEY_RIGHT_PARENTHESIS =   0;
    public static final int GUM_KEY_LEFT_BRACKET =        GLFW_KEY_LEFT_BRACKET;
    public static final int GUM_KEY_RIGHT_BRACKET =       GLFW_KEY_RIGHT_BRACKET;
    public static final int GUM_KEY_BACKSLASH =           GLFW_KEY_BACKSLASH;
    public static final int GUM_KEY_GRAVE_ACCENT =        GLFW_KEY_GRAVE_ACCENT;
    //public static final //int GUM_KEY_WORLD_1 =           GLFW_KEY_WORLD_1;
    //public static final //int GUM_KEY_WORLD_2 =           GLFW_KEY_WORLD_2;
    public static final int GUM_KEY_ESCAPE =              GLFW_KEY_ESCAPE;
    public static final int GUM_KEY_ENTER =               GLFW_KEY_ENTER;
    public static final int GUM_KEY_TAB =                 GLFW_KEY_TAB;
    public static final int GUM_KEY_BACKSPACE =           GLFW_KEY_BACKSPACE;
    public static final int GUM_KEY_INSERT =              GLFW_KEY_INSERT;
    public static final int GUM_KEY_DELETE =              GLFW_KEY_DELETE;
    public static final int GUM_KEY_RIGHT =               GLFW_KEY_RIGHT;
    public static final int GUM_KEY_LEFT =                GLFW_KEY_LEFT;
    public static final int GUM_KEY_DOWN =                GLFW_KEY_DOWN;
    public static final int GUM_KEY_UP =                  GLFW_KEY_UP;
    public static final int GUM_KEY_PAGE_UP =             GLFW_KEY_PAGE_UP;
    public static final int GUM_KEY_PAGE_DOWN =           GLFW_KEY_PAGE_DOWN;
    public static final int GUM_KEY_HOME =                GLFW_KEY_HOME;
    public static final int GUM_KEY_END =                 GLFW_KEY_END;
    public static final int GUM_KEY_CAPS_LOCK =           GLFW_KEY_CAPS_LOCK;
    public static final int GUM_KEY_SCROLL_LOCK =         GLFW_KEY_SCROLL_LOCK;
    public static final int GUM_KEY_NUM_LOCK =            GLFW_KEY_NUM_LOCK;
    public static final int GUM_KEY_PRINT_SCREEN =        GLFW_KEY_PRINT_SCREEN;
    public static final int GUM_KEY_PAUSE =               GLFW_KEY_PAUSE;
    public static final int GUM_KEY_F1 =                  GLFW_KEY_F1;
    public static final int GUM_KEY_F2 =                  GLFW_KEY_F2;
    public static final int GUM_KEY_F3 =                  GLFW_KEY_F3;
    public static final int GUM_KEY_F4 =                  GLFW_KEY_F4;
    public static final int GUM_KEY_F5 =                  GLFW_KEY_F5;
    public static final int GUM_KEY_F6 =                  GLFW_KEY_F6;
    public static final int GUM_KEY_F7 =                  GLFW_KEY_F7;
    public static final int GUM_KEY_F8 =                  GLFW_KEY_F8;
    public static final int GUM_KEY_F9 =                  GLFW_KEY_F9;
    public static final int GUM_KEY_F10 =                 GLFW_KEY_F10;
    public static final int GUM_KEY_F11 =                 GLFW_KEY_F11;
    public static final int GUM_KEY_F12 =                 GLFW_KEY_F12;
    public static final int GUM_KEY_F13 =                 GLFW_KEY_F13;
    public static final int GUM_KEY_F14 =                 GLFW_KEY_F14;
    public static final int GUM_KEY_F15 =                 GLFW_KEY_F15;
    public static final int GUM_KEY_F16 =                 GLFW_KEY_F16;
    public static final int GUM_KEY_F17 =                 GLFW_KEY_F17;
    public static final int GUM_KEY_F18 =                 GLFW_KEY_F18;
    public static final int GUM_KEY_F19 =                 GLFW_KEY_F19;
    public static final int GUM_KEY_F20 =                 GLFW_KEY_F20;
    public static final int GUM_KEY_F21 =                 GLFW_KEY_F21;
    public static final int GUM_KEY_F22 =                 GLFW_KEY_F22;
    public static final int GUM_KEY_F23 =                 GLFW_KEY_F23;
    public static final int GUM_KEY_F24 =                 GLFW_KEY_F24;
    public static final int GUM_KEY_F25 =                 GLFW_KEY_F25;
    public static final int GUM_KEY_NUMPAD_0 =            GLFW_KEY_KP_0;
    public static final int GUM_KEY_NUMPAD_1 =            GLFW_KEY_KP_1;
    public static final int GUM_KEY_NUMPAD_2 =            GLFW_KEY_KP_2;
    public static final int GUM_KEY_NUMPAD_3 =            GLFW_KEY_KP_3;
    public static final int GUM_KEY_NUMPAD_4 =            GLFW_KEY_KP_4;
    public static final int GUM_KEY_NUMPAD_5 =            GLFW_KEY_KP_5;
    public static final int GUM_KEY_NUMPAD_6 =            GLFW_KEY_KP_6;
    public static final int GUM_KEY_NUMPAD_7 =            GLFW_KEY_KP_7;
    public static final int GUM_KEY_NUMPAD_8 =            GLFW_KEY_KP_8;
    public static final int GUM_KEY_NUMPAD_9 =            GLFW_KEY_KP_9;
    public static final int GUM_KEY_NUMPAD_DECIMAL =      GLFW_KEY_KP_DECIMAL;
    public static final int GUM_KEY_NUMPAD_DIVIDE =       GLFW_KEY_KP_DIVIDE;
    public static final int GUM_KEY_NUMPAD_MULTIPLY =     GLFW_KEY_KP_MULTIPLY;
    public static final int GUM_KEY_NUMPAD_SUBTRACT =     GLFW_KEY_KP_SUBTRACT;
    public static final int GUM_KEY_NUMPAD_ADD =          GLFW_KEY_KP_ADD;
    public static final int GUM_KEY_NUMPAD_ENTER =        GLFW_KEY_KP_ENTER;
    public static final int GUM_KEY_NUMPAD_EQUAL =        GLFW_KEY_KP_EQUAL;
    public static final int GUM_KEY_LEFT_SHIFT =          GLFW_KEY_LEFT_SHIFT;
    public static final int GUM_KEY_LEFT_CONTROL =        GLFW_KEY_LEFT_CONTROL;
    public static final int GUM_KEY_LEFT_ALT =            GLFW_KEY_LEFT_ALT;
    public static final int GUM_KEY_LEFT_SUPER =          GLFW_KEY_LEFT_SUPER;
    public static final int GUM_KEY_RIGHT_SHIFT =		  GLFW_KEY_RIGHT_SHIFT;
    public static final int GUM_KEY_RIGHT_CONTROL =		  GLFW_KEY_RIGHT_CONTROL;
    public static final int GUM_KEY_RIGHT_ALT =			  GLFW_KEY_RIGHT_ALT;
    public static final int GUM_KEY_RIGHT_SUPER =		  GLFW_KEY_RIGHT_SUPER;
    public static final int GUM_KEY_MENU =                GLFW_KEY_MENU;



    public static final int GUM_MOD_SHIFT =              GLFW_MOD_SHIFT;
    public static final int GUM_MOD_CONTROL =            GLFW_MOD_CONTROL;
    public static final int GUM_MOD_ALT =                GLFW_MOD_ALT;
    public static final int GUM_MOD_SUPER =              GLFW_MOD_SUPER;
    public static final int GUM_MOD_CAPS_LOCK =          GLFW_MOD_CAPS_LOCK;
    public static final int GUM_MOD_NUM_LOCK =           GLFW_MOD_NUM_LOCK;
    public static final int GUM_MOD_ESCAPE =             0;


    private boolean busy;

    private Window pContextWindow;
    private String u8TextInput;
    private int iLastPressedKey, iLastReleasedKey;
    private int iLastPressedModKey, iLastReleasedModKey;

    public interface KeyboardButtonCallback {
        void run(int key, int mod);
    }

    public interface KeyboardTextCallback {
        void run(String str, int codepoint);
    }
    
    //Callbacks
    private KeyboardTextCallback textEnteredCallback = null;
    private KeyboardButtonCallback keyPressedCallback = null, keyReleasedCallback = null;


    
    public Keyboard(Window context)
	{
		pContextWindow = context;
		iLastPressedKey = 0;
		iLastReleasedKey = 0;
		u8TextInput = "";

        
	}

	public void cleanup()
	{
	}

	public void reset()
	{
		this.u8TextInput = "";
		this.iLastPressedKey = 0;
		this.iLastReleasedKey = 0;
		this.iLastPressedModKey = 0;
		this.iLastReleasedModKey = 0;
	}


	public static String key2string(int key)
	{
		if     (key == GUM_KEY_A) 					return "A";
		else if(key == GUM_KEY_B) 					return "B";
		else if(key == GUM_KEY_C) 					return "C";
		else if(key == GUM_KEY_D) 					return "D";
		else if(key == GUM_KEY_E) 					return "E";
		else if(key == GUM_KEY_F) 					return "F";
		else if(key == GUM_KEY_G) 					return "G";
		else if(key == GUM_KEY_H) 					return "H";
		else if(key == GUM_KEY_I) 					return "I";
		else if(key == GUM_KEY_J) 					return "J";
		else if(key == GUM_KEY_K) 					return "K";
		else if(key == GUM_KEY_L) 					return "L";
		else if(key == GUM_KEY_M) 					return "M";
		else if(key == GUM_KEY_N) 					return "N";
		else if(key == GUM_KEY_O) 					return "O";
		else if(key == GUM_KEY_P) 					return "P";
		else if(key == GUM_KEY_Q) 					return "Q";
		else if(key == GUM_KEY_R) 					return "R";
		else if(key == GUM_KEY_S) 					return "S";
		else if(key == GUM_KEY_T) 					return "T";
		else if(key == GUM_KEY_U) 					return "U";
		else if(key == GUM_KEY_V) 					return "V";
		else if(key == GUM_KEY_W) 					return "W";
		else if(key == GUM_KEY_X) 					return "X";
		else if(key == GUM_KEY_Y) 					return "Y";
		else if(key == GUM_KEY_Z) 					return "Z";
		else if(key == GUM_KEY_0) 					return "0";
		else if(key == GUM_KEY_1) 					return "1";
		else if(key == GUM_KEY_2) 					return "2";
		else if(key == GUM_KEY_3) 					return "3";
		else if(key == GUM_KEY_4) 					return "4";
		else if(key == GUM_KEY_5) 					return "5";
		else if(key == GUM_KEY_6) 					return "6";
		else if(key == GUM_KEY_7) 					return "7";
		else if(key == GUM_KEY_8) 					return "8";
		else if(key == GUM_KEY_9) 					return "9";
		else if(key == GUM_KEY_NUMPAD_0) 	    	return "Numpad0";
		else if(key == GUM_KEY_NUMPAD_1) 	    	return "Numpad1";
		else if(key == GUM_KEY_NUMPAD_2) 	    	return "Numpad2";
		else if(key == GUM_KEY_NUMPAD_3) 	    	return "Numpad3";
		else if(key == GUM_KEY_NUMPAD_4) 	    	return "Numpad4";
		else if(key == GUM_KEY_NUMPAD_5) 	    	return "Numpad5";
		else if(key == GUM_KEY_NUMPAD_6) 	    	return "Numpad6";
		else if(key == GUM_KEY_NUMPAD_7) 	    	return "Numpad7";
		else if(key == GUM_KEY_NUMPAD_8) 	    	return "Numpad8";
		else if(key == GUM_KEY_NUMPAD_9) 	    	return "Numpad9";
		else if(key == GUM_KEY_F1) 					return "F1";
		else if(key == GUM_KEY_F2) 					return "F2";
		else if(key == GUM_KEY_F3) 					return "F3";
		else if(key == GUM_KEY_F4) 					return "F4";
		else if(key == GUM_KEY_F5) 					return "F5";
		else if(key == GUM_KEY_F6) 					return "F6";
		else if(key == GUM_KEY_F7) 					return "F7";
		else if(key == GUM_KEY_F8) 					return "F8";
		else if(key == GUM_KEY_F9) 					return "F9";
		else if(key == GUM_KEY_F10) 				return "F10";
		else if(key == GUM_KEY_F11) 				return "F11";
		else if(key == GUM_KEY_F12) 				return "F12";
		else if(key == GUM_KEY_F13) 				return "F13";
		else if(key == GUM_KEY_F14) 				return "F14";
		else if(key == GUM_KEY_F15) 				return "F15";
		else if(key == GUM_KEY_F16) 				return "F16";
		else if(key == GUM_KEY_F17) 				return "F17";
		else if(key == GUM_KEY_F18) 				return "F18";
		else if(key == GUM_KEY_F19) 				return "F19";
		else if(key == GUM_KEY_F20) 				return "F20";
		else if(key == GUM_KEY_F21) 				return "F21";
		else if(key == GUM_KEY_F22) 				return "F22";
		else if(key == GUM_KEY_F23) 				return "F23";
		else if(key == GUM_KEY_F24) 				return "F24";
		else if(key == GUM_KEY_F25) 				return "F25";
		else if(key == GUM_KEY_ESCAPE) 				return "Escape";
		else if(key == GUM_KEY_LEFT_CONTROL) 		return "LControl";
		else if(key == GUM_KEY_LEFT_SHIFT) 			return "LShift";
		else if(key == GUM_KEY_LEFT_ALT) 			return "LAlt";
		else if(key == GUM_KEY_LEFT_SUPER) 			return "LSystem";
		else if(key == GUM_KEY_RIGHT_CONTROL) 		return "RControl";
		else if(key == GUM_KEY_RIGHT_SHIFT) 		return "RShift";
		else if(key == GUM_KEY_RIGHT_ALT) 			return "RAlt";
		else if(key == GUM_KEY_RIGHT_SUPER) 		return "RSystem";
		else if(key == GUM_KEY_MENU) 				return "Menu";
		else if(key == GUM_KEY_LEFT_PARENTHESIS) 	return "(";
		else if(key == GUM_KEY_RIGHT_PARENTHESIS) 	return ")";
		else if(key == GUM_KEY_LEFT_BRACKET) 		return "[";
		else if(key == GUM_KEY_RIGHT_BRACKET) 		return "]";
		else if(key == GUM_KEY_SEMICOLON) 			return ";";
		else if(key == GUM_KEY_COMMA) 				return ",";
		else if(key == GUM_KEY_PERIOD) 				return ".";
		else if(key == GUM_KEY_APOSTROPHE) 			return "\'";
		else if(key == GUM_KEY_SLASH) 				return "/";
		else if(key == GUM_KEY_BACKSLASH) 			return "\\";
		else if(key == GUM_KEY_EQUAL) 				return "=";
		else if(key == GUM_KEY_MINUS) 				return "-";
		else if(key == GUM_KEY_SPACE) 				return "Space";
		else if(key == GUM_KEY_ENTER) 				return "Return";
		else if(key == GUM_KEY_BACKSPACE) 			return "Back";
		else if(key == GUM_KEY_TAB) 				return "Tab";
		else if(key == GUM_KEY_PAGE_UP) 			return "Page Up";
		else if(key == GUM_KEY_PAGE_DOWN) 			return "Page Down";
		else if(key == GUM_KEY_END) 				return "End";
		else if(key == GUM_KEY_HOME) 				return "Home";
		else if(key == GUM_KEY_INSERT) 				return "Insert";
		else if(key == GUM_KEY_DELETE) 				return "Delete";
		else if(key == GUM_KEY_PAUSE) 				return "Paues";
		else if(key == GUM_KEY_NUMPAD_ADD) 			return "+";
		else if(key == GUM_KEY_NUMPAD_SUBTRACT) 	return "-";
		else if(key == GUM_KEY_NUMPAD_MULTIPLY) 	return "*";
		else if(key == GUM_KEY_NUMPAD_DIVIDE) 		return "/";
		else if(key == GUM_KEY_LEFT) 				return "Left";
		else if(key == GUM_KEY_RIGHT) 				return "Right";
		else if(key == GUM_KEY_UP) 					return "UP";
		else if(key == GUM_KEY_DOWN) 				return "Down";

		return "";
	}

	public boolean checkLastPressedKey(int key) { return checkLastPressedKey(key, 0); }
	public boolean checkLastPressedKey(int key, int modkey)
	{ 
		return iLastPressedKey == key && iLastPressedModKey == modkey; 
	}

	public boolean checkLastReleasedKey(int key) { return checkLastReleasedKey(key, 0); }
	public boolean checkLastReleasedKey(int key, int modkey)
	{ 
		System.out.println(iLastReleasedModKey & modkey);
		return iLastReleasedKey == key && iLastReleasedModKey == modkey;
	}

	public boolean checkKeyPressed(int key)
	{ 
		return GLFW.glfwGetKey(pContextWindow.getNativeWindow(), key) == GLFW_PRESS; 
		//return iLastPressedKey == key;
	}

	public boolean checkKeyReleased(int key)
	{ 
		return GLFW.glfwGetKey(pContextWindow.getNativeWindow(), key) == GLFW_RELEASE; 
		//return iLastReleasedKey == key;
	}

	public void setBusiness(boolean val)  { busy = val; }

    
    public void handleEvent(Event event)
    {
        switch(event.type)
        {
            case Event.GUM_EVENT_KEYBOARD_PRESSED:
                keyboardPressCallback(event.data.keyboardkey, event.data.keyboardmod);
                break;
                
            case Event.GUM_EVENT_KEYBOARD_RELEASED:
                keyboardReleaseCallback(event.data.keyboardkey, event.data.keyboardmod);
                break;
				
			case Event.GUM_EVENT_KEYBOARD_TEXT_ENTERED:
				keyboardTextCallback(event.data.keyboardkey);
				break;
        }
    }
    
	void keyboardPressCallback(int key, int modkey)
	{
		iLastPressedKey  = key; 
		iLastPressedModKey = modkey;

		if(keyPressedCallback != null)
			keyPressedCallback.run(key, modkey);
	}

	void keyboardReleaseCallback(int key, int modkey)
	{
		iLastReleasedKey = key; 
		iLastReleasedModKey = modkey;

		if(keyReleasedCallback != null)
			keyReleasedCallback.run(key, modkey);
	}

	void keyboardTextCallback(int codepoint)
	{
		//std::wstring_convert<std::codecvt_utf8<char32_t>, char32_t> convert;
		//u8TextInput = convert.to_bytes(codepoint);

        u8TextInput = new String(Character.toChars(codepoint));

		if(textEnteredCallback != null)
			textEnteredCallback.run(u8TextInput, codepoint);
	}

	
	public void onKeyPress(KeyboardButtonCallback callback)   { keyPressedCallback = callback; }
	public void onKeyRelease(KeyboardButtonCallback callback) { keyReleasedCallback = callback; }
	public void onTextEntered(KeyboardTextCallback callback)  { textEnteredCallback = callback; }

	public boolean isBusy()      { return busy; }
	public String getTextInput() { return this.u8TextInput; }
}