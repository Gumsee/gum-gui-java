package com.gumse.tools;

import java.nio.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import com.gumse.maths.bbox2i;
import com.gumse.maths.ivec2;

public class Toolbox {
    public static FloatBuffer arrayList2FloatBuffer(ArrayList<Float> arrList)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(arrList.size());
        for(int i = 0; i < arrList.size(); i++)
            buffer.put(arrList.get(i));
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer array2D2FloatBuffer(float[][] arr)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(arr.length * arr[0].length);
        for(int i = 0; i < arr.length; i++)
            for(int j = 0; j < arr[i].length; j++)
                buffer.put(arr[i][j]);
        buffer.flip();
        return buffer;
    }

    public static IntBuffer arrayList2IntBuffer(ArrayList<Integer> arrList)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(arrList.size());
        for(int i = 0; i < arrList.size(); i++)
            buffer.put(arrList.get(i));
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer loadResourceToByteBuffer(String resource) 
    {
        ByteBuffer buffer = null;


        try 
        {
            InputStream source = Toolbox.class.getClassLoader().getResourceAsStream(resource);
            byte[] bytes = source.readAllBytes();
            buffer = MemoryUtil.memAlloc(bytes.length);
            for(int i = 0; i < bytes.length; i++)
            {
                //System.out.println(String.format("%02X ", bytes[i]));
                buffer.put(i, bytes[i]);
            }
        }
        catch(Exception e)
        {
            Debug.error("Failed to read resource \"" + resource + "\" into bytebuffer: " + e.getMessage());
        }

        return buffer;
    }

    public static String loadResourceAsString(String resource)
    {
        String retstr = "";
        try {
            retstr = new Scanner(Toolbox.class.getClassLoader().getResourceAsStream(resource), "UTF-8").useDelimiter("\\A").next();
        }
        //catch(IOException e) { Debug.error("Failed to read resource \"" + resource + "\": " + e.getMessage()); }
        catch(Exception e) { Debug.error("Failed to read resource \"" + resource + "\": " + e.getMessage()); }

        return retstr;
    }


    public static boolean checkBoxIntersection(bbox2i bbox1, bbox2i bbox2)
    {
        return  bbox1.getPos().x                     <= bbox2.getPos().x + bbox2.getSize().x &&
                bbox1.getPos().x + bbox1.getSize().x >= bbox2.getPos().x                     && 
                bbox1.getPos().y                     <= bbox2.getPos().y + bbox2.getSize().y && 
                bbox1.getPos().y + bbox1.getSize().y >= bbox2.getPos().y;
    }

    public static boolean checkPointInBox(ivec2 point, bbox2i bbox)
    {
        return  point.x <= bbox.getPos().x + bbox.getSize().x &&
                point.x >= bbox.getPos().x && 
                point.y <= bbox.getPos().y + bbox.getSize().y && 
                point.y >= bbox.getPos().y;
    }
}
