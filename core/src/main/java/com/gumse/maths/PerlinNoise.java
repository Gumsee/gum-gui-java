package com.gumse.maths;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gumse.textures.Texture;

public class PerlinNoise 
{

    static public float interpolate(float a, float b, float blend)
    {
        double theta = blend * Math.PI;
        float f = (float)(1.0f - Math.cos(theta)) * 0.5f;
        return a * (1.0f - f) + b * f;
    }

    static public float noise(int x, int y, int seed) 
    {   
        return tools.noise(x * 23451 + y * 57636 + seed) * 2.0f - 1.0f;
    }

    static public float smoothNoise(int x, int y, int seed) 
    {   
        float corners = (noise(x - 1, y - 1, seed) + noise(x + 1, y - 1, seed) + noise(x - 1, y + 1, seed) + noise(x + 1, y + 1, seed)) / 16.0f;
        float sides = (noise(x - 1, y, seed) + noise(x + 1, y, seed) + noise(x, y + 1, seed) + noise(x, y + 1, seed)) / 8.0f;
        float center = noise(x, y, seed) / 4.0f;
        return corners + sides + center;
    }

    static public float interpolatedNoise(float x, float y, int seed) 
    {
        int intX = (int)x;
        int intY = (int)y;
        float fracX = x - intX;
        float fracY = y - intY;

        float v1 = smoothNoise(intX, intY, seed);
        float v2 = smoothNoise(intX + 1, intY, seed);
        float v3 = smoothNoise(intX, intY + 1, seed);
        float v4 = smoothNoise(intX + 1, intY + 1, seed);

        float i1 = interpolate(v1, v2, fracX);
        float i2 = interpolate(v3, v4, fracX);

        return interpolate(i1, i2, fracY);
    }

    static public ArrayList<Float> getNoiseMap(int width, int height, float smoothness, int seed)
    {
        ArrayList<Float> retList = new ArrayList<Float>();
        for(int j = 0; j < height; j++)
            for(int i = 0; i < width; i++)
                retList.add(interpolatedNoise(i / smoothness, j / smoothness, seed));
        return retList;
    }

    public static Texture genTexture(int width, int height, int seed)
    {
        Texture retTex = new Texture();
        ByteBuffer bImageData = BufferUtils.createByteBuffer(width * height * 4 * 1);

        ArrayList<Float> noiseMap = getNoiseMap(width, height, 6.0f, seed);
        for(int i = 0; i < noiseMap.size(); i++)
        {
            float fnoise = noiseMap.get(i);
            fnoise += 1.0;
            fnoise /= 2.0;
            fnoise *= 255;
            bImageData.put((byte)fnoise);
        }
        System.out.println("Done");
        bImageData.flip();

        retTex.bind(0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RED, width, height, 0, 
        GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, bImageData);
        Texture.unbind();

        return retTex;
    }
}
