package com.gumse.maths;

import com.gumse.tools.Output;

public class mat4 {

    private float fMatrix[][] = {
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}
    };

    public mat4(){ }

    public mat4(mat3 val)
    {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                fMatrix[i][j] = val.get(i, j);
    }


    public mat4(float val)
    {
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                fMatrix[i][j] = val;
    }


    public mat4(float m00, float m01, float m02, float m03,
                float m10, float m11, float m12, float m13,
                float m20, float m21, float m22, float m23,
                float m30, float m31, float m32, float m33)
    {
        fMatrix[0][0] = m00; fMatrix[0][1] = m01; fMatrix[0][2] = m02; fMatrix[0][3] = m03;
        fMatrix[1][0] = m10; fMatrix[1][1] = m11; fMatrix[1][2] = m12; fMatrix[1][3] = m13;
        fMatrix[2][0] = m20; fMatrix[2][1] = m21; fMatrix[2][2] = m22; fMatrix[2][3] = m23;
        fMatrix[3][0] = m30; fMatrix[3][1] = m31; fMatrix[3][2] = m32; fMatrix[3][3] = m33;
    }

    /**
     * multiplies matrix 4x4 with a floating point value
     * | 1 0 0 0 |       | 5 0 0 0 |
     * | 0 1 0 0 | x 5 = | 0 5 0 0 |
     * | 0 0 1 0 |       | 0 0 5 0 |
     * | 0 0 0 1 |       | 0 0 0 5 |
     * @param val
     */
    public void mul(float val)
    {
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                fMatrix[i][j] *= val;
    }


    /**
     * multiplies matrix 4x4 with a vector 4x1
     * | 1 0 0 0 |               | x 0 0 0 |
     * | 0 1 0 0 | x (x y z w) = | 0 y 0 0 |
     * | 0 0 1 0 |               | 0 0 z 0 |
     * | 0 0 0 1 |               | 0 0 0 w |
     * @param val
     */
    public void mul(vec4 val)
    {
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                fMatrix[i][j] *= val.valueByIndex(i);
    }


    /**
     * transposes a 4x4 matrix
     *            | 1 2 3 4 |    | 1 1 1 1 |
     * transpose (| 1 2 3 4 |) = | 2 2 2 2 |
     *            | 1 2 3 4 |    | 3 3 3 3 |
     *            | 1 2 3 4 |    | 4 4 4 4 |
     * @return itself
     */
    public mat4 transpose()
    {
        mat4 tmpMat = new mat4();
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                tmpMat.set(i, j, this.get(j, i));
        this.set(tmpMat);
        return tmpMat;
    }


    /**
     * multiplies matrix 4x4 with a matrix 4x4
     * |  1  2  3  4 |   |  1  2  3  4 |   |  90 100 110 120 |
     * |  5  6  7  8 | x |  5  6  7  8 | = | 202 228 254 280 |
     * |  9 10 11 12 |   |  9 10 11 12 |   | 314 356 398 440 |
     * | 13 14 15 16 |   | 13 14 15 16 |   | 426 484 542 600 |
     * @param mat
     * @return itself
     */
    public mat4 mul(mat4 mat)
    {
        mat4 tmpMat = new mat4(0);
        int dim = 4;
        for(int i = 0; i < dim; i++)
            for(int j = 0; j < dim; j++)
                for(int k = 0; k < dim; k++)
                    tmpMat.set(i, j, tmpMat.get(i, j) + this.get(i, k) * mat.get(k, j));
        this.set(tmpMat);
        return tmpMat;
    }


    public void print()
    {
        String output = "\n";
        for(int i = 0; i < fMatrix.length; i++)
        {
            for(int j = 0; j < fMatrix[i].length; j++)
            {
                output += Float.toString(fMatrix[i][j]) + "\t";
            }
            output += "\n";
        }
        Output.info(output);
    }

    public void translate(vec3 transVector)
    {
        fMatrix[0][3] = transVector.x;
        fMatrix[1][3] = transVector.y;
        fMatrix[2][3] = transVector.z;
    }

    public void scale(vec3 scaleVector)
    {
        fMatrix[0][0] = scaleVector.x;
        fMatrix[1][1] = scaleVector.y;
        fMatrix[2][2] = scaleVector.z;
    }

    public void rotate(vec3 rotationVector)
    {
        mat3 rot = new mat3();
        mat3 rotx = new mat3();
        mat3 roty = new mat3();
        mat3 rotz = new mat3();
        rotx.rotateX(rotationVector.x);
        roty.rotateY(rotationVector.y);
        rotz.rotateZ(rotationVector.z);
        rot.mul(rotx);
        rot.mul(roty);
        rot.mul(rotz);
        
        this.mul(new mat4(rot));
    }
    
    /**
     * | (1 / tan(FOV / 2)) / aspectratio          0                         0                                  0               |
     * |                  0                 1 / tan(FOV / 2)                 0                                  0               |
     * |                  0                        0           -(far + near) / (far - near)    -(2 * far * near) / (far - near) |
     * |                  0                        0                        -1                                  0               |
     * @param FOV
     * @param aspectRatio
     * @param near
     * @param far
     * @return
     */
    public static mat4 perspective(float FOV, float aspectRatio, float near, float far)
    {
        mat4 perspectiveMat = new mat4();
        float scale = 1.0f / tools.tandeg(FOV * 0.5f); 
        perspectiveMat.set(0, 0, scale / aspectRatio);
        perspectiveMat.set(1, 1, scale); 
        perspectiveMat.set(2, 2, -(far + near) / (far - near)); 
        perspectiveMat.set(2, 3, -(2 * far * near) / (far - near)); 
        perspectiveMat.set(3, 2, -1);
        perspectiveMat.set(3, 3, 0);
        return perspectiveMat;
    }

    /*public static mat4 ortho(float top, float right, float bottom, float left, float near, float far)
    {
        mat4 orthoMat = new mat4();
        orthoMat.set(0, 0, 2.0f / (right - left));
        orthoMat.set(1, 1, 2.0f / (top - bottom));
        orthoMat.set(2, 2, -2.0f / (far - near));
        orthoMat.set(3, 3, 1.0f);

        orthoMat.set(0, 3, -(right + left) / (right - left));
        orthoMat.set(1, 3, -(top + bottom) / (top - bottom));
        orthoMat.set(2, 3, -(far + near) / (far - near));

        return orthoMat;
    }*/

    public static mat4 ortho(float top, float right, float bottom, float left, float near, float far)
    {
        mat4 orthoMat = new mat4();
        orthoMat.set(0, 0, 2.0f / (right - left));
        orthoMat.set(1, 1, 2.0f / (top - bottom));
        orthoMat.set(2, 2, -2.0f / (far - near));
        orthoMat.set(3, 3, 1.0f);

        orthoMat.set(3, 0, -(right + left) / (right - left));
        orthoMat.set(3, 1, -(top + bottom) / (top - bottom));
        orthoMat.set(3, 2, -(far + near) / (far - near));

        return orthoMat;
    }

    
    public static mat4 view(vec3 center, vec3 eye, vec3 up)
    {
        mat4 viewMat = new mat4();
        vec3  f = vec3.normalize(vec3.sub(eye, center));
        vec3  u = vec3.normalize(up);
        vec3  s = vec3.normalize(vec3.cross(f, u));
        u = vec3.cross(s, f);
    
        viewMat.set(0, 0,  s.x);
        viewMat.set(1, 0,  s.y);
        viewMat.set(2, 0,  s.z);
        viewMat.set(0, 1,  u.x);
        viewMat.set(1, 1,  u.y);
        viewMat.set(2, 1,  u.z);
        viewMat.set(0, 2, f.x);
        viewMat.set(1, 2, f.y);
        viewMat.set(2, 2, f.z);
        viewMat.set(3, 0, -vec3.dot(s, eye));
        viewMat.set(3, 1, -vec3.dot(u, eye));
        viewMat.set(3, 2, -vec3.dot(f, eye));
        return viewMat;
    }


    //
    //Getter
    //
    public float get(int row, int col) { return this.fMatrix[row][col]; }
    public float[][] getData()         { return this.fMatrix; }

    //
    // Setter
    //
    public void set(int row, int col, float val) { this.fMatrix[row][col] = val; }
    public void set(mat4 mat) 
    { 
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                this.fMatrix[i][j] = mat.get(i, j); 
    }
}
