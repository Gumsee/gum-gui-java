package com.gumse.textures;

import java.nio.*;
import java.io.*;
import java.util.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import java.lang.Exception;

import com.gumse.maths.ivec2;
import com.gumse.maths.vec2;
import com.gumse.tools.Debug;
import com.gumse.tools.Toolbox;
import org.apache.commons.io.IOUtils;

import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int iTextureID;
    private ivec2 vTextureSize;
    private ByteBuffer bImageData;
    private int iComponents;
    private String sName = "unnamed";

    private void create()
    {
        iTextureID = GL20.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, iTextureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public Texture(String name)
    {
        sName = name;
        create();
    }

    public Texture(String name, int comps, ivec2 res)
    {
        iComponents = comps;
        vTextureSize = res;
        sName = name;
        create();
    }

    public Texture()
    {
        create();
    }

    public void load(String filename)
    {
        ByteBuffer imageBuffer;
        imageBuffer = Toolbox.loadResourceToByteBuffer(filename);

        if(imageBuffer.equals(null))
            throw new RuntimeException();

        loadMemory(imageBuffer);

        MemoryUtil.memFree(imageBuffer);
    }

    public void loadMemory(ByteBuffer imageBuffer)
    {
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);  

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

        if (bImageData == null) 
            Debug.error("Failed to load Texture file " + sName + ": " + stbi_failure_reason());

        loadDirectly(bImageData, channels.get(0), new ivec2(x.get(0), y.get(0)), GL11.GL_UNSIGNED_BYTE);
        stbi_image_free(bImageData);
    }

    public void loadDirectly(ByteBuffer imageBuffer, int numcomps, ivec2 res, int datatype)
    {
        vTextureSize = res;
        iComponents = numcomps;
        bImageData = imageBuffer;
        int format = GL11.GL_RGBA;
        switch(iComponents)
        {
            case 4: format = GL11.GL_RGBA; break;
            case 3: format = GL11.GL_RGB; break;
            case 2: Debug.error("Texture: 2 components not supported"); break;
            case 1: format = GL11.GL_RED; break;
        }

        for(int j = 0; j < res.x; j++)
        {
            for(int k = 0; k < res.y; k++)
            {
                byte aByte = imageBuffer.get(j * res.y + k);
                
                int number = aByte & 0xff;
                System.out.print(number);
            }
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, iTextureID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, (int)vTextureSize.x, (int)vTextureSize.y, 0, format, datatype, bImageData);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        String msg = "Loaded Image " + sName;
        msg += " (" + Integer.toString((int)vTextureSize.x) + "x" + Integer.toString((int)vTextureSize.y) + ")";
        msg += " with " + Integer.toString(iComponents) + " compontents,";
        //msg += " has HDR = " + (stbi_is_hdr_from_memory(bImageData) ? "true" : "false");

        Debug.info(msg);
    }

    public ivec2 getSize()
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

    public void cleanup()
    {
        GL11.glDeleteTextures(iTextureID);
    }

    public static void unbindAny()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public int getID()
    {
        return this.iTextureID;
    }

    public String getName()
    {
        return this.sName;
    }
}
