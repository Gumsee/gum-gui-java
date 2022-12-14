package com.gumse.tools;

import java.nio.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import com.gumse.maths.*;

public class Toolbox 
{
    public static int StringToInt(String str)        { int ret = 0;      try { ret = Integer.parseInt(str);   } catch(NumberFormatException e) { Debug.error("StringToInt: couldn't convert string, invalid argument!");    } return ret; }
    public static float StringToFloat(String str)    { float ret = 0.0f; try { ret = Float.parseFloat(str);   } catch(NumberFormatException e) { Debug.error("StringToFloat: couldn't convert string, invalid argument!");  } return ret; }
    public static double StringToDouble(String str)  { double ret = 0.0; try { ret = Double.parseDouble(str); } catch(NumberFormatException e) { Debug.error("StringToDouble: couldn't convert string, invalid argument!"); } return ret; }

    public static vec2 StringToVec2(String str)
    {
        String[] numsStr = str.split(",");

        if(numsStr.length == 0) return new vec2(0.0f);
        if(numsStr.length == 1) return new vec2(StringToFloat(strExtractNumbers(numsStr[0])));

        return new vec2(
            StringToFloat(strExtractNumbers(numsStr[0])),
            StringToFloat(strExtractNumbers(numsStr[1]))
        );
    }

    public static vec3 StringToVec3(String str)
    {
        String[] numsStr = str.split(",");

        if(numsStr.length == 0) return new vec3(0.0f);
        if(numsStr.length == 1) return new vec3(StringToFloat(strExtractNumbers(numsStr[0])));

        return new vec3(
            StringToFloat(strExtractNumbers(numsStr[0])),
            StringToFloat(strExtractNumbers(numsStr[1])),
            StringToFloat(strExtractNumbers(numsStr[2]))
        );
    }

    public static vec4 StringToVec4(String str)
    {
        String[] numsStr = str.split(",");

        if(numsStr.length == 0) return new vec4(0.0f);
        if(numsStr.length == 1) return new vec4(StringToFloat(strExtractNumbers(numsStr[0])));

        return new vec4(
            StringToFloat(strExtractNumbers(numsStr[0])),
            StringToFloat(strExtractNumbers(numsStr[1])),
            StringToFloat(strExtractNumbers(numsStr[2])),
            StringToFloat(strExtractNumbers(numsStr[3]))
        );
    }

    public static String strExtractNumbers(String str)
    {
        String numStr = "";
        if(str == null)
            return "0";
        
        for(int i = 0; i < str.length(); i++)
        {
            if((str.charAt(i) >= '0' && str.charAt(i) <= '9') || str.charAt(i) == '-' || str.charAt(i) == '.')
            {
                numStr += str.charAt(i);
            }
        }
        return numStr;
    }


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

    public static ByteBuffer loadResourceToByteBuffer(String resource, Class<?> classtouse) 
    {
        ByteBuffer buffer = null;


        try 
        {
            InputStream source = classtouse.getClassLoader().getResourceAsStream(resource);
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

    public static String loadResourceAsString(String resource, Class<?> classtouse)
    {
        String retstr = "";
        try {
            retstr = new Scanner(classtouse.getClassLoader().getResourceAsStream(resource), "UTF-8").useDelimiter("\\A").next();
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
