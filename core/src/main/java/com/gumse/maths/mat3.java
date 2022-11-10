package com.gumse.maths;

import com.gumse.tools.Debug;

public class mat3 {

    private float fMatrix[][] = {
        {1, 0, 0},
        {0, 1, 0},
        {0, 0, 1}
    };

    public mat3(){ }


    public mat3(float val)
    {
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                fMatrix[i][j] = val;
    }


    public mat3(float m00, float m01, float m02,
                float m10, float m11, float m12,
                float m20, float m21, float m22)
    {
        fMatrix[0][0] = m00; fMatrix[0][1] = m01; fMatrix[0][2] = m02;
        fMatrix[1][0] = m10; fMatrix[1][1] = m11; fMatrix[1][2] = m12;
        fMatrix[2][0] = m20; fMatrix[2][1] = m21; fMatrix[2][2] = m22;
    }

    /**
     * multiplies matrix 3x3 with a floating point value
     * | 1 0 0 |       | 5 0 0 |
     * | 0 1 0 | x 5 = | 0 5 0 |
     * | 0 0 1 |       | 0 0 5 |
     * @param val
     */
    public void mul(float val)
    {
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                fMatrix[i][j] *= val;
    }


    /**
     * multiplies matrix 3x3 with a vector 1x3
     * | 1 0 0 |             | x 0 0 |
     * | 0 1 0 | x (x y z) = | 0 y 0 |
     * | 0 0 1 |             | 0 0 z |
     * @param val
     */
    public void mul(vec3 val)
    {
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                fMatrix[i][j] *= val.valueByIndex(i);
    }


    /**
     * inverts the axis' of a 3x3 matrix
     *         | 1 2 3 |    | 1 1 1 |
     * invert (| 1 2 3 |) = | 2 2 2 |
     *         | 1 2 3 |    | 3 3 3 |
     * @return itself
     */
    public mat3 invert()
    {
        mat3 tmpMat = new mat3();
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                tmpMat.set(i, j, this.get(j, i));
        this.set(tmpMat);
        return tmpMat;
    }


    /**
     * multiplies matrix 4x4 with a matrix 4x4
     * |  1  2  3 |   |  1  2  3 |   |  30  36  42 |
     * |  7  8  9 | x |  7  8  9 | = |  66  81  96 |
     * |  7  8  9 |   |  7  8  9 |   | 102 126 150 |
     * @param mat
     * @return itself
     */
    public mat3 mul(mat3 mat)
    {
        mat3 tmpMat = new mat3(0);
        int dim = 3;
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
        Debug.info(output);
    }

    public void rotateX(float deg)
    {
        fMatrix[1][1] = tools.cosdeg(deg);
        fMatrix[2][2] = tools.cosdeg(deg);
        fMatrix[1][2] = -tools.sindeg(deg);
        fMatrix[2][1] = tools.sindeg(deg);
    }

    public void rotateY(float deg)
    {
        fMatrix[0][0] = tools.cosdeg(deg);
        fMatrix[0][2] = tools.sindeg(deg);
        fMatrix[2][0] = -tools.sindeg(deg);
        fMatrix[2][2] = tools.cosdeg(deg);
    }

    public void rotateZ(float deg)
    {
        fMatrix[0][0] = tools.cosdeg(deg);
        fMatrix[0][1] = -tools.sindeg(deg);
        fMatrix[1][0] = tools.sindeg(deg);
        fMatrix[1][1] = tools.cosdeg(deg);
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
    public void set(mat3 mat) 
    { 
        for(int i = 0; i < fMatrix.length; i++)
            for(int j = 0; j < fMatrix[i].length; j++)
                this.fMatrix[i][j] = mat.get(i, j); 
    }
}
