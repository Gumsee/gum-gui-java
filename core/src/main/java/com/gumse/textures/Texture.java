package com.gumse.textures;

import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import com.gumse.maths.ivec2;
import com.gumse.tools.Output;
import com.gumse.tools.Toolbox;

import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int iTextureID;
    private ivec2 vTextureSize;
    private ByteBuffer bImageData;
    private int iComponents;
    private String sName;
    private boolean bCreateOpenGLTexture = true;

    public Texture(String name)
    {
        this();
        sName = name;
    }

    public Texture(String name, int comps, ivec2 res)
    {
        this();
        iComponents = comps;
        vTextureSize = res;
        sName = name;
    }

    public Texture()
    {
        vTextureSize = new ivec2();
        sName = "unnamed";
        iTextureID = GL20.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, iTextureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public boolean load(String resname, Class<?> classtouse)
    {
        ByteBuffer imageBuffer;
        imageBuffer = Toolbox.loadResourceToByteBuffer(resname, classtouse);

        if(imageBuffer == null)
            return false;

        boolean result = loadMemory(imageBuffer);
        MemoryUtil.memFree(imageBuffer);

        return result;
    }

    public boolean loadFile(String filename, Class<?> classtouse)
    {
        ByteBuffer imageBuffer;
        imageBuffer = Toolbox.loadFileToByteBuffer(filename, classtouse);

        if(imageBuffer == null)
            return false;

        boolean result = loadMemory(imageBuffer);

        MemoryUtil.memFree(imageBuffer);

        return result;
    }

    public boolean loadMemory(ByteBuffer imageBuffer)
    {
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);  

        //Debug.info(Integer.toString(imageBuffer.capacity()));
        if(imageBuffer == null)
            return false;

        if(!stbi_info_from_memory(imageBuffer, x, y, channels)) 
        {
            Output.warn(stbi_failure_reason());
            return false;
        }

        bImageData = stbi_load_from_memory(imageBuffer, x, y, channels, 0);

        if (bImageData == null) 
        {
            Output.warn("Failed to load Texture file " + sName + ": " + stbi_failure_reason());
            return false;
        }

        loadDirectly(bImageData, channels.get(0), new ivec2(x.get(0), y.get(0)), GL11.GL_UNSIGNED_BYTE);
        //stbi_image_free(bImageData);

        return true;
    }

    public void loadDirectly(ByteBuffer imageBuffer, int numcomps, ivec2 res, int datatype)
    {
        vTextureSize = res;
        iComponents = numcomps;
        bImageData = imageBuffer;
        if(bCreateOpenGLTexture)
        {
            int format = GL11.GL_RGBA;
            switch(iComponents)
            {
                case 4: format = GL11.GL_RGBA; break;
                case 3: format = GL11.GL_RGB; break;
                case 2: Output.error("Texture: 2 components not supported"); break;
                case 1: format = GL11.GL_RED; break;
            }
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, iTextureID);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, (int)vTextureSize.x, (int)vTextureSize.y, 0, format, datatype, bImageData);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            deleteData();
        }

        String msg = "Loaded Image " + sName;
        msg += " (" + Integer.toString((int)vTextureSize.x) + "x" + Integer.toString((int)vTextureSize.y) + ")";
        msg += " with " + Integer.toString(iComponents) + " compontents,";
        //msg += " has HDR = " + (stbi_is_hdr_from_memory(bImageData) ? "true" : "false");

        Output.debug(msg);
    }

    public ByteBuffer getData()
    {
        return bImageData;
    }

    public void deleteData()
    {
        if(bImageData != null)
        {
            stbi_image_free(bImageData);
            bImageData = null;
        }
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

    public static void unbind()
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

    public void dontCreateGLTexture() { this.bCreateOpenGLTexture = false; }

    public int getID() { return this.iTextureID; }
    public String getName() { return this.sName; }
    public int numComponents() { return this.iComponents; }
}
