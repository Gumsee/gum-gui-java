package com.gumse.textures;

import java.nio.*;
import java.io.*;
import java.util.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import java.lang.Exception;

import com.gumse.maths.vec2;
import com.gumse.tools.Debug;
import com.gumse.tools.Toolbox;
import org.apache.commons.io.IOUtils;

import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int iTextureID;
    private vec2 vTextureSize;
    private ByteBuffer bImageData;
    private int iComponents;


    public Texture()
    {
        iTextureID = GL20.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, iTextureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void load(String filename)
    {
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);  

        ByteBuffer imageBuffer;
        imageBuffer = Toolbox.loadResourceToByteBuffer(filename);
        //Debug.info(Integer.toString(imageBuffer.capacity()));
        if(imageBuffer.equals(null))
        {
            throw new RuntimeException();
        }

        if(!stbi_info_from_memory(imageBuffer, x, y, channels)) 
        {
            Debug.error(stbi_failure_reason());
            return;
        }

        bImageData = stbi_load_from_memory(imageBuffer, x, y, channels, 0);
        vTextureSize = new vec2(x.get(0), y.get(0));
        iComponents = channels.get(0);

        MemoryUtil.memFree(imageBuffer);

        if (bImageData == null) 
            Debug.error("Failed to read Texture file " + filename + ": " + stbi_failure_reason());

        int format = iComponents == 4 ? GL11.GL_RGBA : GL11.GL_RGB;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, iTextureID);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, (int)vTextureSize.x, (int)vTextureSize.y, 0, 
        format, GL11.GL_UNSIGNED_BYTE, bImageData);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        String msg = "Loaded Image " + filename;
        msg += " (" + Integer.toString((int)vTextureSize.x) + "x" + Integer.toString((int)vTextureSize.y) + ")";
        msg += " with " + Integer.toString(iComponents) + " compontents,";
        //msg += " has HDR = " + (stbi_is_hdr_from_memory(bImageData) ? "true" : "false");

        Debug.info(msg);
        stbi_image_free(bImageData);
    }

    public vec2 getSize()
    {
        return vTextureSize;
    }

    public void bind(int index)
    {
        GL20.glActiveTexture(GL20.GL_TEXTURE0 + index);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, iTextureID);
    }

    public void unbind()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }


    public static void unbindAny()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public int getID()
    {
        return this.iTextureID;
    }
}
