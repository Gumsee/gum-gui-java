package com.gumse.maths;

import java.lang.Math;
import java.util.Random;

public class tools {

    private static Random randomGenerator = new Random();

    public static float sindeg(float deg) { return (float)Math.sin(Math.toRadians(deg)); }
    public static float cosdeg(float deg) { return (float)Math.cos(Math.toRadians(deg)); }
    public static float tandeg(float deg) { return (float)Math.tan(Math.toRadians(deg)); }

    public static mat4 createTransformationMatrix(vec3 translation, vec3 rotation, vec3 scale)
    {
        mat4 transmat = new mat4();
        transmat.translate(translation);
        transmat.scale(scale);
        transmat.rotate(rotation);
        return transmat;
    }

    public static double Lerp(double t, double a, double b) { return a + t * (b - a); }
    public static double fade(double t)                     { return ((6*t - 15)*t + 10)*t*t*t; }
    public static float randomf(float min, float max)       { return min + (float)Math.random() * (max - min); }
    public static float noise(int seed)                     { randomGenerator.setSeed(seed); return randomGenerator.nextFloat(); }
}
