package com.gumse.gui;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class GumGUI
{
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)Native.load("GumSystem-shared", CLibrary.class);
    
        void printf(String format, Object... args);
    }

    public GumGUI(String[] args)
    {
        CLibrary.INSTANCE.printf("Hello, World\n");
        for (int i=0;i < args.length;i++) 
        {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }

        
    }

    public void render()
    {
      
    }
}