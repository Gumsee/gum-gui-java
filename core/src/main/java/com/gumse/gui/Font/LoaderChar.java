package com.gumse.gui.Font;

import java.nio.ByteBuffer;

import com.gumse.maths.ivec2;

public class LoaderChar {
    public ByteBuffer image;
    public ivec2 resolution;
    public int accent;

    public LoaderChar(ByteBuffer img, ivec2 res, int acc)
    {
        image = img;
        resolution = res;
        accent = acc;
    }
}
