package com.gumse.maths;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gumse.textures.Texture;

public class RandomNoise 
{
    public static float noise(int x, int y, int seed)
    {
        return tools.noise(x * 23451 + y * 57636 + seed);
    }

    public static Texture genTexture(int width, int height, int seed)
    {
        Texture retTex = new Texture();
        ByteBuffer bImageData = BufferUtils.createByteBuffer(width * height * 4 * 4);
        
        //Random generator = new Random(seed);
        for(int j = 0; j < height; j++)
        {
            for(int i = 0; i < width; i++)
            {
                float inoise = noise(i, j, seed);
                System.out.println(inoise);
                inoise *= 255;
                byte bnoise = (byte)inoise;
                bImageData.put(bnoise);
                bImageData.put(bnoise);
                bImageData.put(bnoise);
                bImageData.put((byte) 0xFF);
            }
        }
        System.out.println("Done");
        bImageData.flip();

        retTex.bind(0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, 
        GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bImageData);
        retTex.unbind();

        return retTex;
    }
}
