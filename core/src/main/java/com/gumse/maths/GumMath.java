package com.gumse.maths;

public class GumMath {
    public static <T extends Number & Comparable<T>> T clamp(T var, T min, T max)
    {           
        if(var.compareTo(min) < 0) { var = min; }
        if(var.compareTo(max) > 0) { var = max; }
        return var;
    }
}
